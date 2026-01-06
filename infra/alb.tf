module "alb" {
  source  = "terraform-aws-modules/alb/aws"
  version = "~> 9.0"

  name   = "${var.project}-${var.env}-alb"
  vpc_id = module.vpc.vpc_id

  subnets = module.vpc.public_subnets

  enable_deletion_protection = false

  create_security_group = false
  security_groups       = [aws_security_group.alb.id]

  target_groups = {
    api_gateway = {
      backend_port         = 8000
      backend_protocol     = "HTTP"
      target_type          = "ip"
      deregistration_delay = 30
      create_attachment    = false
      health_check = {
        enabled             = true
        path                = "/status"
        protocol            = "HTTP"
        port                = 8100
        healthy_threshold   = 3
        unhealthy_threshold = 3
        interval            = 30
        matcher             = "200"
      }
    }
  }

  listeners = {
    http = {
      port     = 80
      protocol = "HTTP"

      forward = {
        target_group_key = "api_gateway"
      }
    }
  }

  tags = {
    service   = "api-gateway"
    component = "alb"
  }
}

