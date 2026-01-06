resource "aws_security_group" "alb" {
  name        = "${var.project}-${var.env}-alb-sg"
  description = "Security group for the ALB"
  vpc_id      = module.vpc.vpc_id

  ingress {
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
    description = "Allow HTTP from internet"
  }

  egress {
    from_port   = 8000
    to_port     = 8000
    protocol    = "tcp"
    cidr_blocks = [module.vpc.vpc_cidr_block]
    description = "Allow ALB outbound to VPC (api-gateway traffic)"
  }

  egress {
    from_port   = 8100
    to_port     = 8100
    protocol    = "tcp"
    cidr_blocks = [module.vpc.vpc_cidr_block]
    description = "Allow ALB outbound to VPC (health checks)"
  }

  tags = {
    component = "alb"
  }
}

resource "aws_security_group" "api_gateway" {
  name        = "${var.project}-${var.env}-api-gateway-sg"
  description = "Security group for api-gateway"
  vpc_id      = module.vpc.vpc_id

  ingress {
    security_groups = [aws_security_group.alb.id]
    from_port       = 8000
    to_port         = 8000
    protocol        = "tcp"
    description     = "Allow HTTP traffic from ALB to Kong proxy"
  }

  ingress {
    security_groups = [aws_security_group.alb.id]
    from_port       = 8100
    to_port         = 8100
    protocol        = "tcp"
    description     = "Allow ALB health checks to Kong status endpoint"
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    service = "api-gateway"
  }
}

resource "aws_security_group" "auth_service" {
  name        = "${var.project}-${var.env}-auth-service-sg"
  description = "Security group for auth-service"
  vpc_id      = module.vpc.vpc_id

  ingress {
    cidr_blocks = [module.vpc.vpc_cidr_block]
    from_port   = var.auth_service_port
    to_port     = var.auth_service_port
    protocol    = "tcp"
    description = "Allow api-gateway to reach auth-service"
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    service = "auth-service"
  }
}

resource "aws_security_group" "url_service" {
  name        = "${var.project}-${var.env}-url-service-sg"
  description = "Security group for url-service"
  vpc_id      = module.vpc.vpc_id

  ingress {
    cidr_blocks = [module.vpc.vpc_cidr_block]
    from_port   = var.url_service_port
    to_port     = var.url_service_port
    protocol    = "tcp"
    description = "Allow api-gateway to reach url-service"
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    service = "url-service"
  }
}

resource "aws_security_group" "redirector_service" {
  name        = "${var.project}-${var.env}-redirector-service-sg"
  description = "Security group for redirector-service"
  vpc_id      = module.vpc.vpc_id

  ingress {
    cidr_blocks = [module.vpc.vpc_cidr_block]
    from_port   = var.redirector_service_port
    to_port     = var.redirector_service_port
    protocol    = "tcp"
    description = "Allow api-gateway to reach redirector-service"
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    service = "redirector-service"
  }
}

resource "aws_security_group" "analytics_service" {
  name        = "${var.project}-${var.env}-analytics-service-sg"
  description = "Security group for analytics-service"
  vpc_id      = module.vpc.vpc_id

  ingress {
    cidr_blocks = [module.vpc.vpc_cidr_block]
    from_port   = var.analytics_service_port
    to_port     = var.analytics_service_port
    protocol    = "tcp"
    description = "Allow api-gateway to reach analytics-service"
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    service = "analytics-service"
  }
}

resource "aws_security_group" "rds" {
  name   = "${var.project}-${var.env}-rds"
  vpc_id = module.vpc.vpc_id

  ingress {
    description = "Allow Postgres from microservices ECS tasks"
    from_port   = 5432
    to_port     = 5432
    protocol    = "tcp"
    security_groups = [
      aws_security_group.auth_service.id,
      aws_security_group.url_service.id,
      aws_security_group.redirector_service.id,
      aws_security_group.analytics_service.id
    ]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    component = "rds"
  }
}

resource "aws_security_group" "msk" {
  name   = "${var.project}-${var.env}-msk"
  vpc_id = module.vpc.vpc_id

  ingress {
    description = "Allow MSK from microservices ECS tasks"
    from_port   = 9098
    to_port     = 9098
    protocol    = "tcp"
    security_groups = [
      aws_security_group.auth_service.id,
      aws_security_group.url_service.id,
      aws_security_group.redirector_service.id,
      aws_security_group.analytics_service.id
    ]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    component = "msk"
  }
}
