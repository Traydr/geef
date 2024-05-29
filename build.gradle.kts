import org.codehaus.plexus.util.Os

val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val exposed_version: String by project
val hikari_version: String by project
val kotlinx_version: String by project
val koin_version: String by project
val jsoup_version: String by project
val openmateo_version: String by project

plugins {
    kotlin("jvm") version "1.9.23"
    id("au.id.wale.tailwind") version "0.2.0"
    id("io.ktor.plugin") version "2.3.10"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.23"
}

group = "dev.traydr.vu"
version = "0.0.1"

tailwind {
    version = "3.4.1"
    configPath = "src/main/resources"
    input = "src/main/resources/tailwind/input.css"
    output = "src/main/resources/css/styles.css"
}

application {
    mainClass.set("dev.traydr.ktor.ApplicationKt")

    val isDevelopment: Boolean = providers.environmentVariable("DEV").isPresent
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

ktor {
    fatJar {
        archiveFileName.set("fat.jar")
    }
}

tasks.register<Exec>("npmInstall") {
    workingDir = File("src/main/resources")
    commandLine("npm", "install")
}

// Very verbose way to specify order
tasks.tailwindDownload {
    if (Os.isFamily(Os.FAMILY_UNIX)) {
        dependsOn(tasks.getByName("npmInstall"))
    }
}

tasks.tailwindInit {
    dependsOn(tasks.tailwindDownload)
}

tasks.tailwindCompile {
    dependsOn(tasks.tailwindInit)
}

tasks.build {
    dependsOn(tasks.tailwindCompile)
}

repositories {
    maven(url = "https://jitpack.io")
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-sessions-jvm")
    implementation("io.ktor:ktor-server-host-common-jvm")
    implementation("io.ktor:ktor-server-status-pages-jvm")
    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm")
    implementation("io.ktor:ktor-server-html-builder-jvm")
    implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:$kotlinx_version")
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation("com.h2database:h2:2.1.214")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.insert-koin:koin-ktor:$koin_version")
    implementation("io.ktor:ktor-server-auth:$ktor_version")
    implementation("com.zaxxer:HikariCP:$hikari_version")
    implementation("at.favre.lib:bcrypt:0.10.2")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposed_version")
    implementation("org.postgresql:postgresql:42.7.2")
    implementation("org.jsoup:jsoup:$jsoup_version")
    implementation("com.open-meteo:open-meteo-api-kotlin:$openmateo_version")
    testImplementation("io.ktor:ktor-server-tests-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}
