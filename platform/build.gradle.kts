plugins {
    alias(libs.plugins.mvproject.android.library)
}

android {
    namespace = "com.mvproject.tvprogramguide.platform"
}

dependencies {
    implementation(project(":core:core-domain"))
    implementation(project(":core:core-models"))
    implementation(project(":core:core-utils"))

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.bundles.workManager)
    // koin-android needed only for ProgramReceiver (BroadcastReceivers can't be constructor-injected)
    implementation(platform(libs.koin.bom))
    implementation(libs.bundles.koin)

    implementation(libs.timber)
    implementation(libs.androidx.core)
    implementation(libs.startUp)
}
