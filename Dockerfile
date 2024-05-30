FROM openjdk:17-jdk-slim
VOLUME /tmp
COPY build/libs/your-application.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]