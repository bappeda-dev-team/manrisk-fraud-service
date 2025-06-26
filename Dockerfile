# Use OpenJDK 21 as base image
FROM openjdk:21-jdk-slim

# Install netcat for database waiting and curl for health checks
RUN apt-get update && apt-get install -y netcat-openbsd curl && rm -rf /var/lib/apt/lists/*

# Set working directory
WORKDIR /app

# Create a non-root user
RUN groupadd -r appuser && useradd -r -g appuser appuser

# Copy the JAR file
COPY build/libs/*.jar app.jar

# Change ownership to the app user
RUN chown -R appuser:appuser /app

# Switch to the non-root user
USER appuser

# Expose port 8080
EXPOSE 8080

# Add health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]