import java.util.*

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    id("kotlin-parcelize")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    compileSdk = 32

    defaultConfig {
        applicationId = libs.versions.applicationId.get()
        minSdk = 24
        targetSdk = 32
        versionCode = 1
        versionName = "0.2.7"
        testInstrumentationRunner = libs.versions.androidTestInstrumentation.get()

        resourceConfigurations.addAll(listOf("en", "ru", "uk"))

        vectorDrawables.useSupportLibrary = true
    }

    val projectProperties = readProperties(file("../keystore.properties"))
    signingConfigs {
        register("configRelease").configure {
            storeFile = file(projectProperties["storeFile"] as String)
            storePassword = projectProperties["storePassword"] as String
            keyAlias = projectProperties["keyAlias"] as String
            keyPassword = projectProperties["keyPassword"] as String
        }
    }

    buildTypes {
        debug {
            isDebuggable = true
            setProperty(
                "archivesBaseName",
                "${rootProject.name}_${project.android.defaultConfig.versionName}"
            )
        }

        release {
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName("configRelease")
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
        jvmTarget = JavaVersion.VERSION_11.toString()
    }
    viewBinding {
        android.buildFeatures.viewBinding = true
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }
    packagingOptions {
        resources.excludes.add("/META-INF/{AL2.0,LGPL2.1}")
    }
    testOptions {
        unitTests.all {
            it.useJUnitPlatform()
        }
    }
}

fun readProperties(propertiesFile: File) = Properties().apply {
    propertiesFile.inputStream().use { fis ->
        load(fis)
    }
}

dependencies {

    implementation(libs.bundles.appLibraries)

    implementation(libs.dataStore)

    implementation(libs.bundles.network)

    implementation(libs.bundles.coroutines)

    implementation(libs.bundles.coil)

    implementation(libs.bundles.compose)

    implementation(libs.bundles.lifecycleCompose)

    implementation(libs.bundles.pagerCompose)

    implementation(libs.bundles.navHiltCompose)

    implementation(libs.startUp)

    implementation(libs.bundles.workManager)

    implementation(libs.kotlinxDatetime)

    implementation(libs.bundles.firebase)

    implementation(libs.bundles.room)
    kapt(libs.roomCompiler)

    implementation(libs.hilt)
    kapt(libs.bundles.hiltCompiler)

    testImplementation(libs.testJunit)
    testImplementation(libs.kotestRunnerJunit5)
    testImplementation(libs.kotestAssertionsCore)
    testImplementation(libs.mockk)

    implementation(libs.bundles.testAndroid)

    debugImplementation(libs.bundles.testDebugCompose)
}
