resource "aws_ecs_service" "api_gateway" {
  name            = "${var.project}-${var.env}-api-gateway-ecs-service"
  cluster         = aws_ecs_cluster.main.id
  task_definition = aws_ecs_task_definition.api_gateway.arn

  desired_count = 1
  launch_type   = "FARGATE"

  enable_execute_command = true

  load_balancer {
    target_group_arn = module.alb.target_groups.api_gateway.arn
    container_name   = "api-gateway"
    container_port   = 8000
  }

  network_configuration {
    subnets          = module.vpc.private_subnets
    security_groups  = [aws_security_group.api_gateway.id]
    assign_public_ip = false
  }

  deployment_controller {
    type = "ECS"
  }

  deployment_circuit_breaker {
    enable   = true
    rollback = true
  }

  health_check_grace_period_seconds = 60

  tags = {
    service = "api-gateway"
  }

  #   lifecycle {
  #     ignore_changes = [
  #       task_definition,
  #       desired_count
  #     ]
  #   }
}

resource "aws_ecs_service" "auth_service" {
  name            = "${var.project}-${var.env}-auth-service-ecs-service"
  cluster         = aws_ecs_cluster.main.id
  task_definition = aws_ecs_task_definition.auth_service.arn

  desired_count = 1
  launch_type   = "FARGATE"

  network_configuration {
    subnets          = module.vpc.private_subnets
    security_groups  = [aws_security_group.auth_service.id]
    assign_public_ip = false
  }

  service_registries {
    registry_arn = aws_service_discovery_service.auth_service.arn
  }

  deployment_controller {
    type = "ECS"
  }

  deployment_circuit_breaker {
    enable   = true
    rollback = true
  }

  health_check_grace_period_seconds = 60

  tags = {
    service = "auth-service"
  }

  #   lifecycle {
  #     ignore_changes = [
  #       task_definition,
  #       desired_count
  #     ]
  #   }
}

resource "aws_ecs_service" "url_service" {
  name            = "${var.project}-${var.env}-url-service-ecs-service"
  cluster         = aws_ecs_cluster.main.id
  task_definition = aws_ecs_task_definition.url_service.arn

  desired_count = 1
  launch_type   = "FARGATE"

  network_configuration {
    subnets          = module.vpc.private_subnets
    security_groups  = [aws_security_group.url_service.id]
    assign_public_ip = false
  }

  service_registries {
    registry_arn = aws_service_discovery_service.url_service.arn
  }

  deployment_controller {
    type = "ECS"
  }

  deployment_circuit_breaker {
    enable   = true
    rollback = true
  }

  health_check_grace_period_seconds = 60

  tags = {
    service = "url-service"
  }

  #   lifecycle {
  #     ignore_changes = [
  #       task_definition,
  #       desired_count
  #     ]
  #   }
}

resource "aws_ecs_service" "redirector_service" {
  name            = "${var.project}-${var.env}-redirector-service-ecs-service"
  cluster         = aws_ecs_cluster.main.id
  task_definition = aws_ecs_task_definition.redirector_service.arn

  desired_count = 1
  launch_type   = "FARGATE"

  network_configuration {
    subnets          = module.vpc.private_subnets
    security_groups  = [aws_security_group.redirector_service.id]
    assign_public_ip = false
  }

  service_registries {
    registry_arn = aws_service_discovery_service.redirector_service.arn
  }

  deployment_controller {
    type = "ECS"
  }

  deployment_circuit_breaker {
    enable   = true
    rollback = true
  }

  health_check_grace_period_seconds = 60

  tags = {
    service = "redirector-service"
  }

  #   lifecycle {
  #     ignore_changes = [
  #       task_definition,
  #       desired_count
  #     ]
  #   }
}

resource "aws_ecs_service" "analytics_service" {
  name            = "${var.project}-${var.env}-analytics-service-ecs-service"
  cluster         = aws_ecs_cluster.main.id
  task_definition = aws_ecs_task_definition.analytics_service.arn

  desired_count = 1
  launch_type   = "FARGATE"

  network_configuration {
    subnets          = module.vpc.private_subnets
    security_groups  = [aws_security_group.analytics_service.id]
    assign_public_ip = false
  }

  service_registries {
    registry_arn = aws_service_discovery_service.analytics_service.arn
  }

  deployment_controller {
    type = "ECS"
  }

  deployment_circuit_breaker {
    enable   = true
    rollback = true
  }

  health_check_grace_period_seconds = 60

  tags = {
    service = "analytics-service"
  }

  #   lifecycle {
  #     ignore_changes = [
  #       task_definition,
  #       desired_count
  #     ]
  #   }
}
