plugins {
    kotlin("jvm") version "1.5.0"
}

group = "dev.siro256.spigotpl"
version = "1.0.0"

repositories {
    maven { url = uri("https://maven.siro256.dev/repository/maven-public/") }
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.google.code.gson:gson:2.8.7")
    implementation("org.spigotmc:spigot-api:1.12.2-R0.1-SNAPSHOT")
}

configurations.all {
    resolutionStrategy.cacheChangingModulesFor(0, TimeUnit.SECONDS)
}

tasks {
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }

    withType<ProcessResources> {
        filteringCharset = "UTF-8"
        from(projectDir) { include("LICENSE") }
    }

    withType<Jar> {
        from(configurations.implementation.get().apply { isCanBeResolved = true }.filterNot { it.name.endsWith(".pom") }.map { if (it.isDirectory) it else zipTree(it) })
    }
}
