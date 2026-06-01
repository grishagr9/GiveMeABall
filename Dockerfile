FROM eclipse-temurin:17-jre
WORKDIR /app
# Копируем JAR внутрь образа
COPY target/bot-0.0.1-SNAPSHOT.jar /app/bot.jar
EXPOSE 8080
CMD ["java", "-jar", "/app/bot.jar"]