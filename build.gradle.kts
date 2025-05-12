plugins {
    java
    idea
}

group = "org.pushing-pixels.radiance.demo"
version = "1.0.0"

java.sourceCompatibility = JavaVersion.VERSION_11
java.targetCompatibility = JavaVersion.VERSION_11

buildscript {
    repositories {
        mavenLocal()
        mavenCentral()
        maven("https://plugins.gradle.org/m2/")
        maven("https://central.sonatype.com/repository/maven-snapshots/")
    }

    dependencies {
        classpath(libs.radiance.gradlePlugin)
        classpath(libs.versionchecker.gradlePlugin)
    }
}

repositories {
    mavenLocal()
    mavenCentral()
    maven { url = uri("https://central.sonatype.com/repository/maven-snapshots/") }
}

dependencies {
    implementation(libs.radiance.common)
}

tasks.register<org.pushingpixels.radiance.tools.svgtranscoder.gradle.TranscodeDeepTask>("transcodeDeep") {
    inputRootDirectory = file("src/main/resources")
    outputRootDirectory = file("src/gen/java/org/radiance/demo/svg")
    outputRootPackageName = "org.radiance.demo.svg"
    outputLanguage = "java"
    transcode()
}

tasks.withType<JavaCompile> {
    dependsOn("transcodeDeep")
}

java {
    sourceSets {
        java {
            sourceSets["main"].apply {
                java.srcDir("$rootDir/src/main/java")
                java.srcDir("$rootDir/src/gen/java")
            }
        }
    }
}

idea {
    module {
        generatedSourceDirs.add(file("$rootDir/src/gen/java"))
    }
}

// To generate report about available dependency updates, run
// ./gradlew dependencyUpdates
apply(plugin = "com.github.ben-manes.versions")
