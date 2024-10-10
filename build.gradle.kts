plugins {
    kotlin("jvm") version "2.0.21"
}

repositories {
    mavenCentral()
}
dependencies {
    implementation("org.json:json:20220924")
}

tasks {
    sourceSets {
        main {
            java.srcDirs("src")
        }
    }

    wrapper {
        gradleVersion = "7.6"
    }
}
