# Usa a imagem base do OpenJDK 17 para build
FROM eclipse-temurin:17-jdk-alpine as builder

# Define argumentos para versões
ARG JAR_FILE=target/manager_user_security-1.0.0.jar
ARG APP_NAME=manager_user_security

# Cria um diretório para a aplicação
WORKDIR /build

# Copia o arquivo JAR da aplicação
COPY ${JAR_FILE} app.jar

# Extrai o JAR para otimizar as camadas Docker
RUN mkdir -p extracted && \
    java -Djarmode=layertools -jar app.jar extract --destination extracted

# Usa imagem JRE para runtime (mais leve)
FROM eclipse-temurin:17-jre-alpine as runtime

# Cria um usuário não-root para executar a aplicação
RUN addgroup -S spring && adduser -S spring -G spring

# Cria diretórios para logs e configuração
RUN mkdir -p /app/logs && \
    mkdir -p /app/config && \
    chown -R spring:spring /app

WORKDIR /app

# Copia as camadas da fase de build
COPY --from=builder --chown=spring:spring /build/extracted/dependencies/ ./
COPY --from=builder --chown=spring:spring /build/extracted/spring-boot-loader/ ./
COPY --from=builder --chown=spring:spring /build/extracted/snapshot-dependencies/ ./
COPY --from=builder --chown=spring:spring /build/extracted/application/ ./

# Expõe a porta 8080 para a aplicação Spring Boot
EXPOSE 8080

# Configura variáveis de ambiente para a JVM e para a aplicação
ENV JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseG1GC -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"
ENV SPRING_MAIN_ALLOW_BEAN_DEFINITION_OVERRIDING="true"
# Removendo a variável que permite referências circulares
# ENV SPRING_MAIN_ALLOW_CIRCULAR_REFERENCES="true"

# Configura Health Check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD wget -q --spider http://localhost:8080/manager_user_security/actuator/health || exit 1

# Define o usuário não-root
USER spring:spring

# Executa a aplicação quando o container iniciar
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
