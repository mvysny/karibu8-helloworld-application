import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.gradle.api.tasks.testing.logging.TestExceptionFormat

plugins {
    kotlin("jvm") version "1.3.10"
    // need to use Gretty here because of https://github.com/johndevs/gradle-vaadin-plugin/issues/317
    id("org.gretty") version "2.2.0"
    id("com.devsoap.plugin.vaadin") version "1.4.1"
}

defaultTasks("clean", "build")

repositories {
    mavenCentral()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

vaadin {
    version = "8.6.0"
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
    compile("com.github.mvysny.karibudsl:karibu-dsl-v8:0.5.1")

    // include proper kotlin version
    compile(kotlin("stdlib-jdk8"))

    // logging
    // currently we are logging through the SLF4J API to LogBack. See src/main/resources/logback.xml file for the logger configuration
    compile("ch.qos.logback:logback-classic:1.2.3")
    // this will allow us to configure Vaadin to log to SLF4J
    compile("org.slf4j:jul-to-slf4j:1.7.25")

    // test support
    testCompile("com.github.mvysny.kaributesting:karibu-testing-v8:1.0.0")
    testCompile("com.github.mvysny.dynatest:dynatest-engine:0.12")

    // workaround until https://youtrack.jetbrains.com/issue/IDEA-178071 is fixed
    compile("com.vaadin:vaadin-themes:${vaadin.version}")
    compile("com.vaadin:vaadin-client-compiled:${vaadin.version}")

    // heroku app runner
    staging("com.github.jsimone:webapp-runner:9.0.11.0")
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
