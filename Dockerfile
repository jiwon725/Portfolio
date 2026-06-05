# 1. 빌드 단계 (Build Stage)
FROM eclipse-temurin:17-jdk-focal AS build
WORKDIR /app
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src
RUN chmod +x ./mvnw
RUN ./mvnw clean package -DskipTests

# 2. 실행 단계 (Run Stage)
FROM eclipse-temurin:17-jre-focal
WORKDIR /app
COPY --from=build /app/target/portfolio-0.0.1-SNAPSHOT.jar app.jar

# 포트 개방
EXPOSE 8080

# 실행 명령어
ENTRYPOINT ["java", "-jar", "app.jar"]
