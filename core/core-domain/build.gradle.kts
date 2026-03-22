plugins {
    alias(libs.plugins.mvproject.android.library)
    alias(libs.plugins.kotlinx.serialization.plugin)
}

android {
    namespace = "com.mvproject.tvprogramguide.core.domain"
}

dependencies {
    implementation(project(":core:core-models"))
    implementation(project(":core:core-database"))
    implementation(project(":core:core-network"))
    implementation(project(":core:core-datastore"))
    implementation(project(":core:core-utils"))
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.bundles.room)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.bundles.workManager)
    implementation(platform(libs.koin.bom))
    implementation(libs.bundles.koin)
    implementation(platform(libs.firebase.bom))
    implementation(libs.bundles.firebase)
    implementation(libs.bundles.credentials)
    implementation(libs.startUp)
    implementation(libs.timber)
    implementation(libs.androidx.core)
    implementation(libs.kotlinx.datetime)
}
