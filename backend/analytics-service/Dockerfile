FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY target/analytics-service.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]