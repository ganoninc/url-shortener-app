#!/usr/bin/env bash
set -euo pipefail

SERVICE="${1:-}"
ENV="${ENV:-prod}"
AWS_REGION="${AWS_REGION:-eu-west-3}"
PROJECT="url-shortener"

if [[ -z "$SERVICE" ]]; then
  echo "Usage: ./scripts/build.sh <service>"
  exit 1
fi

SERVICE_DIR="backend/$SERVICE"

if [[ ! -d "$SERVICE_DIR" ]]; then
  echo "Unknown service: $SERVICE"
  exit 1
fi

ACCOUNT_ID="$(aws sts get-caller-identity --query Account --output text)"
ECR_URI="${ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com/${PROJECT}-${ENV}-${SERVICE}"

GIT_SHA="$(git rev-parse --short HEAD)"

echo "🔐 ECR login"
aws ecr get-login-password --region "$AWS_REGION" \
| docker login --username AWS --password-stdin \
  "${ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com"

echo "🐳 Building $SERVICE ($GIT_SHA)"

docker buildx build \
  --platform linux/amd64 \
  -t "${ECR_URI}:${GIT_SHA}" \
  --push \
  "$SERVICE_DIR"

echo "✅ Pushed:"
echo "  - ${ECR_URI}:${GIT_SHA}" 