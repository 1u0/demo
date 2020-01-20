FROM openjdk:8-jre

RUN groupadd -r appserv && useradd -r -s /bin/false -g appserv appserv
RUN mkdir /app
WORKDIR /app

EXPOSE 8080

COPY target/appointments-facade-*.jar /app/appointments-facade.jar
RUN chown -R appserv:appserv /app
USER appserv
CMD java $(java-dynamic-memory-opts) -jar appointments-facade.jar
