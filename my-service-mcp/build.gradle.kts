plugins {
    java
    id("io.quarkus")
    id("nullability-conventions")
}

repositories {
    mavenCentral()
    mavenLocal()
}

val quarkusPlatformGroupId: String by project
val quarkusPlatformArtifactId: String by project
val quarkusPlatformVersion: String by project

dependencies {
    implementation(enforcedPlatform("${quarkusPlatformGroupId}:${quarkusPlatformArtifactId}:${quarkusPlatformVersion}"))
    implementation("io.quarkus:quarkus-arc")
    implementation("io.quarkiverse.mcp:quarkus-mcp-server-http:1.11.0")
    implementation("io.quarkus:quarkus-smallrye-health")
    implementation(libs.jspecify)
    testImplementation("io.quarkus:quarkus-junit")
    testImplementation("io.quarkiverse.mcp:quarkus-mcp-server-test:1.11.0")
    testImplementation(libs.assertj.core)
}

group = "de.roamingthings"
version = "1.0.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_25
    targetCompatibility = JavaVersion.VERSION_25
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-parameters")
}

tasks.test {
    exclude("**/*IT.class")
}

val integrationTest by tasks.registering(Test::class) {
    description = "Runs integration tests."
    group = "verification"
    val testSourceSet = sourceSets["test"]
    testClassesDirs = testSourceSet.output.classesDirs
    classpath = testSourceSet.runtimeClasspath
    include("**/*IT.class")
    useJUnitPlatform()
    dependsOn("quarkusBuild")
}
