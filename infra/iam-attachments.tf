# ECS execution role attachments

# api-gateway exec attachments
resource "aws_iam_role_policy_attachment" "attach_api_gateway_exec_execution_policy" {
  role       = aws_iam_role.ecs_task_exec_api_gateway.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy"
}

resource "aws_iam_role_policy_attachment" "attach_api_gateway_exec_secrets" {
  role       = aws_iam_role.ecs_task_exec_api_gateway.name
  policy_arn = aws_iam_policy.api_gateway_exec_secrets.arn
}

# auth-service exec attachments
resource "aws_iam_role_policy_attachment" "attach_auth_service_exec_execution_policy" {
  role       = aws_iam_role.ecs_task_exec_auth_service.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy"
}

resource "aws_iam_role_policy_attachment" "attach_auth_service_exec_secrets" {
  role       = aws_iam_role.ecs_task_exec_auth_service.name
  policy_arn = aws_iam_policy.auth_service_exec_secrets.arn
}

# url-service exec attachments
resource "aws_iam_role_policy_attachment" "attach_url_service_exec_execution_policy" {
  role       = aws_iam_role.ecs_task_exec_url_service.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy"
}

resource "aws_iam_role_policy_attachment" "attach_url_service_exec_secrets" {
  role       = aws_iam_role.ecs_task_exec_url_service.name
  policy_arn = aws_iam_policy.url_service_exec_secrets.arn
}

# redirector-service exec attachments
resource "aws_iam_role_policy_attachment" "attach_redirector_service_exec_execution_policy" {
  role       = aws_iam_role.ecs_task_exec_redirector_service.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy"
}

resource "aws_iam_role_policy_attachment" "attach_redirector_service_exec_secrets" {
  role       = aws_iam_role.ecs_task_exec_redirector_service.name
  policy_arn = aws_iam_policy.redirector_service_exec_secrets.arn
}

# analytics-service exec attachments
resource "aws_iam_role_policy_attachment" "attach_analytics_service_exec_execution_policy" {
  role       = aws_iam_role.ecs_task_exec_analytics_service.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy"
}
resource "aws_iam_role_policy_attachment" "attach_analytics_service_exec_secrets" {
  role       = aws_iam_role.ecs_task_exec_analytics_service.name
  policy_arn = aws_iam_policy.analytics_service_exec_secrets.arn
}


# ECS task role attachments

resource "aws_iam_role_policy_attachment" "attach_api_gateway_ssm" {
  role       = aws_iam_role.ecs_task_api_gateway.name
  policy_arn = aws_iam_policy.ssm.arn
}

resource "aws_iam_role_policy_attachment" "attach_url_service_msk" {
  role       = aws_iam_role.ecs_task_url_service.name
  policy_arn = aws_iam_policy.msk.arn
}

resource "aws_iam_role_policy_attachment" "attach_redirector_service_msk" {
  role       = aws_iam_role.ecs_task_redirector_service.name
  policy_arn = aws_iam_policy.msk.arn
}

resource "aws_iam_role_policy_attachment" "attach_analytics_service_msk" {
  role       = aws_iam_role.ecs_task_analytics_service.name
  policy_arn = aws_iam_policy.msk.arn
}
