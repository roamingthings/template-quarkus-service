# my-service

Main application module. Quarkus REST service with BCE/ECB architecture.

## Dev Mode

```bash
../gradlew quarkusDev
```

Dev UI available at <http://localhost:8080/q/dev/>.

## Build

```bash
../gradlew clean build
```

## Uber-JAR

```bash
../gradlew build -Dquarkus.package.jar.type=uber-jar
```

Run with `java -jar build/*-runner.jar`.

## Docker

Build the application first, then build and run the container image:

```bash
../gradlew build
docker build -f src/main/docker/Dockerfile.jvm -t my-service .
docker run -i --rm -p 8080:8080 my-service
```
