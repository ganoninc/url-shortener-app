# DB/RDS secrets
resource "random_password" "db_password" {
  length  = 32
  special = false
}

resource "aws_secretsmanager_secret" "db" {
  name = "${var.project}-${var.env}-db"
  tags = {
    component = "rds"
  }
}

resource "aws_secretsmanager_secret_version" "db" {
  secret_id = aws_secretsmanager_secret.db.id

  secret_string = jsonencode({
    username = var.pg_username
    password = random_password.db_password.result
    host     = aws_db_instance.postgres.address
    dbname   = var.pg_db_name
  })
}

resource "random_password" "jwt_secret" {
  length           = 64
  special          = true
  override_special = "!#$%&*()-_=+[]{}<>:?"
}

resource "aws_secretsmanager_secret" "jwt" {
  name = "${var.project}-${var.env}-jwt"
  tags = {
    component = "security"
    scope     = "jwt-signing"
    shared    = "true"
  }
}

resource "aws_secretsmanager_secret_version" "jwt" {
  secret_id = aws_secretsmanager_secret.jwt.id
  secret_string = jsonencode({
    jwt_secret = random_password.jwt_secret.result
  })
}

resource "aws_secretsmanager_secret" "google_oauth" {
  name = "${var.project}-${var.env}-google-oauth"
  tags = {
    component = "security"
    scope     = "oauth"
    provider  = "google"
    shared    = "true"
  }
}

resource "aws_secretsmanager_secret_version" "google_oauth" {
  secret_id = aws_secretsmanager_secret.google_oauth.id

  secret_string = jsonencode({
    google_client_id     = var.google_client_id
    google_client_secret = var.google_client_secret
  })
}
