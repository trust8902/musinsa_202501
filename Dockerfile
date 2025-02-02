# 빌드 스테이지
FROM openjdk:21-jdk-slim AS build
WORKDIR /app
COPY . /app
RUN chmod +x gradlew && ./gradlew clean build
RUN ls -l /app
RUN ls -l /app/build
RUN ls -l /app/build/libs/

# 실행 스테이지
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /app/build/libs/*.jar /app/shop.jar
RUN chmod +x /app/shop.jar
CMD ["java", "-jar", "/app/shop.jar"]
