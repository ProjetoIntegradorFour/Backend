FROM openjdk:17-jdk-slim
WORKDIR /app

# Copy Maven descriptor first (helps Docker caching)
COPY pom.xml .
COPY src ./src

# Build the application
RUN apt-get update && apt-get install -y maven
RUN mvn -e -B clean package -DskipTests

# Run the application
EXPOSE 8080
CMD ["java", "-jar", "target/*.jar"]