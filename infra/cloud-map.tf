resource "aws_service_discovery_private_dns_namespace" "main" {
  name        = local.cloudmap_namespace
  description = "Service discovery used by the api-gateway service"
  vpc         = module.vpc.vpc_id
}

resource "aws_service_discovery_service" "auth_service" {
  name = "auth-service"

  dns_config {
    namespace_id = aws_service_discovery_private_dns_namespace.main.id

    dns_records {
      ttl  = 10
      type = "A"
    }

    routing_policy = "MULTIVALUE"
  }

  lifecycle {
    create_before_destroy = true
  }
}

resource "aws_service_discovery_service" "url_service" {
  name = "url-service"

  dns_config {
    namespace_id = aws_service_discovery_private_dns_namespace.main.id

    dns_records {
      ttl  = 10
      type = "A"
    }

    routing_policy = "MULTIVALUE"
  }

  lifecycle {
    create_before_destroy = true
  }
}

resource "aws_service_discovery_service" "redirector_service" {
  name = "redirector-service"

  dns_config {
    namespace_id = aws_service_discovery_private_dns_namespace.main.id

    dns_records {
      ttl  = 10
      type = "A"
    }

    routing_policy = "MULTIVALUE"
  }

  lifecycle {
    create_before_destroy = true
  }
}

resource "aws_service_discovery_service" "analytics_service" {
  name = "analytics-service"

  dns_config {
    namespace_id = aws_service_discovery_private_dns_namespace.main.id

    dns_records {
      ttl  = 10
      type = "A"
    }

    routing_policy = "MULTIVALUE"
  }

  lifecycle {
    create_before_destroy = true
  }
}
