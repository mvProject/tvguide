plugins {
    id("org.jlleitschuh.gradle.ktlint").version(Versions.ktLint)
    id("com.github.ben-manes.versions").version(Versions.depUpdate)
}
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:${Versions.gradle}")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.navigation}")
        classpath("com.google.dagger:hilt-android-gradle-plugin:${Versions.daggerHilt}")
        classpath("org.jlleitschuh.gradle:ktlint-gradle:${Versions.ktLint}")
    }
}

ktlint {
    version.set("0.45.1")
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

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

tasks.withType<org.jlleitschuh.gradle.ktlint.tasks.KtLintFormatTask> {
    setSource(files(rootDir))
}

tasks.withType<org.jlleitschuh.gradle.ktlint.tasks.BaseKtLintCheckTask> {
    setSource(files(rootDir))
}