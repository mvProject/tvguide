plugins {
    alias(libs.plugins.mvproject.android.library.compose)
}

android {
    namespace = "com.mvproject.tvprogramguide.core.ui"
}

dependencies {
    implementation(project(":core:core-models"))
    implementation(project(":core:core-utils"))
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.datetime)
    implementation(libs.timber)
}
