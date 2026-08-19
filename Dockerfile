# ===== BUILD STAGE =====
FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /build

# Cacheo de dependencias
COPY pom.xml .
RUN mvn -B -q -DskipTests dependency:go-offline

# Código fuente
COPY src ./src

# Build del jar
RUN mvn -B -q clean package -DskipTests


# ===== RUNTIME STAGE =====
FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

# 🔥 Instalar curl de forma más robusta (evita fallos de red en CI)
RUN apt-get update \
    && apt-get install -y --no-install-recommends curl --fix-missing \
    && rm -rf /var/lib/apt/lists/*

# Crear usuario no root antes de copiar (mejor capa)
RUN useradd -ms /bin/bash appuser

# Copiar jar
COPY --from=builder /build/target/app.jar app.jar

# Permisos (evita problemas raros)
RUN chown appuser:appuser /app/app.jar

USER appuser

EXPOSE 8080

# Healthcheck
HEALTHCHECK --interval=30s --timeout=5s --retries=3 --start-period=20s \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java","-jar","app.jar"]