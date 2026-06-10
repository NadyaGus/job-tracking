FROM dockerhub.timeweb.cloud/eclipse-temurin:17-jdk-noble AS builder
WORKDIR /app
COPY . .
RUN apt-get update && apt-get install -y dos2unix && \
    dos2unix gradlew && chmod +x gradlew

RUN ./gradlew build --no-daemon -x test

FROM dockerhub.timeweb.cloud/eclipse-temurin:17-jre-noble
WORKDIR /app

# Копируем скомпилированный jar и запускаем его напрямую
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
