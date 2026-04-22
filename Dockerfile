# Usa uma imagem leve com Java 8 já instalado
FROM eclipse-temurin:8-jre

# Define o diretório de trabalho dentro do container
WORKDIR /app

# Copia automaticamente o JAR mais recente gerado pelo Maven
COPY target/outsera-api-*.jar app.jar

# Expõe a porta da aplicação
EXPOSE 8081

# Comando que sobe sua API quando o container iniciar
ENTRYPOINT ["java", "-jar", "app.jar"]