resource "aws_iam_role" "ecs_task_api_gateway" {
  name               = "${var.project}-${var.env}-ecs-task-api-gateway-role"
  assume_role_policy = data.aws_iam_policy_document.ecs_task_assume_role.json
}

resource "aws_iam_role" "ecs_task_url_service" {
  name               = "${var.project}-${var.env}-ecs-task-url-service"
  assume_role_policy = data.aws_iam_policy_document.ecs_task_assume_role.json
}

resource "aws_iam_role" "ecs_task_redirector_service" {
  name               = "${var.project}-${var.env}-ecs-task-redirector-service"
  assume_role_policy = data.aws_iam_policy_document.ecs_task_assume_role.json
}

resource "aws_iam_role" "ecs_task_analytics_service" {
  name               = "${var.project}-${var.env}-ecs-task-analytics-service"
  assume_role_policy = data.aws_iam_policy_document.ecs_task_assume_role.json
}
