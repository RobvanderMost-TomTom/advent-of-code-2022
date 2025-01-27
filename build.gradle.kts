plugins {
    kotlin("jvm") version "2.1.10"
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
