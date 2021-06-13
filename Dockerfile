FROM openjdk:8-jdk-alpine
ARG JAR_FILE=target/shoping-cart*.jar
COPY ${JAR_FILE} application.jar
ENTRYPOINT ["java", "-jar", "application.jar"]