# 1단계: 빌드 환경 (Gradle Wrapper 사용)
FROM eclipse-temurin:17-jdk-alpine AS build

# 작업 디렉토리 설정
WORKDIR /app

# Gradle Wrapper 관련 파일, 빌드 스크립트 및 소스 복사
COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./
COPY src src

# gradlew 실행 권한 부여
RUN chmod +x gradlew

# Gradle 빌드 실행 (테스트 제외)
RUN ./gradlew clean build -x test --no-daemon

# 2단계: 실행 환경 (JRE)
FROM eclipse-temurin:17-jre-alpine

# 작업 디렉토리 설정
WORKDIR /app

# 빌드된 JAR 파일 복사
COPY --from=build /app/build/libs/record-*-SNAPSHOT.jar app.jar

# 8080 포트 노출
EXPOSE 8080

# 애플리케이션 실행 시 포트 지정
ENTRYPOINT ["java", "-jar", "app.jar"]