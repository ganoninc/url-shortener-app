resource "aws_ecr_repository" "api_gateway" {
  name                 = "${var.project}-${var.env}-api-gateway"
  image_tag_mutability = "IMMUTABLE"

  image_scanning_configuration {
    scan_on_push = true
  }

  tags = {
    service   = "api-gateway"
    component = "ecr"
  }
}

resource "aws_ecr_lifecycle_policy" "api_gateway_policy" {
  repository = aws_ecr_repository.api_gateway.name
  policy = jsonencode({
    rules = [
      {
        rulePriority = 1,
        description  = "Expire untagged images after 7 days",
        selection = {
          tagStatus   = "untagged",
          countType   = "sinceImagePushed",
          countUnit   = "days",
          countNumber = 7
        },
        action = {
          type = "expire"
        }
      }
    ]
  })
}


resource "aws_ecr_repository" "auth_service" {
  name                 = "${var.project}-${var.env}-auth-service"
  image_tag_mutability = "IMMUTABLE"

  image_scanning_configuration {
    scan_on_push = true
  }

  tags = {
    service   = "auth-service"
    component = "ecr"
  }
}

resource "aws_ecr_lifecycle_policy" "auth_service_policy" {
  repository = aws_ecr_repository.auth_service.name
  policy = jsonencode({
    rules = [
      {
        rulePriority = 1,
        description  = "Expire untagged images after 7 days",
        selection = {
          tagStatus   = "untagged",
          countType   = "sinceImagePushed",
          countUnit   = "days",
          countNumber = 7
        },
        action = {
          type = "expire"
        }
      }
    ]
  })
}


resource "aws_ecr_repository" "url_service" {
  name                 = "${var.project}-${var.env}-url-service"
  image_tag_mutability = "IMMUTABLE"

  image_scanning_configuration {
    scan_on_push = true
  }

  tags = {
    service   = "url-service"
    component = "ecr"
  }
}

resource "aws_ecr_lifecycle_policy" "url_service_policy" {
  repository = aws_ecr_repository.url_service.name
  policy = jsonencode({
    rules = [
      {
        rulePriority = 1,
        description  = "Expire untagged images after 7 days",
        selection = {
          tagStatus   = "untagged",
          countType   = "sinceImagePushed",
          countUnit   = "days",
          countNumber = 7
        },
        action = {
          type = "expire"
        }
      }
    ]
  })
}


resource "aws_ecr_repository" "redirector_service" {
  name                 = "${var.project}-${var.env}-redirector-service"
  image_tag_mutability = "IMMUTABLE"

  image_scanning_configuration {
    scan_on_push = true
  }

  tags = {
    service   = "redirector-service"
    component = "ecr"
  }
}

resource "aws_ecr_lifecycle_policy" "redirector_service_policy" {
  repository = aws_ecr_repository.redirector_service.name
  policy = jsonencode({
    rules = [
      {
        rulePriority = 1,
        description  = "Expire untagged images after 7 days",
        selection = {
          tagStatus   = "untagged",
          countType   = "sinceImagePushed",
          countUnit   = "days",
          countNumber = 7
        },
        action = {
          type = "expire"
        }
      }
    ]
  })
}


resource "aws_ecr_repository" "analytics_service" {
  name                 = "${var.project}-${var.env}-analytics-service"
  image_tag_mutability = "IMMUTABLE"

  image_scanning_configuration {
    scan_on_push = true
  }

  tags = {
    service   = "analytics-service"
    component = "ecr"
  }
}

resource "aws_ecr_lifecycle_policy" "analytics_service_policy" {
  repository = aws_ecr_repository.analytics_service.name
  policy = jsonencode({
    rules = [
      {
        rulePriority = 1,
        description  = "Expire untagged images after 7 days",
        selection = {
          tagStatus   = "untagged",
          countType   = "sinceImagePushed",
          countUnit   = "days",
          countNumber = 7
        },
        action = {
          type = "expire"
        }
      }
    ]
  })
}
