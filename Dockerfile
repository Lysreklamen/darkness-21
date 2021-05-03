# Experimental Dockerfile for the sequence generator.
# Build with:
#     docker build -t lysreklamen-generator:latest .
# Run with:
#     docker run --rm -it -v ./darkness-19/generator/scripts:/app/darkness-19/generator/scripts -v ~/darkness-19/generator/output:/app/generator/output lysreklamen-generator:latest

FROM openjdk:8

RUN mkdir /app
WORKDIR /app

COPY gradle ./gradle
COPY gradlew .
RUN ./gradlew -v

COPY build.gradle .
COPY settings.gradle .
COPY generator ./generator
COPY simulator ./simulator

RUN ./gradlew generator:build

ENTRYPOINT ["./gradlew", "generator:run"]
