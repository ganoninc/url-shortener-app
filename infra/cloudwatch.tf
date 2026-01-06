resource "aws_cloudwatch_log_group" "api_gateway" {
  name              = "/ecs/${var.project}-${var.env}-api-gateway"
  retention_in_days = 30
}

resource "aws_cloudwatch_log_group" "auth_service" {
  name              = "/ecs/${var.project}-${var.env}-auth-service"
  retention_in_days = 30
}

resource "aws_cloudwatch_log_group" "url_service" {
  name              = "/ecs/${var.project}-${var.env}-url-service"
  retention_in_days = 30
}

resource "aws_cloudwatch_log_group" "redirector_service" {
  name              = "/ecs/${var.project}-${var.env}-redirector-service"
  retention_in_days = 30
}

resource "aws_cloudwatch_log_group" "analytics_service" {
  name              = "/ecs/${var.project}-${var.env}-analytics-service"
  retention_in_days = 30
}
