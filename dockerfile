FROM maven:3.9.6-eclipse-temurin-21-alpine as build
WORKDIR /BasicWebServer

COPY ./pom.xml ./pom.xml

# Download dependencies
RUN mvn -B -f pom.xml dependency:resolve

COPY ./src ./src
# Build without running tests
RUN mvn -B -f pom.xml clean package -DskipTests=true

###########################################################
# RUN UNIT TESTS
FROM build as tests

RUN mvn -B verify

###########################################################
FROM openjdk:22-ea-21-slim-bullseye as execution_stage
WORKDIR /BasicWebServer
RUN addgroup appgroup --gid 1000 && useradd --uid 1000 -g appgroup appuser
RUN apt update -y && apt upgrade -y
RUN apt install curl -y
COPY --from=build /BasicWebServer/target/BasicWebServer-latest-package.jar /BasicWebServer/basicWebServer.jar
RUN chown -R appuser:appgroup /BasicWebServer
EXPOSE 80
USER 1000:1000
CMD ["java", "-cp", "./basicWebServer.jar", "org.dreds20.Main"]