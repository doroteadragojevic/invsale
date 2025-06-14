# 1. stage: build
FROM maven:3.8.6-eclipse-temurin-17 AS build

WORKDIR /app

# Kopiraj sve fajlove u container
COPY . .

# Pokreni Maven build i generiraj JAR
RUN mvn clean package -DskipTests

# 2. stage: finalni image
FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

# Kopiraj JAR iz build stagea
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
