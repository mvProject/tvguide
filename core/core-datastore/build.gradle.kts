plugins {
    alias(libs.plugins.mvproject.android.library)
    alias(libs.plugins.kotlinx.serialization.plugin)
}

android {
    namespace = "com.mvproject.tvprogramguide.core.datastore"
}

dependencies {
    implementation(project(":core:core-models"))
    implementation(libs.dataStore)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.datetime)
}
