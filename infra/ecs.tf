resource "aws_ecs_cluster" "main" {
  name = "${var.project}-${var.env}-cluster"
}

resource "aws_cloudwatch_log_group" "ecs" {
  name              = "/ecs/${var.project}-${var.env}"
  retention_in_days = 30
}
