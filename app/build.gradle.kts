plugins {
    id("com.github.ben-manes.versions") version "0.53.0"
    id("application")
    id("checkstyle")
    id("org.sonarqube") version "7.1.0.6387"
    id("jacoco")
}

group = "hexlet.code"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

application {
    mainClass = "hexlet.code.App"
}

sonar {
    properties {
        property("sonar.projectKey", "sheykoda-rettani_java-project-72")
        property("sonar.organization", "sheykoda-rettani")
    }
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    implementation("io.javalin:javalin:7.1.0")
    implementation("org.slf4j:slf4j-api:2.0.10")
    runtimeOnly("ch.qos.logback:logback-classic:1.5.32")
}

tasks.jacocoTestReport {
    reports {
        xml.required = true
        csv.required = false
        html.outputLocation = layout.buildDirectory.dir("jacocoHtml")
    }
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}