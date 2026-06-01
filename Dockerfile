FROM eclipse-temurin:17-jre
WORKDIR /app
COPY target/*.jar bot.jar
EXPOSE 8080
CMD ["java", "-jar", "bot.jar"]