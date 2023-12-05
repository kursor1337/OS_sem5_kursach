pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}

dependencyResolutionManagement {

    versionCatalogs {
        create("libs") {
            library("datetime", "org.jetbrains.kotlinx", "kotlinx-datetime").version("0.5.0")
            library("coroutines", "org.jetbrains.kotlinx", "kotlinx-coroutines-core").version("1.8.0-RC")
            library("clikt", "com.github.ajalt.clikt", "clikt").version("4.2.1")
        }
    }
}

rootProject.name = "OS_sem5_kursach"