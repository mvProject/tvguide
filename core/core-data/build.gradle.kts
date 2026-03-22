plugins {
    alias(libs.plugins.mvproject.android.library)
    alias(libs.plugins.kotlinx.serialization.plugin)
}

android {
    namespace = "com.mvproject.tvprogramguide.core.data"
}

dependencies {
    implementation(project(":core:core-models"))
    implementation(project(":core:core-domain"))
    implementation(project(":core:core-database"))
    implementation(project(":core:core-network"))
    implementation(project(":core:core-datastore"))
    implementation(project(":core:core-utils"))

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.bundles.room)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.datetime)

    // BackupRepository takes FirebaseAuth as a parameter type
    implementation(platform(libs.firebase.bom))
    implementation(libs.bundles.firebase)
    implementation(libs.bundles.credentials)

    implementation(libs.timber)
}
