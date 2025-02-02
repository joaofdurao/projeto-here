FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} phereapi.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/phereapi.jar"]