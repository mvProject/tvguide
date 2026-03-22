plugins {
    alias(libs.plugins.mvproject.android.library)
}

android {
    namespace = "com.mvproject.tvprogramguide.core.domain"
}

dependencies {
    implementation(project(":core:core-models"))
    implementation(project(":core:core-utils"))
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.datetime)
}
