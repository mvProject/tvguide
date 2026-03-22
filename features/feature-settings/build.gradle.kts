plugins {
    alias(libs.plugins.mvproject.android.library.compose)
    alias(libs.plugins.kotlinx.serialization.plugin)
}

android {
    namespace = "com.mvproject.tvprogramguide.feature.settings"
}

dependencies {
    implementation(project(":core:core-models"))
    implementation(project(":core:core-ui"))
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.compose.navigation)
}
