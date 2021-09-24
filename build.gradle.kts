import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.gradle.api.tasks.testing.logging.TestExceptionFormat

plugins {
    kotlin("jvm") version "1.5.31"
    // need to use Gretty here because of https://github.com/johndevs/gradle-vaadin-plugin/issues/317
    id("org.gretty") version "3.0.6"
    id("com.devsoap.plugin.vaadin") version "2.0.0.beta2"
}

defaultTasks("clean", "build")

repositories {
    mavenCentral()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

vaadin {
    version = "8.14.0"
}

gretty {
    contextPath = "/"
    servletContainer = "jetty9.4"
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        // to see the exceptions of failed tests in Travis-CI console.
        exceptionFormat = TestExceptionFormat.FULL
    }
}

val staging by configurations.creating

dependencies {
    // don't use api/implementation: com.devsoap.plugin.vaadin will then cause them not to be packaged in the WAR archive!
    // Karibu-DSL dependency
    compile("com.github.mvysny.karibudsl:karibu-dsl-v8:1.0.7")

    // include proper kotlin version
    compile(kotlin("stdlib-jdk8"))

    // logging
    // currently we are logging through the SLF4J API to SLF4J-Simple. See src/main/resources/simplelogger.properties file for the logger configuration
    compile("org.slf4j:slf4j-simple:1.7.32")
    // this will allow us to configure Vaadin to log to SLF4J
    compile("org.slf4j:jul-to-slf4j:1.7.32")

    // test support
    testImplementation("com.github.mvysny.kaributesting:karibu-testing-v8:1.3.3")
    testImplementation("com.github.mvysny.dynatest:dynatest-engine:0.20")

    // workaround until https://youtrack.jetbrains.com/issue/IDEA-178071 is fixed
    compile("com.vaadin:vaadin-themes:${vaadin.version}")
    compile("com.vaadin:vaadin-client-compiled:${vaadin.version}")

    // heroku app runner
    staging("com.heroku:webapp-runner-main:9.0.41.0")
}

// Heroku
tasks {
    val copyToLib by registering(Copy::class) {
        into("$buildDir/server")
        from(staging) {
            include("webapp-runner*")
        }
    }
    val stage by registering {
        dependsOn("build", copyToLib)
    }
}
