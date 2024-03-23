import java.util.Properties

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.android.kotlin)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.firebase.crashlitycs)
    alias(libs.plugins.gms.googleServices)
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "com.mvproject.tvprogramguide"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
        targetSdk = 34
        versionCode = 15
        versionName = "0.6.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        resourceConfigurations.addAll(
            listOf(
                "en",
                "cs",
                "de",
                "es",
                "fr",
                "hr",
                "it",
                "lt",
                "lv",
                "nl",
                "pl",
                "pt",
                "ro",
                "ru",
                "tr",
                "uk",
            ),
        )

        vectorDrawables.useSupportLibrary = true

        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
        }
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
                "${rootProject.name}_${project.android.defaultConfig.versionName}",
            )
        }

        release {
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName("configRelease")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
            setProperty(
                "archivesBaseName",
                "${rootProject.name}_${project.android.defaultConfig.versionName}",
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.10"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "**/attach_hotspot_windows.dll"
            excludes += "META-INF/licenses/**"
            excludes += "META-INF/**.md"
        }
    }
    testOptions {
        unitTests.all {
            it.useJUnitPlatform()
        }
    }
}

fun readProperties(propertiesFile: File) =
    Properties().apply {
        propertiesFile.inputStream().use { fis ->
            load(fis)
        }
    }

dependencies {
    implementation("com.google.android.material:material:1.11.0")

    implementation(libs.bundles.appLibraries)

    implementation(libs.dataStore)

    implementation(libs.bundles.network)

    implementation(libs.kotlinx.coroutines.core)

    implementation(libs.bundles.coil)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose)

    implementation(libs.bundles.lifecycleCompose)

    implementation(libs.bundles.navHiltCompose)

    implementation(libs.startUp)

    implementation(libs.bundles.workManager)

    implementation(libs.kotlinxDatetime)

    implementation(libs.bundles.playReview)

    implementation(platform(libs.firebase.bom))
    implementation(libs.bundles.firebase)

    implementation(libs.bundles.room)
    ksp(libs.roomCompiler)

    implementation(libs.accompanistPermissions)

    implementation(libs.hilt)
    ksp(libs.bundles.hiltCompiler)

    testImplementation(libs.testJunit)

    implementation(libs.bundles.kotest)

    testImplementation(libs.mockk)

    implementation(libs.bundles.testAndroid)

    debugImplementation(libs.bundles.testDebugCompose)
}
