plugins {
    alias(libs.plugins.mvproject.android.library)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.mvproject.tvprogramguide.core.database"

    defaultConfig {
        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
        }
    }
}

dependencies {
    implementation(project(":core:core-models"))
    implementation(libs.bundles.room)
    ksp(libs.room.compiler)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.datetime)
}
