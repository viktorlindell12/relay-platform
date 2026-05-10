# ---- Build ----
FROM eclipse-temurin:25-jdk AS build
WORKDIR /workspace

RUN apt-get update && apt-get install -y --no-install-recommends maven \
    && rm -rf /var/lib/apt/lists/*

# Copy poms separately so dependency layer is cached between source changes
COPY pom.xml .
COPY relay-auth-service/pom.xml relay-auth-service/pom.xml
RUN mvn dependency:go-offline -pl relay-auth-service -am -q --no-transfer-progress

COPY relay-auth-service/src relay-auth-service/src
RUN mvn package -pl relay-auth-service -am -DskipTests -q --no-transfer-progress

# ---- Runtime ----
FROM eclipse-temurin:25-jre AS runtime

RUN apt-get update && apt-get install -y --no-install-recommends curl \
    && rm -rf /var/lib/apt/lists/* \
    && groupadd --system relay && useradd --system --gid relay relay

WORKDIR /app
COPY --from=build /workspace/relay-auth-service/target/app.jar app.jar

USER relay
EXPOSE 8081

HEALTHCHECK --interval=10s --timeout=5s --retries=5 --start-period=30s \
    CMD curl -f http://localhost:8081/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]