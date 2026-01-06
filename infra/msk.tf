resource "aws_msk_serverless_cluster" "kafka" {
  client_authentication {
    sasl {
      iam {
        enabled = true
      }
    }
  }
  cluster_name = "${var.project}-${var.env}-kafka"

  vpc_config {
    subnet_ids         = module.vpc.private_subnets
    security_group_ids = [aws_security_group.msk.id]
  }

  tags = {
    component = "msk"
  }
}
