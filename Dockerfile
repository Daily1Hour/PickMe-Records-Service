# 단계 1: 빌드 단계
FROM openjdk:17-jdk-slim AS build

# 빌드 의존성 설치 (필요 시)
RUN apt-get update && \
    apt-get install -y curl unzip && \
    rm -rf /var/lib/apt/lists/*

# 작업 디렉토리 설정
WORKDIR /app

# Gradle Wrapper를 사용하여 빌드할 경우, 필요한 파일 복사
COPY gradlew .
COPY gradle gradle

# 소스 코드 및 설정 파일 복사
COPY build.gradle settings.gradle ./
COPY src src

# 실행 권한 부여 (필요 시)
RUN chmod +x ./gradlew

# 애플리케이션 빌드
RUN ./gradlew build -x test

# 단계 2: 실행 단계
FROM openjdk:17-jdk-slim

# 작업 디렉토리 설정
WORKDIR /app

# 빌드 단계에서 생성된 JAR 파일을 복사
COPY --from=build /app/build/libs/record-0.0.1-SNAPSHOT.jar app.jar

# 포트 노출
EXPOSE 8080

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar", "--server.port=8080"]