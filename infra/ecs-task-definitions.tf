resource "aws_ecs_task_definition" "api_gateway" {
  family = "${var.project}-${var.env}-api-gateway"

  cpu    = 256
  memory = 512

  requires_compatibilities = ["FARGATE"]
  network_mode             = "awsvpc"

  execution_role_arn = aws_iam_role.ecs_task_exec_api_gateway.arn

  task_role_arn = aws_iam_role.ecs_task_api_gateway.arn

  container_definitions = jsonencode([
    {
      name  = "api-gateway"
      image = "${aws_ecr_repository.api_gateway.repository_url}:7951d71"

      cpu       = 256
      memory    = 512
      essential = true

      portMappings = [
        {
          containerPort = 8000
          hostPort      = 8000
          protocol      = "tcp"
        },
        {
          containerPort = 8100
          hostPort      = 8100
          protocol      = "tcp"
        }
      ]

      environment = [
        { name = "KONG_PROXY_LISTEN", value = "0.0.0.0:8000" },
        { name = "KONG_ADMIN_LISTEN", value = "0.0.0.0:8001" },
        { name = "KONG_DATABASE", value = "off" },
        { name = "KONG_ROUTER_FLAVOR", value = "expressions" },
        { name = "KONG_STATUS_LISTEN", value = "0.0.0.0:8100" },
        { name = "KONG_VAULTS", value = "env" },
        { name = "KONG_DECLARATIVE_CONFIG", value = "/kong/declarative/kong.yml" },
        { name = "KONG_PLUGINS", value = "bundled,jwt,jwt-claims-headers" },
        { name = "KONG_TRUSTED_IPS", value = var.vpc_cidr },
        { name = "KONG_REAL_IP_HEADER", value = "X-Forwarded-For" },
        { name = "KONG_REAL_IP_RECURSIVE", value = "on" },

        // CORS
        { name = "CORS_ALLOWED_ORIGINS", value = "[http://${module.alb.dns_name}]" },

        // Cloud Map service hosts and ports
        { name = "AUTH_SERVICE_HOST", value = "auth-service.${local.cloudmap_namespace}" },
        { name = "AUTH_SERVICE_PORT", value = tostring(var.auth_service_port) },
        { name = "URL_SERVICE_HOST", value = "url-service.${local.cloudmap_namespace}" },
        { name = "URL_SERVICE_PORT", value = tostring(var.url_service_port) },
        { name = "REDIRECTOR_SERVICE_HOST", value = "redirector-service.${local.cloudmap_namespace}" },
        { name = "REDIRECTOR_SERVICE_PORT", value = tostring(var.redirector_service_port) },
        { name = "ANALYTICS_SERVICE_HOST", value = "analytics-service.${local.cloudmap_namespace}" },
        { name = "ANALYTICS_SERVICE_PORT", value = tostring(var.analytics_service_port) },
      ]

      secrets = [
        {
          name      = "JWT_SECRET"
          valueFrom = "${aws_secretsmanager_secret.jwt.arn}:jwt_secret::"
        },
      ]

      healthCheck = {
        command     = ["CMD-SHELL", "curl -f http://localhost:8100/status || exit 1"]
        interval    = 30
        timeout     = 5
        retries     = 3
        startPeriod = 15
      }

      logConfiguration = {
        logDriver = "awslogs"
        options = {
          "awslogs-group"         = aws_cloudwatch_log_group.api_gateway.name
          "awslogs-region"        = var.aws_region
          "awslogs-stream-prefix" = "ecs"
        }
      }
    }
  ])

  tags = {
    service = "api-gateway"
  }
}

