# Use the official OpenJDK image as a parent image
FROM openjdk:11-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the packaged Spring Boot application JAR file into the container
COPY target/comingsoon.jar /app/comingsoon.jar

# Run the JAR file
CMD ["java", "-jar", "comingsoon.jar"]
