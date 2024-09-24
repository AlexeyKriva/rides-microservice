FROM maven:3.8.5-openjdk-17

WORKDIR /rides

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean
RUN mvn package -DskipTests

FROM openjdk:17-jdk

COPY /target/rides-microservice-0.0.1-SNAPSHOT.jar /rides/launch-rides.jar

ENTRYPOINT ["java","-jar","/rides/launch-rides.jar"]

EXPOSE 8083