FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY target/Rest_Security_Api-0.0.1-SNAPSHOT-plain.jar /app
EXPOSE 8081
CMD ["java", "-jar", "Rest_Security_Api-0.0.1-SNAPSHOT-plain.jar"]