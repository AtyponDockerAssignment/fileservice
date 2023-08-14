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
ENTRYPOINT ["java", "-jar", "/app/my-app.jar"]