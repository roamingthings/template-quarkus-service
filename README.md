# Java 25 Quarkus Service Template

Template for building traditional Quarkus REST services using Java 25 with BCE/ECB architecture.

## Requirements

- Java 25 (use [SDKMAN](https://sdkman.io/): `sdk install java 25-open`)

## Modules

Two independent Gradle builds connected via composite build (`includeBuild` in root `settings.gradle.kts`):

- [**my-service/**](my-service/) - Quarkus application with JAX-RS resources and BCE architecture
- **my-service-st/** - System tests and reusable MicroProfile REST Client interfaces (dual-purpose: `src/main/java`
  provides API client JAR for consumers, `src/test/java` contains system tests)

See [docs/project-structure.md](docs/project-structure.md) for architecture details.

## Build

```bash
cd my-service && ../gradlew clean build
```

## Dev Mode

```bash
cd my-service && ../gradlew quarkusDev
```

Dev UI available at <http://localhost:8080/q/dev/>.

## System Tests

```bash
cd my-service-st && ../gradlew clean test
```

Configure the service base URI via `BASE_URI` environment variable or `application.properties`.

## Using This Template with Coding Agents

Fork this repository and use a coding agent to customize the template.

### Setup Prompts

**1. Rename the service:**

```
Rename "my-service" to "inventory-service". Update all module names, package names,
and references accordingly.
```

**2. Add a JAX-RS resource:**

```
Add a ProductsResource that handles CRUD operations for a Product entity with
fields: id (String), name (String), price (BigDecimal). Follow BCE architecture
and create a corresponding REST client interface in the -st module.
```

**3. Add business logic:**

```
Implement validation for Product: price must be positive, name must not be empty.
Use a sealed result type with Success and ValidationError cases.
```

## Development Guidelines

This project follows coding conventions defined in [AGENTS.md](AGENTS.md). Key principles:

- BCE/ECB architecture pattern
- Java 25 modern syntax (var, records, pattern matching)
- Package-private visibility by default
- System.Logger for logging
- KISS and YAGNI