resource "aws_ecs_task_definition" "auth_service" {
  family = "${var.project}-${var.env}-auth-service"

  cpu    = 256
  memory = 512

  requires_compatibilities = ["FARGATE"]
  network_mode             = "awsvpc"

  execution_role_arn = aws_iam_role.ecs_task_exec_auth_service.arn

  # Auth service currently has no AWS runtime permissions

  container_definitions = jsonencode([
    {
      name  = "auth-service"
      image = "${aws_ecr_repository.auth_service.repository_url}:a87b232"

      cpu       = 256
      memory    = 512
      essential = true

      portMappings = [
        {
          containerPort = var.auth_service_port
          hostPort      = var.auth_service_port
          protocol      = "tcp"
        },
      ]

      environment = [
        { name = "SPRING_PROFILES_ACTIVE", value = "prod" },
        { name = "JWT_EXPIRATION_MS", value = "${var.jwt_expiration_ms}" },
        { name = "JWT_REFRESH_TOKEN_EXPIRATION_MS", value = "${var.jwt_refresh_token_expiration_ms}" },
        { name = "SERVER_PORT", value = tostring(var.auth_service_port) },
        { name = "DATABASE_URL", value = "jdbc:postgresql://${aws_db_instance.postgres.address}:5432/${var.pg_db_name}?sslmode=require&stringtype=unspecified" },
        { name = "DATABASE_USERNAME", value = var.pg_username },
        { name = "FRONTEND_ORIGIN", value = "http://${module.alb.dns_name}" },
        { name = "MANAGEMENT_SERVER_PORT", value = "9000" },
      ]

      secrets = [
        {
          name      = "DATABASE_PASSWORD"
          valueFrom = "${aws_secretsmanager_secret.db.arn}:password::"
        },
        {
          name      = "JWT_SECRET"
          valueFrom = "${aws_secretsmanager_secret.jwt.arn}:jwt_secret::"
        },
        {
          name      = "GOOGLE_CLIENT_ID"
          valueFrom = "${aws_secretsmanager_secret.google_oauth.arn}:google_client_id::"
        },
        {
          name      = "GOOGLE_CLIENT_SECRET"
          valueFrom = "${aws_secretsmanager_secret.google_oauth.arn}:google_client_secret::"
        },
      ]

      healthCheck = {
        command     = ["CMD-SHELL", "curl -f http://localhost:9000/actuator/health || exit 1"]
        interval    = 30
        timeout     = 5
        retries     = 3
        startPeriod = 120
      }

      logConfiguration = {
        logDriver = "awslogs"
        options = {
          "awslogs-group"         = aws_cloudwatch_log_group.auth_service.name
          "awslogs-region"        = var.aws_region
          "awslogs-stream-prefix" = "ecs"
        }
      }
    }
  ])

  tags = {
    service = "auth-service"
  }
}

resource "aws_ecs_task_definition" "url_service" {
  family = "${var.project}-${var.env}-url-service"

  cpu    = 256
  memory = 512

  requires_compatibilities = ["FARGATE"]
  network_mode             = "awsvpc"

  execution_role_arn = aws_iam_role.ecs_task_exec_url_service.arn

  task_role_arn = aws_iam_role.ecs_task_url_service.arn

  container_definitions = jsonencode([
    {
      name  = "url-service"
      image = "${aws_ecr_repository.url_service.repository_url}:f15d5a7"

      cpu       = 256
      memory    = 512
      essential = true

      portMappings = [
        {
          containerPort = var.url_service_port
          hostPort      = var.url_service_port
          protocol      = "tcp"
        },
      ]

      environment = [
        { name = "SPRING_PROFILES_ACTIVE", value = "prod" },
        { name = "KAFKA_URL", value = aws_msk_serverless_cluster.kafka.bootstrap_brokers_sasl_iam },
        { name = "KAFKA_SECURITY_PROTOCOL", value = "SASL_SSL" },
        { name = "KAFKA_SASL_MECHANISM", value = "AWS_MSK_IAM" },
        { name = "KAFKA_JAAS_CONFIG", value = "software.amazon.msk.auth.iam.IAMLoginModule required;" },
        { name = "KAFKA_CALLBACK_HANDLER", value = "software.amazon.msk.auth.iam.IAMClientCallbackHandler" },
        { name = "SERVER_PORT", value = tostring(var.url_service_port) },
        { name = "DATABASE_URL", value = "jdbc:postgresql://${aws_db_instance.postgres.address}:5432/${var.pg_db_name}?sslmode=require&stringtype=unspecified" },
        { name = "DATABASE_USERNAME", value = var.pg_username },
        { name = "MANAGEMENT_SERVER_PORT", value = "9000" },
      ]

      secrets = [
        {
          name      = "DATABASE_PASSWORD"
          valueFrom = "${aws_secretsmanager_secret.db.arn}:password::"
        },
      ]

      healthCheck = {
        command     = ["CMD-SHELL", "curl -f http://localhost:9000/actuator/health || exit 1"]
        interval    = 30
        timeout     = 5
        retries     = 3
        startPeriod = 120
      }

      logConfiguration = {
        logDriver = "awslogs"
        options = {
          "awslogs-group"         = aws_cloudwatch_log_group.url_service.name
          "awslogs-region"        = var.aws_region
          "awslogs-stream-prefix" = "ecs"
        }
      }
    }
  ])

  tags = {
    service = "url-service"
  }
}

