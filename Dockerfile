FROM openjdk:22-ea-21-slim-bullseye
LABEL authors="LABBE"
RUN addgroup spring
RUN adduser --ingroup spring spring
USER spring:spring
EXPOSE 82
ARG FICHIER_JAR="target/zoo-front-1.1.jar"
COPY ${FICHIER_JAR} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]