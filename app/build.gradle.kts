import java.util.Properties

plugins {
    alias(libs.plugins.mvproject.android.application.compose)
    alias(libs.plugins.firebase.crashlitycs)
    alias(libs.plugins.gms.googleServices)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.kotlinx.serialization.plugin)
}

android {
    namespace = "com.mvproject.tvprogramguide"

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
        versionCode = 90
        versionName = "1.0.0"

        androidResources {
            localeFilters += listOf(
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
            )
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

    buildFeatures {
        buildConfig = true
    }

    testOptions {
        unitTests.all { it.useJUnitPlatform() }
    }
}

fun readProperties(propertiesFile: File) =
    Properties().apply {
        propertiesFile.inputStream().use { fis ->
            load(fis)
        }
    }

dependencies {
    implementation(project(":core:core-models"))
    implementation(project(":core:core-utils"))
    implementation(project(":core:core-database"))

    implementation(libs.dataStore)

    implementation(libs.kotlinx.serialization.json)

    implementation(libs.kotlinx.coroutines.core)

    // image
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)

    // navigation (not provided by convention plugin)
    implementation(libs.androidx.compose.navigation)

    implementation(libs.startUp)

    implementation(libs.bundles.workManager)

    implementation(libs.kotlinx.collections.immutable)

    implementation(libs.kotlinx.datetime)

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

    // Room Database
    implementation(libs.bundles.room)
    ksp(libs.room.compiler)

    implementation(libs.accompanist.permissions)

    testImplementation(libs.testJunit)

    implementation(libs.bundles.kotest)

    implementation(libs.turbine)
    testImplementation(libs.mockk)

    implementation(libs.bundles.testAndroid)

    debugImplementation(libs.bundles.testDebugCompose)
}

tasks.register("printVersionName") {
    println("${rootProject.name}_${project.android.defaultConfig.versionName}")
}
