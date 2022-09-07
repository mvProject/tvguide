plugins {
    id("org.jlleitschuh.gradle.ktlint").version("10.3.0")
    id("com.github.ben-manes.versions").version("0.42.0")
}

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:${libs.versions.androidGradle.get()}")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${libs.versions.kotlin.get()}")
        classpath("com.google.dagger:hilt-android-gradle-plugin:${libs.versions.daggerHilt.get()}")
        classpath("org.jlleitschuh.gradle:ktlint-gradle:${libs.versions.ktLint.get()}")
        classpath("com.google.gms:google-services:${libs.versions.googleServices.get()}")
        classpath("com.google.firebase:firebase-crashlytics-gradle:${libs.versions.crashlyticsGradle.get()}")
    }
}

ktlint {
    version.set("0.45.2")
    android.set(true)
    filter {
        exclude("**/build/**")
        exclude("**/resources/**")
        exclude("**/generated/**")
    }
    reporters {
        reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.PLAIN)
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
    jvmArgs = mutableListOf("--enable-preview")
}

tasks.withType<org.jlleitschuh.gradle.ktlint.tasks.KtLintFormatTask> {
    setSource(files(rootDir))
}

tasks.withType<org.jlleitschuh.gradle.ktlint.tasks.BaseKtLintCheckTask> {
    setSource(files(rootDir))
}
