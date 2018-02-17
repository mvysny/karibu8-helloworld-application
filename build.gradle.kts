import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.junit.platform:junit-platform-gradle-plugin:1.0.3")
    }
}

apply {
    plugin("org.junit.platform.gradle.plugin")
}

plugins {
    kotlin("jvm") version "1.2.21"
    // need to use Gretty here because of https://github.com/johndevs/gradle-vaadin-plugin/issues/317
    id("org.akhikhl.gretty") version "2.0.0"
    id("com.devsoap.plugin.vaadin") version "1.3.1"
}

defaultTasks("clean", "build")

repositories {
    mavenCentral()
    jcenter()
    maven("https://dl.bintray.com/mvysny/github")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

vaadin {
    version = "8.3.0"
}

gretty {
    contextPath = "/"
}

dependencies {
    // Karibu-DSL dependency
    compile("com.github.vok.karibudsl:karibu-dsl-v8:0.2.22")

    // include proper kotlin version
    compile(kotlin("stdlib-jdk8"))

    // logging
    // currently we are logging through the SLF4J API to LogBack. See src/main/resources/logback.xml file for the logger configuration
    compile("ch.qos.logback:logback-classic:1.2.3")
    // this will allow us to configure Vaadin to log to SLF4J
    compile("org.slf4j:jul-to-slf4j:1.7.25")

    // test support
    testCompile("com.github.vok.karibudsl:karibu-testing-v8:0.2.22")
    testCompile("com.github.mvysny.dynatest:dynatest:0.2")

    // workaround until https://youtrack.jetbrains.com/issue/IDEA-178071 is fixed
    compile("com.vaadin:vaadin-themes:${vaadin.version}")
    compile("com.vaadin:vaadin-client-compiled:${vaadin.version}")
}
