# Koristi službeni OpenJDK 17 image kao osnovu
FROM eclipse-temurin:17-jdk-jammy

# Postavi radni direktorij u containeru
WORKDIR /app

# Kopiraj buildani JAR iz target foldera u container i preimenuj u app.jar
COPY target/*.jar app.jar

# Otvori port 8080
EXPOSE 8080

# Pokreni aplikaciju
ENTRYPOINT ["java", "-jar", "app.jar"]
