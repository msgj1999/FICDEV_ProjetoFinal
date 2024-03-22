FROM openjdk:17-jdk-slim

COPY target/*.jar projetofinalficdev.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","projetofinalficdev.jar"]