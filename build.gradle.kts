import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.gradle.api.tasks.testing.logging.TestExceptionFormat

plugins {
    kotlin("jvm") version "1.3.72"
    // need to use Gretty here because of https://github.com/johndevs/gradle-vaadin-plugin/issues/317
    id("org.gretty") version "3.0.1"
    id("com.devsoap.plugin.vaadin") version "2.0.0.beta2"
}

defaultTasks("clean", "build")

repositories {
    jcenter()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

vaadin {
    version = "8.10.3"
}

gretty {
    contextPath = "/"
    servletContainer = "jetty9.4"
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
    // Karibu-DSL dependency
    api("com.github.mvysny.karibudsl:karibu-dsl-v8:1.0.0")

    // include proper kotlin version
    api(kotlin("stdlib-jdk8"))

    // logging
    // currently we are logging through the SLF4J API to SLF4J-Simple. See src/main/resources/simplelogger.properties file for the logger configuration
    implementation("org.slf4j:slf4j-simple:1.7.30")
    // this will allow us to configure Vaadin to log to SLF4J
    implementation("org.slf4j:jul-to-slf4j:1.7.30")

    // test support
    testImplementation("com.github.mvysny.kaributesting:karibu-testing-v8:1.1.24")
    testImplementation("com.github.mvysny.dynatest:dynatest-engine:0.16")

    // workaround until https://youtrack.jetbrains.com/issue/IDEA-178071 is fixed
    compile("com.vaadin:vaadin-themes:${vaadin.version}")
    compile("com.vaadin:vaadin-client-compiled:${vaadin.version}")

    // heroku app runner
    staging("com.heroku:webapp-runner-main:9.0.31.0")
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
