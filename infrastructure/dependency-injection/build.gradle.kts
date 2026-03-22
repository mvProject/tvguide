plugins {
    alias(libs.plugins.mvproject.android.library)
}

android {
    namespace = "com.mvproject.tvprogramguide.infrastructure.di"
}

dependencies {
    // All modules whose classes are wired here
    implementation(project(":core:core-models"))
    implementation(project(":core:core-domain"))
    implementation(project(":core:core-data"))
    implementation(project(":core:core-database"))
    implementation(project(":core:core-network"))
    implementation(project(":core:core-datastore"))
    implementation(project(":core:core-utils"))
    implementation(project(":platform"))

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.bundles.room)
    implementation(libs.bundles.workManager)
    implementation(libs.bundles.ktor)

    implementation(platform(libs.firebase.bom))
    implementation(libs.bundles.firebase)
    implementation(libs.bundles.credentials)

    implementation(platform(libs.koin.bom))
    implementation(libs.bundles.koin)

    implementation(libs.dataStore)
    implementation(libs.timber)
    implementation(libs.androidx.core)
}
