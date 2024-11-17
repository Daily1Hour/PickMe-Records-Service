# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Install necessary packages and tools
RUN apt-get update && apt-get install -y \
    curl \
    unzip \
    git \
    && rm -rf /var/lib/apt/lists/*

# Install Gradle
ENV GRADLE_VERSION=7.6
RUN mkdir -p /opt/gradle && \
    curl -fsSL https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip -o gradle.zip && \
    unzip gradle.zip -d /opt/gradle && \
    rm gradle.zip && \
    ln -s /opt/gradle/gradle-${GRADLE_VERSION}/bin/gradle /usr/local/bin/gradle

# Set the working directory inside the container
WORKDIR /workspace

# Copy the build.gradle and settings.gradle (if exists) to cache dependencies
COPY build.gradle settings.gradle* ./

# Download dependencies (without building the project)
RUN gradle build -x test --no-daemon || true

# Copy the rest of the application source code
COPY . .

# Expose port 8080
EXPOSE 8080

# Enable Spring Boot Devtools for live reload
ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS \
    JAVA_OPTS="-Dspring.devtools.restart.enabled=true"

# Command to run the application in development mode
CMD ["gradle", "bootRun"]