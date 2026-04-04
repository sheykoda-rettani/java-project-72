plugins {
    id("com.github.ben-manes.versions") version "0.53.0"
    id("application")
    id("checkstyle")
    id("org.sonarqube") version "7.1.0.6387"
    id("jacoco")
    id("com.github.johnrengelman.shadow") version "8.1.1"
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

    implementation("io.javalin:javalin:6.6.0")
    implementation("io.javalin:javalin-bundle:6.6.0")
    implementation("io.javalin:javalin-rendering:6.6.0")

    implementation("org.slf4j:slf4j-api:2.0.10")
    runtimeOnly("ch.qos.logback:logback-classic:1.5.32")

    runtimeOnly("com.h2database:h2:2.3.232")
    runtimeOnly("org.postgresql:postgresql:42.7.9")
    implementation("com.zaxxer:HikariCP:5.1.0")

    implementation("org.jdbi:jdbi3-core:3.11.0")
    implementation("org.jdbi:jdbi3-sqlobject:3.11.0")

    implementation("gg.jte:jte:3.2.2")
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