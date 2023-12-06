@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("org.jlleitschuh.gradle.ktlint").version("10.3.0")
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.kotlin) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.kotlin.parcelize) apply false
    alias(libs.plugins.firebase.crashlitycs) apply false
    alias(libs.plugins.gms.googleServices) apply false
    alias(libs.plugins.versions) apply false

}

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("org.jlleitschuh.gradle:ktlint-gradle:${libs.versions.ktLint.get()}")
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
