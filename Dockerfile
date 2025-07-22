 # Use an OpenJDK base image
FROM openjdk:17-jdk-slim

# Set working directory inside the container
WORKDIR /app

# Copy your jar file into the container
COPY target/whatsapp-1.0.0.jar app.jar

# Expose the port your app runs on
EXPOSE 8080

# Command to run your app
ENTRYPOINT ["java", "-jar", "app.jar"]