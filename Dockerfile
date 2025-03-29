FROM openjdk:17-jdk-slim-buster
WORKDIR /app
COPY target/SpringSecurity-0.0.1-SNAPSHOT.jar /app/SpringSecurity.jar
ENTRYPOINT ["java","-jar", "SpringSecurity.jar"]