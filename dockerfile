# execute mvn package para buildar a aplicação antes de criar a imagem
FROM openjdk:11.0.12-jre-slim

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} app.jar

ENTRYPOINT [ "java", "-jar", "/app.jar" ]
EXPOSE 8080