output "vpc_id" {
  value = module.vpc.vpc_id
}

output "private_subnets" {
  value = module.vpc.private_subnets
}

output "public_subnets" {
  value = module.vpc.public_subnets
}

output "rds_endpoint" {
  value = aws_db_instance.postgres.address
}

output "rds_port" {
  value = aws_db_instance.postgres.port
}

output "msk_cluster_arn" {
  value = aws_msk_serverless_cluster.kafka.arn
}

output "ecs_cluster_name" {
  value = aws_ecs_cluster.main.name
}

output "erc_api_gateway_repo" {
  value = aws_ecr_repository.api_gateway.repository_url
}

output "erc_auth_service_repo" {
  value = aws_ecr_repository.auth_service.repository_url
}

output "alb_dns_name" {
  value = module.alb.dns_name
}
