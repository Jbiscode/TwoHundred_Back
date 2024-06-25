FROM openjdk:17-jdk-slim
# 시간대 설정
ENV TZ=Asia/Seoul
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

VOLUME /tmp
COPY build/libs/bidbuy-1.0.0.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]