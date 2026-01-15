FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Security: Don't run as root in AWS environment
RUN addgroup -S spring && adduser -S spring -G spring

ARG OTEL_JAVA_AGENT_VERSION=2.23.0
ADD https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v${OTEL_JAVA_AGENT_VERSION}/opentelemetry-javaagent.jar /otel/opentelemetry-javaagent.jar
RUN chown spring:spring /otel/opentelemetry-javaagent.jar

RUN apk add --no-cache curl

COPY --from=builder /app/target/auth-service.jar app.jar

USER spring:spring

# JVM Tuning for Cost Efficiency
ENTRYPOINT ["java", \
    "-XX:+UseContainerSupport", \
    "-XX:MaxRAMPercentage=75.0", \
    "-XX:+UseSerialGC", \
    "-Xss512k", \
    "-javaagent:/otel/opentelemetry-javaagent.jar", \
    "-jar", "app.jar"]