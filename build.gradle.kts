plugins {
    val kotlinVersion = "1.4.21"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion
    id("com.github.johnrengelman.shadow") version "6.1.0"
}

group = "dev.hikari"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
}

tasks.withType<org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
        allWarningsAsErrors = true
        this.freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
    }
}

tasks.withType<Jar> {
    manifest {
        attributes(
            mapOf(
                "Main-Class" to "dev.hikari.Main"
            )
        )
    }
    from("./") {
        include("build.gradle.kts")
    }
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("net.mamoe:mirai-core:2.0-RC")
    implementation("com.charleskorn.kaml:kaml:0.26.0")
}
