FROM openjdk:17-jdk-alpine

COPY target/library-0.0.1-SNAPSHOT.jar library-0.0.1.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/library-0.0.1.jar"]