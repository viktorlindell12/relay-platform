# ---- Build ----
FROM eclipse-temurin:25-jdk AS build
WORKDIR /workspace

RUN apt-get update && apt-get install -y --no-install-recommends maven \
    && rm -rf /var/lib/apt/lists/*

# Copy all pom.xml files so Maven can validate the full reactor, then cache dependencies.
# Sibling module source is not needed — BFF has no inter-module dependencies.
COPY pom.xml .
COPY relay-auth-service/pom.xml relay-auth-service/pom.xml
COPY relay-user-service/pom.xml relay-user-service/pom.xml
COPY relay-message-service/pom.xml relay-message-service/pom.xml
COPY relay-bot-service/pom.xml relay-bot-service/pom.xml
COPY relay-proto/pom.xml relay-proto/pom.xml
COPY relay-bff-service/pom.xml relay-bff-service/pom.xml
RUN mvn dependency:go-offline -pl relay-bff-service -am -q --no-transfer-progress

COPY relay-bff-service/src relay-bff-service/src
RUN mvn package -pl relay-bff-service -am -DskipTests -q --no-transfer-progress

# ---- Runtime ----
FROM eclipse-temurin:25-jre AS runtime

RUN apt-get update && apt-get install -y --no-install-recommends curl \
    && rm -rf /var/lib/apt/lists/* \
    && groupadd --system relay && useradd --system --gid relay relay

WORKDIR /app
COPY --from=build /workspace/relay-bff-service/target/app.jar app.jar

USER relay
EXPOSE 8080

HEALTHCHECK --interval=10s --timeout=5s --retries=5 --start-period=30s \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]