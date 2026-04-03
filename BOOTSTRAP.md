# Bootstrapping a New Project from This Template

This document provides the complete specification for AI coding agents (Claude Code, Cursor, Windsurf, etc.)
to scaffold a new project from this template. It can also be used as a manual reference.

## Agent Instructions

### Interaction Flow

1. Read this entire file before starting.
2. Ask all questions from the "Questions to Ask" section in a single message,
   presenting the options clearly with defaults noted.
3. Wait for the user's answers before making any changes.
4. Calculate the derived values from the user's responses.
5. Show the user a confirmation summary with all values and derived values.
   Ask for explicit confirmation before proceeding.
6. Execute the **Transformation Steps** in order.
7. After all transformations, verify the build compiles.
8. Present the **Post-Setup Guidance** to the user.

### Getting the Template Files

If you are running in an empty directory (not a cloned copy of the template):

1. Clone the template into a temporary subdirectory:
   ```bash
   git clone https://github.com/roamingthings/template-quarkus-service .bootstrap-tmp
   ```
2. Move all contents (including dotfiles) into the current directory:
   ```bash
   mv .bootstrap-tmp/* .bootstrap-tmp/.* . 2>/dev/null
   rm -rf .bootstrap-tmp
   ```
3. Then proceed with the Transformation Steps below.

If you are already inside a cloned copy of the template, skip directly to
the Transformation Steps.

## Questions to Ask

Gather the following from the user before making any changes. Present all questions together in a single message.

| # | Question | Example | Default | Validation |
|---|----------|---------|---------|------------|
| 1 | **Root project name** (kebab-case, used for `rootProject.name` in the root `settings.gradle.kts`) | `acme-inventory` | Same as project name | Must be kebab-case, no underscores |
| 2 | **Project name** (kebab-case, used for service directories, resource naming) | `inventory-service` | — | Must be kebab-case, no underscores |
| 3 | **Java base package** (the application-level package for all code) | `com.acme.inventory` | — | Valid Java package segments |
| 4 | **Gradle group** (the Maven/Gradle group ID for the project) | `com.acme` | Same as base package | Valid Java package segments |

### Derived Values

Calculate these from the user's answers — do not ask for them separately.

> **IMPORTANT — Package naming:** The Java base package provided by the user IS the application package.
> Do NOT append the project name to it. For example, if the user says base package `de.roamingthings.flowers`
> and project name `flowers`, the application package is `de.roamingthings.flowers` — NOT
> `de.roamingthings.flowers.flowers`.

| Value | Derivation | Example (project: `inventory-service`, base package: `com.acme.inventory`, group: `com.acme`) |
|-------|-----------|---------|
| `APP_PACKAGE` | The Java base package as provided by the user | `com.acme.inventory` |
| `APP_PACKAGE_PATH` | `APP_PACKAGE` with dots replaced by `/` | `com/acme/inventory` |
| `GRADLE_GROUP` | The Gradle group as provided (or defaults to `APP_PACKAGE`) | `com.acme` |
| `PROJECT_NAME_NO_HYPHENS` | Project name with hyphens removed | `inventoryservice` |

**Show the user a confirmation summary** with all derived values before making any changes. Example:

```
Root project name:  acme-inventory
Project name:       inventory-service
Java base package:  com.acme.inventory
Gradle group:       com.acme
Config key:         inventoryservice

Proceed? (yes/no)
```

## Transformation Steps

Execute these steps in order. Each step lists exactly what to change.

### Step 1: Remove Template Git History

Always remove the template's git history and reinitialize:

```bash
rm -rf .git
git init
```

### Step 2: Rename Service Directories

Run all four commands — the `rm -rf` lines are required because Gradle `.gradle/` cache directories
inside `my-service/` and `my-service-st/` survive the `mv` and leave empty husks behind:

```bash
mv my-service {PROJECT_NAME}
mv my-service-st {PROJECT_NAME}-st
rm -rf my-service
rm -rf my-service-st
```

Verify afterwards that `my-service/` and `my-service-st/` no longer exist.

### Step 3: Update Root `settings.gradle.kts`

```kotlin
rootProject.name = "{ROOT_PROJECT_NAME}"

includeBuild("{PROJECT_NAME}")
includeBuild("{PROJECT_NAME}-st")
```

### Step 4: Update `{PROJECT_NAME}/settings.gradle.kts`

Change only the `rootProject.name` line:

```kotlin
rootProject.name = "{PROJECT_NAME}"
```

The rest of the file (`pluginManagement`, `dependencyResolutionManagement`) stays unchanged.

### Step 5: Update `{PROJECT_NAME}-st/settings.gradle.kts`

Change only the `rootProject.name` line:

```kotlin
rootProject.name = "{PROJECT_NAME}-st"
```

### Step 6: Update Gradle `group` in Both `build.gradle.kts`

Replace `group = "de.roamingthings"` with `group = "{GRADLE_GROUP}"` in:

