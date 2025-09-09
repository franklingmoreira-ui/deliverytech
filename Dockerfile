# ESTÁGIO 1: Build (O Construtor)
# Usamos uma imagem completa do Maven com JDK 21 para compilar o projeto.
# Esta imagem é pesada, mas será descartada no final.
FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src

# ✅✅✅ LINHA ADICIONADA PARA RESOLVER O ERRO ✅✅✅
# Copia a pasta .git para que o plugin do Maven possa ler as informações do commit.
COPY .git ./.git

# Compila o projeto e empacota em um arquivo .jar
RUN mvn clean install -DskipTests


# ESTÁGIO 2: Final (A Imagem de Produção)
# Usamos uma imagem JRE (Java Runtime Environment) super leve baseada em Alpine.
# Ela contém apenas o necessário para EXECUTAR a aplicação, não para compilá-la.
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

