## Json schema validation demo

This is a sample application using [JSON Schema](https://json-schema.org/) for validation of 3rd party (rest) api and
also for testing the api provided by the service itself.

Implementation tech stack: Java 8, Spring Boot.

### Usage

You may need to change the configuration for provider api url (`providers.example.url`) in `application.properties` file.

Run `mvn spring-boot:run` to run the service locally (for development).

To build a docker image, run:
```shell
mvn clean package
docker build .
```