resource "aws_ecs_task_definition" "redirector_service" {
  family = "${var.project}-${var.env}-redirector-service"

  cpu    = 256
  memory = 512

  requires_compatibilities = ["FARGATE"]
  network_mode             = "awsvpc"

  execution_role_arn = aws_iam_role.ecs_task_exec_redirector_service.arn

  task_role_arn = aws_iam_role.ecs_task_redirector_service.arn

  container_definitions = jsonencode([
    {
      name  = "redirector-service"
      image = "${aws_ecr_repository.redirector_service.repository_url}:f15d5a7"

      cpu       = 256
      memory    = 512
      essential = true

      portMappings = [
        {
          containerPort = var.redirector_service_port
          hostPort      = var.redirector_service_port
          protocol      = "tcp"
        },
      ]

      environment = [
        { name = "SPRING_PROFILES_ACTIVE", value = "prod" },
        { name = "KAFKA_URL", value = aws_msk_serverless_cluster.kafka.bootstrap_brokers_sasl_iam },
        { name = "KAFKA_SECURITY_PROTOCOL", value = "SASL_SSL" },
        { name = "KAFKA_SASL_MECHANISM", value = "AWS_MSK_IAM" },
        { name = "KAFKA_JAAS_CONFIG", value = "software.amazon.msk.auth.iam.IAMLoginModule required;" },
        { name = "KAFKA_CALLBACK_HANDLER", value = "software.amazon.msk.auth.iam.IAMClientCallbackHandler" },
        { name = "SERVER_PORT", value = tostring(var.redirector_service_port) },
        { name = "DATABASE_URL", value = "jdbc:postgresql://${aws_db_instance.postgres.address}:5432/${var.pg_db_name}?sslmode=require&stringtype=unspecified" },
        { name = "DATABASE_USERNAME", value = var.pg_username },
        { name = "MANAGEMENT_SERVER_PORT", value = "9000" },
      ]

      secrets = [
        {
          name      = "DATABASE_PASSWORD"
          valueFrom = "${aws_secretsmanager_secret.db.arn}:password::"
        },
      ]

      healthCheck = {
        command     = ["CMD-SHELL", "curl -f http://localhost:9000/actuator/health || exit 1"]
        interval    = 30
        timeout     = 5
        retries     = 3
        startPeriod = 120
      }

      logConfiguration = {
        logDriver = "awslogs"
        options = {
          "awslogs-group"         = aws_cloudwatch_log_group.redirector_service.name
          "awslogs-region"        = var.aws_region
          "awslogs-stream-prefix" = "ecs"
        }
      }
    }
  ])

  tags = {
    service = "redirector-service"
  }
}

resource "aws_ecs_task_definition" "analytics_service" {
  family = "${var.project}-${var.env}-analytics-service"

  cpu    = 256
  memory = 512

  requires_compatibilities = ["FARGATE"]
  network_mode             = "awsvpc"

  execution_role_arn = aws_iam_role.ecs_task_exec_analytics_service.arn

  task_role_arn = aws_iam_role.ecs_task_analytics_service.arn

  container_definitions = jsonencode([
    {
      name  = "analytics-service"
      image = "${aws_ecr_repository.analytics_service.repository_url}:7951d71"

      cpu       = 256
      memory    = 512
      essential = true

      portMappings = [
        {
          containerPort = var.analytics_service_port
          hostPort      = var.analytics_service_port
          protocol      = "tcp"
        },
      ]

      environment = [
        { name = "SPRING_PROFILES_ACTIVE", value = "prod" },
        { name = "KAFKA_URL", value = aws_msk_serverless_cluster.kafka.bootstrap_brokers_sasl_iam },
        { name = "KAFKA_SECURITY_PROTOCOL", value = "SASL_SSL" },
        { name = "KAFKA_SASL_MECHANISM", value = "AWS_MSK_IAM" },
        { name = "KAFKA_JAAS_CONFIG", value = "software.amazon.msk.auth.iam.IAMLoginModule required;" },
        { name = "KAFKA_CALLBACK_HANDLER", value = "software.amazon.msk.auth.iam.IAMClientCallbackHandler" },
        { name = "SERVER_PORT", value = tostring(var.analytics_service_port) },
        { name = "DATABASE_URL", value = "jdbc:postgresql://${aws_db_instance.postgres.address}:5432/${var.pg_db_name}?sslmode=require&stringtype=unspecified" },
        { name = "DATABASE_USERNAME", value = var.pg_username },
        { name = "MANAGEMENT_SERVER_PORT", value = "9000" },
      ]

      secrets = [
        {
          name      = "DATABASE_PASSWORD"
          valueFrom = "${aws_secretsmanager_secret.db.arn}:password::"
        },
      ]

      healthCheck = {
        command     = ["CMD-SHELL", "curl -f http://localhost:9000/actuator/health || exit 1"]
        interval    = 30
        timeout     = 5
        retries     = 3
        startPeriod = 120
      }

      logConfiguration = {
        logDriver = "awslogs"
        options = {
          "awslogs-group"         = aws_cloudwatch_log_group.analytics_service.name
          "awslogs-region"        = var.aws_region
          "awslogs-stream-prefix" = "ecs"
        }
      }
    }
  ])

  tags = {
    service = "analytics-service"
  }
}
