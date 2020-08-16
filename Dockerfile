FROM maven:3-jdk-8 as BUILD
ARG PROFILE=aws
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src src
RUN mvn package -DskipTests -P $PROFILE

FROM openjdk:8-jdk-alpine
COPY --from=BUILD /app/target/client-info-0.0.1-SNAPSHOT.jar /app/app.jar
ENTRYPOINT ["java","-jar","/app/app.jar"]
