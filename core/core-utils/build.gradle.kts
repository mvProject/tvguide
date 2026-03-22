plugins {
    alias(libs.plugins.mvproject.android.library)
    alias(libs.plugins.kotlinx.serialization.plugin)
}

android {
    namespace = "com.mvproject.tvprogramguide.core.utils"
}

dependencies {
    implementation(project(":core:core-models"))
    implementation(libs.kotlinx.datetime)
    implementation(libs.bundles.ksoup)
    implementation(libs.timber)
    implementation(libs.androidx.core)
}
