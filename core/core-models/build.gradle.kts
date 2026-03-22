plugins {
    alias(libs.plugins.mvproject.android.library.compose)
    alias(libs.plugins.kotlinx.serialization.plugin)
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "com.mvproject.tvprogramguide.core.models"
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
}
