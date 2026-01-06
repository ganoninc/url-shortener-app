resource "aws_db_subnet_group" "default" {
  name       = "${var.project}-${var.env}-db-subnet-group"
  subnet_ids = module.vpc.private_subnets
  tags = {
    component = "rds"
  }
}

resource "aws_db_instance" "postgres" {
  identifier     = "${var.project}-${var.env}-postgres"
  engine         = "postgres"
  engine_version = "15"
  instance_class = "db.t3.micro"

  allocated_storage = 20
  db_name           = var.pg_db_name
  username          = var.pg_username
  password          = random_password.db_password.result

  publicly_accessible    = false
  skip_final_snapshot    = true
  db_subnet_group_name   = aws_db_subnet_group.default.name
  vpc_security_group_ids = [aws_security_group.rds.id]

  performance_insights_enabled = true

  tags = {
    component = "rds"
  }
}
