# Step 1: Use an official Maven image with JDK 17 to build and bundle the app
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copy the build configuration and source code
COPY pom.xml .
COPY src ./src

# Pre-fetch dependencies to speed up subsequent image builds
RUN mvn dependency:go-offline -B

# Package the application tests without executing them during image compilation
RUN mvn test-compile

# Step 2: Use a slim runtime image containing Chrome for execution headless environment
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app

# Install stable Chrome dependencies required for headless Selenium runs
RUN apt-get update && apt-get install -y \
    wget \
    gnupg \
    curl \
    unzip \
    ca-certificates \
    libglib2.0-0 \
    libnss3 \
    libgconf-2-4 \
    libfontconfig1 \
    libx11-6 \
    libx11-xcb1 \
    libxcb1 \
    libxcomposite1 \
    libxcursor1 \
    libxdamage1 \
    libxext6 \
    libxfixes3 \
    libxi6 \
    libxrandr2 \
    libxrender1 \
    libxss1 \
    libxtst6 \
    libatk1.0-0 \
    libatk-bridge2.0-0 \
    libasound2 \
    libdbus-1-3 \
    libdrm2 \
    libgbm1 \
    libgtk-3-0 \
    fonts-liberation \
    chromium \
    && rm -rf /var/lib/apt/lists/*

# Copy compiled classes and dependencies from the build stage
COPY --from=build /app /app

# Set environment variable to target the internal Chromium binary if needed
ENV CHROME_BIN=/usr/bin/chromium

# Default command to run the TestNG tests inside the container
CMD ["mvn", "test"]
