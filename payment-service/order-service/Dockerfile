FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /build
COPY pom.xml .
RUN mvn -B dependency:go-offline
COPY src ./src
RUN mvn -B package -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /app

ARG OTEL_AGENT_VERSION=2.5.0
ADD https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v${OTEL_AGENT_VERSION}/opentelemetry-javaagent.jar /app/opentelemetry-javaagent.jar

COPY --from=build /build/target/*.jar /app/app.jar

ENTRYPOINT ["sh", "-c", "java -javaagent:/app/opentelemetry-javaagent.jar -jar /app/app.jar"]
