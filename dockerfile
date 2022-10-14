FROM openjdk:17-alpine
RUN addgroup -S convergencia && adduser -S convergencia -G convergencia
RUN mkdir -p /logs
RUN chown convergencia /logs
VOLUME /logs
USER convergencia:convergencia
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
