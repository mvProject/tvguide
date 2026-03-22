plugins {
    alias(libs.plugins.mvproject.android.library.compose)
    alias(libs.plugins.kotlinx.serialization.plugin)
}

android {
    namespace = "com.mvproject.tvprogramguide.feature.settings.app"
}

dependencies {
    implementation(project(":core:core-models"))
    implementation(project(":core:core-domain"))
    implementation(project(":core:core-ui"))
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.androidx.compose.navigation)
    implementation(platform(libs.koin.bom))
    implementation(libs.bundles.koin)
}