- `{PROJECT_NAME}/build.gradle.kts`
- `{PROJECT_NAME}-st/build.gradle.kts`

### Step 7: Rename Java Packages

Rename all Java source directories and update package declarations and imports.

> **CRITICAL — Replacement order matters.** Always replace the most specific patterns first to avoid
> double-replacement. For example, replace `de.roamingthings.myservice` before `de.roamingthings`,
> otherwise `de.roamingthings.myservice` would become `{APP_PACKAGE}.myservice` instead of `{APP_PACKAGE}`.

> **CRITICAL — Moving files safely.** When moving files from old package directories to new ones,
> first create the new directory structure, then copy/move files, then delete ONLY the old directories
> that are now empty. Never use recursive delete (`rm -rf`) on a parent directory that contains both
> old and new paths (e.g., if old is `de/roamingthings/myservice/` and new is `de/roamingthings/flowers/`,
> do NOT `rm -rf de/roamingthings/` as it would delete the new path too).

**Source package mapping:**

| Old | New | Where |
|-----|-----|-------|
| `de/roamingthings/myservice/` | `{APP_PACKAGE_PATH}/` | main/test in both modules |
| `de/roamingthings/` | `{APP_PACKAGE_PATH}/` | `src/native-test/` only |

**In all `.java` files**, replace in this exact order:
1. `de.roamingthings.myservice` → `{APP_PACKAGE}` (most specific first)
2. `de.roamingthings` → `{APP_PACKAGE}` (native-test classes — least specific last)

**Move source files** to match new package paths in:
- `{PROJECT_NAME}/src/main/java/`
- `{PROJECT_NAME}/src/test/java/`
- `{PROJECT_NAME}/src/native-test/java/`
- `{PROJECT_NAME}-st/src/main/java/`
- `{PROJECT_NAME}-st/src/test/java/`

After moving, verify new directories are correct, then delete only the empty old package directories.

### Step 8: Update System Test Config Keys

The system test module uses a REST client config key `"myservice"` that must be renamed:

- In `{PROJECT_NAME}-st/src/main/java/.../GreetingsResourceClient.java`:
  change `@RegisterRestClient(configKey = "myservice")` to `configKey = "{PROJECT_NAME_NO_HYPHENS}"`
- In `{PROJECT_NAME}-st/src/main/java/.../HealthClient.java`:
  change `@RegisterRestClient(configKey = "myservice")` to `configKey = "{PROJECT_NAME_NO_HYPHENS}"`
- In `{PROJECT_NAME}-st/src/main/resources/application.properties`:
  change `quarkus.rest-client.myservice.url` to `quarkus.rest-client.{PROJECT_NAME_NO_HYPHENS}.url`
- In `{PROJECT_NAME}-st/src/test/java/.../GreetingsResourceIT.java`:
  change `@ConfigProperty(name = "quarkus.rest-client.myservice.url")` to match the new config key
- In `{PROJECT_NAME}-st/src/test/java/.../HealthResourceIT.java`:
  change `@ConfigProperty(name = "quarkus.rest-client.myservice.url")` to match the new config key

### Step 9: Update CI/CD Workflows

In `.github/workflows/build_test.yml`, update all `working-directory` fields:

- Replace `./my-service` with `./{PROJECT_NAME}`
- Replace `./my-service-st` with `./{PROJECT_NAME}-st`

### Step 10: Update README.md

- Replace title with project name
- Update module directory names (`my-service` → `{PROJECT_NAME}`, `my-service-st` → `{PROJECT_NAME}-st`)
- Update build commands to use new directory names
- Remove the "Using This Template" section (no longer a template)
- Remove reference to `BOOTSTRAP.md`

### Step 11: Update `docs/project-structure.md`

Replace all references to the old names:
- `my-service` → `{PROJECT_NAME}`
- `my-service-st` → `{PROJECT_NAME}-st`
- `de/roamingthings/myservice` → `{APP_PACKAGE_PATH}`
- `de.roamingthings.myservice` → `{APP_PACKAGE}`
- `de.roamingthings` → `{GRADLE_GROUP}` (in package path examples)

Or delete the file if it no longer accurately describes the project structure.

### Step 12: Clean Up Template Files

- Delete `BOOTSTRAP.md` (this file)
- Delete the `.claude/` directory if present (contains template-specific settings)
- Rewrite `CLAUDE.md` to contain only:
  ```
  @AGENTS.md
  ```

### Step 13: Create Initial Commit and Verify Build

```bash
git add .
git commit -m "feat: initialize {PROJECT_NAME} from template"
```

Run the build to verify everything compiles:

```bash
cd {PROJECT_NAME}
../gradlew clean build
```

If the build fails, fix any remaining package or import issues before continuing.

## Post-Setup Guidance

After bootstrapping, suggest the user:

1. Rename resource classes and business components to match their domain
   (e.g., `GreetingResource` → `ProductsResource`, `Greeter` → product-related control logic)
2. Update business logic in the control layer for their use case
3. Add corresponding REST client interfaces in the `-st` module for new resources
