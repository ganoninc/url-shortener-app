data "aws_iam_policy_document" "api_gateway_exec_secrets" {
  statement {
    effect  = "Allow"
    actions = ["secretsmanager:GetSecretValue"]
    resources = [
      aws_secretsmanager_secret.jwt.arn,
    ]
  }
}

resource "aws_iam_policy" "api_gateway_exec_secrets" {
  name   = "${var.project}-${var.env}-api-gateway-exec-secrets"
  policy = data.aws_iam_policy_document.api_gateway_exec_secrets.json
  tags = {
    component = "iam"
    service   = "api-gateway"
  }
}

data "aws_iam_policy_document" "auth_service_exec_secrets" {
  statement {
    effect  = "Allow"
    actions = ["secretsmanager:GetSecretValue"]
    resources = [
      aws_secretsmanager_secret.jwt.arn,
      aws_secretsmanager_secret.google_oauth.arn,
      aws_secretsmanager_secret.db.arn
    ]
  }
}

resource "aws_iam_policy" "auth_service_exec_secrets" {
  name   = "${var.project}-${var.env}-auth-service-exec-secrets"
  policy = data.aws_iam_policy_document.auth_service_exec_secrets.json
  tags = {
    component = "iam"
    service   = "auth-service"
  }
}

data "aws_iam_policy_document" "url_service_exec_secrets" {
  statement {
    effect  = "Allow"
    actions = ["secretsmanager:GetSecretValue"]
    resources = [
      aws_secretsmanager_secret.db.arn
    ]
  }
}

resource "aws_iam_policy" "url_service_exec_secrets" {
  name   = "${var.project}-${var.env}-url-service-exec-secrets"
  policy = data.aws_iam_policy_document.url_service_exec_secrets.json
  tags = {
    component = "iam"
    service   = "url-service"
  }
}

data "aws_iam_policy_document" "redirector_service_exec_secrets" {
  statement {
    effect  = "Allow"
    actions = ["secretsmanager:GetSecretValue"]
    resources = [
      aws_secretsmanager_secret.db.arn
    ]
  }
}

resource "aws_iam_policy" "redirector_service_exec_secrets" {
  name   = "${var.project}-${var.env}-redirector-service-exec-secrets"
  policy = data.aws_iam_policy_document.redirector_service_exec_secrets.json
  tags = {
    component = "iam"
    service   = "redirector-service"
  }
}

data "aws_iam_policy_document" "analytics_service_exec_secrets" {
  statement {
    effect  = "Allow"
    actions = ["secretsmanager:GetSecretValue"]
    resources = [
      aws_secretsmanager_secret.db.arn
    ]
  }
}

resource "aws_iam_policy" "analytics_service_exec_secrets" {
  name   = "${var.project}-${var.env}-analytics-service-exec-secrets"
  policy = data.aws_iam_policy_document.analytics_service_exec_secrets.json
  tags = {
    component = "iam"
    service   = "analytics-service"
  }
}

data "aws_iam_policy_document" "ecs_exec_ssm" {
  statement {
    effect = "Allow"
    actions = [
      "ssmmessages:CreateControlChannel",
      "ssmmessages:CreateDataChannel",
      "ssmmessages:OpenControlChannel",
      "ssmmessages:OpenDataChannel"
    ]
    resources = ["*"]
  }
}
resource "aws_iam_policy" "ssm" {
  name   = "${var.project}-${var.env}-policy-ssm"
  policy = data.aws_iam_policy_document.ecs_exec_ssm.json
  tags = {
    component = "ecs"
  }
}

data "aws_iam_policy_document" "msk" {
  statement {
    effect = "Allow"
    actions = [
      "kafka-cluster:Connect",
      "kafka-cluster:DescribeCluster"
    ]
    # Cluster ARN format: arn:aws:kafka:region:account:cluster/name/uuid
    resources = [aws_msk_serverless_cluster.kafka.arn]
  }

  statement {
    effect = "Allow"
    actions = [
      "kafka-cluster:DescribeTopic",
      "kafka-cluster:CreateTopic",
      "kafka-cluster:ReadData",
      "kafka-cluster:WriteData"
    ]
    # Topic ARN format: arn:aws:kafka:region:account:topic/cluster-name/cluster-uuid/topic-name
    # Using /* at the end allows access to all topics in this cluster
    resources = ["${replace(aws_msk_serverless_cluster.kafka.arn, "cluster", "topic")}/*"]
  }

  statement {
    effect = "Allow"
    actions = [
      "kafka-cluster:DescribeGroup",
      "kafka-cluster:AlterGroup"
    ]
    # Group ARN format: arn:aws:kafka:region:account:group/cluster-name/cluster-uuid/group-id
    resources = ["${replace(aws_msk_serverless_cluster.kafka.arn, "cluster", "group")}/*"]
  }
}
resource "aws_iam_policy" "msk" {
  name   = "${var.project}-${var.env}-policy-msk"
  policy = data.aws_iam_policy_document.msk.json
  tags = {
    component = "msk"
  }
}
