// Gradle plugins, which extend Gradle's capabilities:
// - The `kotlin` plugin lets Gradle compile Kotlin code.
// - The `application` plugin tells gradle that it should be possible to use the `run` command to
//   start the compiled application.
plugins {
    kotlin("jvm") version "1.8.20"
    application
}

// Set the Java Virtual Machine version that the compiler should target.
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

// Configure the `application` plugin by telling it where the main class is. The `main()` method
// from this class is what will be started by the `run` Gradle command.
application {
    mainClass.set("darkness.libsvg.Entry")
}

// The location of our source code and of the resources that will be compiled into the application.
sourceSets.main {
    java.srcDir("src/kotlin")
    resources.srcDir("src/resources")
}

// Where to download .jar files for the dependencies from.
repositories {
    mavenCentral()
}

// The external libraries that we depend on.
dependencies {
    implementation("org.apache.xmlgraphics:batik-all:1.11")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.4")
}
