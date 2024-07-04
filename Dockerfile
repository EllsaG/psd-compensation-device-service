#temp container to build using gradle

FROM gradle:8.8-jdk17 AS BUILD
ENV APP_HOME=/usr/app/
WORKDIR $APP_HOME
COPY build.gradle.kts settings.gradle.kts $APP_HOME

COPY gradle $APP_HOME/gradle
COPY --chown=gradle:gradle . /home/gradle/src
USER root
RUN chown -R gradle /home/gradle/src

RUN gradle build || return 0
COPY . .
RUN gradle clean build

# actual container
FROM openjdk:latest
ENV JAR_NAME=psd-compensation-device-service-0.0.1-SNAPSHOT.jar
ENV APP_HOME=/usr/app/
WORKDIR $APP_HOME
COPY --from=BUILD $APP_HOME/build/libs/$JAR_NAME .
EXPOSE 8077
ENTRYPOINT exec java -jar ${JAR_NAME}
