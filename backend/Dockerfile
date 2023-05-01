FROM adoptopenjdk/openjdk11 AS builder
# build 하기 위한 gradle 파일 복사
COPY gradlew .
COPY build.gradle .
COPY settings.gradle .
COPY gradle gradle
# src 코드 복사
COPY src src
# Windows의 개행 문자(\r\n)를 Linux 개행 문자(\n)로 변경
RUN apt-get update && apt-get install -y dos2unix
RUN dos2unix ./gradlew
# gradle build and test
RUN chmod +x ./gradlew
RUN ./gradlew build

FROM adoptopenjdk/openjdk11
# 위의 builder 과정에서 생성한 build 파일을 app.jar 파일로 복사합니다.
COPY --from=builder build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]