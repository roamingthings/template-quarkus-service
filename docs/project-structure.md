# Project Structure

## Overview

Two independent Gradle builds connected via composite build:

- **my-service/** - Quarkus application
- **my-service-st/** - API client library and system tests

The root `settings.gradle.kts` uses `includeBuild` to wire them together, allowing `my-service-st` to depend on
`my-service` artifacts without a monolithic multi-project build.

## Directory Structure

```
project-root/
├── build-logic/                                      # CONVENTION PLUGINS
│   ├── build.gradle.kts
│   ├── settings.gradle.kts
│   └── src/main/kotlin/
│       └── nullability-conventions.gradle.kts
├── gradle/
│   └── libs.versions.toml                          # Shared version catalog
├── settings.gradle.kts                              # Composite build (includeBuild)
├── my-service/                                      # APPLICATION MODULE
│   ├── build.gradle.kts
│   ├── settings.gradle.kts
│   └── src/
│       ├── main/
│       │   ├── docker/                              # Dockerfiles (jvm, native)
│       │   ├── resources/application.properties
│       │   └── java/de/roamingthings/myservice/
│       │       ├── greetings/
│       │       │   ├── boundary/GreetingResource.java
│       │       │   └── control/Greeter.java
│       │       └── health/
│       │           └── boundary/
│       │               ├── ApplicationLiveness.java
│       │               └── ApplicationReadiness.java
│       └── test/java/de/roamingthings/
└── my-service-st/                                   # SYSTEM TESTS & API CLIENT
    ├── build.gradle.kts
    ├── settings.gradle.kts
    └── src/
        ├── main/java/de/roamingthings/myservice/    # REST client interfaces (JAR)
        │   ├── greetings/boundary/GreetingsResourceClient.java
        │   └── health/boundary/HealthClient.java
        └── test/java/de/roamingthings/myservice/    # System tests
            └── greetings/boundary/
                ├── GreetingsResourceIT.java
                └── HealthResourceIT.java
```

## `my-service/` - Application Module

Single Quarkus project with JAX-RS resources following BCE/ECB architecture.

**Package structure:** `de.roamingthings.myservice.[component].[boundary|control|entity]`

**Key dependencies:**

- Quarkus REST (Jakarta REST)
- MicroProfile Health
- MicroProfile OpenTelemetry

## `my-service-st/` - System Tests & API Client

Dual-purpose module:

- **`src/main/java`** - MicroProfile REST Client interfaces mirroring the service's JAX-RS resources. Packaged as a JAR
  for consumer applications to use as an API client library.
- **`src/test/java`** - System tests that run against a live service instance using those client interfaces.

Client interfaces use `@RegisterRestClient` with the project's shared configKey and follow the same BCE package structure
as the service module.

**Key dependencies:**

- Quarkus REST Client
- MicroProfile REST Client
- JUnit 5, AssertJ

## `build-logic/` - Convention Plugins

Shared Gradle convention plugins applied by all modules. Contains:

- **`nullability-conventions`** - Configures Error Prone with NullAway to enforce JSpecify nullability annotations at
  compile time. All Error Prone checks are disabled except NullAway, which runs in JSpecify mode with `@NullMarked`
  scope enforcement.

## Key Decisions

1. **Composite build over multi-project:** Each module has its own `build.gradle.kts` and `settings.gradle.kts`,
   connected via `includeBuild` in the root
2. **Shared version catalog:** `gradle/libs.versions.toml` ensures consistent dependency versions
3. **Convention plugins via `build-logic/`:** Centralized build configuration (e.g., nullability enforcement) applied
   consistently across all modules
4. **Dual-purpose `-st` module:** Avoids a separate module for API client interfaces while keeping system tests isolated
5. **BCE architecture:** Business components organized by responsibility, not technical layer

## Build Commands

```bash
# Build application
cd my-service && ../gradlew clean build

# Dev mode
cd my-service && ../gradlew quarkusDev

# Run system tests
cd my-service-st && ../gradlew clean test
```
