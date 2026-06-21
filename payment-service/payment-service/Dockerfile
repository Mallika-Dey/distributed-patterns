FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /build
COPY pom.xml .
RUN mvn -B dependency:go-offline
COPY src ./src
RUN mvn -B package -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /app

# Download the OpenTelemetry Java agent. Replaces Elastic APM agent.
# Auto-instruments Spring Boot, HTTP clients, JDBC, Logback, etc.
ARG OTEL_AGENT_VERSION=2.5.0
ADD https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v${OTEL_AGENT_VERSION}/opentelemetry-javaagent.jar /app/opentelemetry-javaagent.jar

COPY --from=build /build/target/*.jar /app/app.jar
EXPOSE 8082

# All OTel config comes via env vars from docker-compose
ENTRYPOINT ["sh", "-c", "java -javaagent:/app/opentelemetry-javaagent.jar -jar /app/app.jar"]
