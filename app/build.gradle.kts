import java.util.Properties

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.android.kotlin)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ksp)
    alias(libs.plugins.firebase.crashlitycs)
    alias(libs.plugins.gms.googleServices)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.kotlinx.serialization.plugin)
}

android {
    namespace = "com.mvproject.tvprogramguide"
    compileSdk = 35

    val projectProperties = readProperties(file("../keystore.properties"))
    signingConfigs {
        register("configRelease").configure {
            storeFile = file(projectProperties["storeFile"] as String)
            storePassword = projectProperties["storePassword"] as String
            keyAlias = projectProperties["keyAlias"] as String
            keyPassword = projectProperties["keyPassword"] as String
        }
    }

    defaultConfig {
        minSdk = 26
        targetSdk = 35
        versionCode = 90
        versionName = "1.0.0"
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
        val firebaseProperties = readProperties(file("../firebase.properties"))
        buildConfigField(
            "String",
            "FIREBASE_DATABASE_URL",
            firebaseProperties["FIREBASE_DATABASE_URL"] as String
        )
        buildConfigField(
            "String",
            "FIREBASE_DATABASE_TABLE",
            firebaseProperties["FIREBASE_DATABASE_TABLE"] as String
        )
        buildConfigField("String", "WEB_CLIENT_ID", firebaseProperties["WEB_CLIENT_ID"] as String)
    }

    buildTypes {
        debug {
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
        buildConfig = true
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
    implementation(libs.bundles.appLibraries)

    implementation(libs.dataStore)

    implementation(libs.kotlinx.serialization.json)

    implementation(libs.kotlinx.coroutines.core)

    implementation(libs.bundles.coil)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose)

    implementation(libs.bundles.lifecycleCompose)

    implementation(libs.composeNavigation)

    implementation(libs.startUp)

    implementation(libs.bundles.workManager)

    implementation(libs.kotlinxDatetime)

    // implementation(libs.bundles.playReview)
//
    // implementation(libs.bundles.playUpdate)

    implementation(libs.bundles.ksoup)

    implementation(libs.bundles.ktor)

    implementation(libs.bundles.credentials)

    implementation(platform(libs.firebase.bom))
    implementation(libs.bundles.firebase)

    implementation(platform(libs.koin.bom))
    implementation(libs.bundles.koin)

    implementation(libs.bundles.room)
    ksp(libs.roomCompiler)

    implementation(libs.accompanistPermissions)

    testImplementation(libs.testJunit)

    implementation(libs.bundles.kotest)

    testImplementation(libs.mockk)

    implementation(libs.bundles.testAndroid)

    debugImplementation(libs.bundles.testDebugCompose)
}

tasks.register("printVersionName") {
    println("${rootProject.name}_${project.android.defaultConfig.versionName}")
}