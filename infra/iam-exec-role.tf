resource "aws_iam_role" "ecs_task_exec_api_gateway" {
  name               = "${var.project}-${var.env}-api-gateway-exec-role"
  assume_role_policy = data.aws_iam_policy_document.ecs_task_assume_role.json
}

resource "aws_iam_role" "ecs_task_exec_auth_service" {
  name               = "${var.project}-${var.env}-auth-service-exec-role"
  assume_role_policy = data.aws_iam_policy_document.ecs_task_assume_role.json
}

resource "aws_iam_role" "ecs_task_exec_url_service" {
  name               = "${var.project}-${var.env}-url-service-exec-role"
  assume_role_policy = data.aws_iam_policy_document.ecs_task_assume_role.json
}

resource "aws_iam_role" "ecs_task_exec_redirector_service" {
  name               = "${var.project}-${var.env}-redirector-service-exec-role"
  assume_role_policy = data.aws_iam_policy_document.ecs_task_assume_role.json
}

resource "aws_iam_role" "ecs_task_exec_analytics_service" {
  name               = "${var.project}-${var.env}-analytics-service-exec-role"
  assume_role_policy = data.aws_iam_policy_document.ecs_task_assume_role.json
}
