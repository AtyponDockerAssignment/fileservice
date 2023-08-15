FROM debian:bullseye-slim as wait

RUN apt-get update && apt-get install -y --no-install-recommends wait-for-it && rm -rf /var/lib/apt/lists/*

# Use the official gradle image as the base image
FROM gradle:jdk17 as build
WORKDIR /app
# Copy the entire project
COPY --chown=gradle:gradle . /app
# Build the JAR
RUN gradle bootJar --no-daemon
RUN ls -al /app/build/libs/

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=wait /usr/bin/wait-for-it /usr/bin/wait-for-it
# Copy the built jar file from the build image
COPY --from=build /app/build/libs/*.jar /app/my-app.jar
EXPOSE 8080
ENTRYPOINT ["wait-for-it", "global-db:3306","--","java", "-jar", "/app/my-app.jar"]
