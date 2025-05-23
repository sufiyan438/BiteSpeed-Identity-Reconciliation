FROM openjdk:23-jdk-slim
WORKDIR /app
# COPY /libs/*.jar app.jar
COPY build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]