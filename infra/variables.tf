variable "aws_region" {
  type    = string
  default = "eu-west-3"
}

variable "aws_azs" {
  type    = list(string)
  default = ["eu-west-3a", "eu-west-3b", "eu-west-3c"]
}

variable "project" {
  type    = string
  default = "url-shortener"
}

variable "env" {
  type    = string
  default = "prod"
}

variable "vpc_cidr" {
  type    = string
  default = "10.0.0.0/16"
}

variable "private_subnets" {
  type    = list(string)
  default = ["10.0.1.0/24", "10.0.2.0/24", "10.0.3.0/24"]
}

variable "public_subnets" {
  type    = list(string)
  default = ["10.0.10.0/24", "10.0.11.0/24", "10.0.12.0/24"]
}

variable "pg_username" {
  type    = string
  default = "pgadmin"
}

variable "pg_db_name" {
  type    = string
  default = "urlshortener"
}

# variable "cors_allowed_origin" {
#   type = string
#   # default = "[\"http://localhost:5173\", \"http://localhost\"]"
# }

variable "jwt_expiration_ms" {
  type = string
}

variable "jwt_refresh_token_expiration_ms" {
  type = string
}

variable "auth_service_host" {
  type = string
}

variable "auth_service_port" {
  type = number
}

variable "url_service_host" {
  type = string
}

variable "url_service_port" {
  type = number
}

variable "redirector_service_host" {
  type = string
}

variable "redirector_service_port" {
  type = number
}

variable "analytics_service_host" {
  type = string
}

variable "analytics_service_port" {
  type = number
}

variable "google_client_id" {
  type      = string
  sensitive = true
}

variable "google_client_secret" {
  type      = string
  sensitive = true
}
