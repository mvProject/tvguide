plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    compileSdk = Config.compileSdk

    defaultConfig {
        applicationId = "com.kinectpro.tvprogramguide"
        minSdk = Config.minSdk
        targetSdk = Config.targetSdk
        versionCode = Config.versionCode
        versionName = Config.versionName
        testInstrumentationRunner = Config.androidTestInstrumentation
    }

    buildTypes {
        getByName("debug") {
            isDebuggable = true
            setProperty(
                "archivesBaseName",
                "${rootProject.name}_${project.android.defaultConfig.versionName}"
            )
        }

        getByName("release") {
            //  signingConfig = signingConfigs.getByName("configRelease")
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            isDebuggable = false
            setProperty(
                "archivesBaseName",
                "${rootProject.name}_${project.android.defaultConfig.versionName}"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }
    viewBinding {
        android.buildFeatures.viewBinding = true
    }
}

dependencies {

    implementation(Dependencies.appLibraries)
    implementation(Dependencies.preference)
    implementation(Dependencies.network)
    implementation(Dependencies.logging)
    implementation(Dependencies.lifecycleKtx)
    implementation(Dependencies.navigationKtx)
    implementation(Dependencies.coroutines)
    implementation(Dependencies.pager)

    implementation("androidx.datastore:datastore-preferences:1.0.0")
    implementation("androidx.room:room-runtime:2.4.0")
    implementation("androidx.room:room-ktx:2.4.0")
    kapt("androidx.room:room-compiler:2.4.0")

    implementation("io.coil-kt:coil:1.4.0")

    hilt()

    testImplementation(Dependencies.testLibraries)
    androidTestImplementation(Dependencies.androidTestLibraries)
}