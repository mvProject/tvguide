import org.gradle.api.artifacts.dsl.DependencyHandler

object Dependencies {

    // android ui
    private const val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
    private const val coreKtx = "androidx.core:core-ktx:${Versions.coreKtx}"
    private const val timber = "com.jakewharton.timber:timber:${Versions.timber}"
    private const val material = "com.google.android.material:material:${Versions.material}"

    // Network
    private const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    private const val gsonConverter =
        "com.squareup.retrofit2:converter-gson:${Versions.gsonConverter}"
    private const val loggingInterceptor =
        "com.squareup.okhttp3:logging-interceptor:${Versions.loggingInterceptor}"
    private const val gson = "com.google.code.gson:gson:${Versions.gson}"

    // Preference
    // private const val preferenceKtx = "androidx.preference:preference-ktx:${Versions.preferenceKtx}"
    private const val dataStore = "androidx.datastore:datastore-preferences:${Versions.dataStore}"

    private const val kotlinxDatetime =
        "org.jetbrains.kotlinx:kotlinx-datetime:${Versions.datetime}"

    // Annotation
    private const val annotationX = "androidx.annotation:annotation:${Versions.annotation}"

    // Coroutine
    private const val coroutineCoreKtx =
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutinesCoreKtx}"
    private const val coroutineAndroidKtx =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutinesAndroid}"

    // DI
    const val hilt = "com.google.dagger:hilt-android:${Versions.daggerHilt}"
    const val hiltDaggerCompiler = "com.google.dagger:hilt-android-compiler:${Versions.daggerHilt}"
    const val hiltAndroidCompiler = "androidx.hilt:hilt-compiler:${Versions.androidHilt}"

    // Room
    const val roomRuntime = "androidx.room:room-runtime:${Versions.room}"
    const val roomKtx = "androidx.room:room-ktx:${Versions.room}"
    const val roomCompiler = "androidx.room:room-compiler:${Versions.room}"

    // test libs
    private const val junit = "junit:junit:${Versions.junit}"
    private const val extJUnit = "androidx.test.ext:junit:${Versions.extJunit}"
    private const val espressoCore = "androidx.test.espresso:espresso-core:${Versions.espresso}"

    // Compose
    private const val viewModelCompose =
        "androidx.lifecycle:lifecycle-viewmodel-compose:${Versions.viewModelCompose}"
    private const val livedataCompose =
        "androidx.compose.runtime:runtime-livedata:${Versions.compose}"
    private const val activityCompose =
        "androidx.activity:activity-compose:${Versions.activityCompose}"
    private const val composeMaterial = "androidx.compose.material:material:${Versions.compose}"
    private const val composeUi = "androidx.compose.ui:ui:${Versions.compose}"
    private const val composeTooling = "androidx.compose.ui:ui-tooling:${Versions.compose}"
    private const val composeFoundation =
        "androidx.compose.foundation:foundation:${Versions.compose}"
    private const val composeIcons =
        "androidx.compose.material:material-icons-core:${Versions.compose}"
    private const val composeIconsExtended =
        "androidx.compose.material:material-icons-extended:${Versions.compose}"

    private const val composeJunit = "androidx.compose.ui:ui-test-junit4:${Versions.compose}"

    // StartUp
    private const val startUp = "androidx.startup:startup-runtime:${Versions.startUp}"

    // Coil
    private const val coilKt = "io.coil-kt:coil:${Versions.coil}"
    private const val coilKtCompose = "io.coil-kt:coil-compose:${Versions.coilCompose}"

    // ViewPager
    private const val accompanistPager =
        "com.google.accompanist:accompanist-pager:${Versions.accompanistPager}"
    private const val accompanistPagerIndicators =
        "com.google.accompanist:accompanist-pager-indicators:${Versions.accompanistPager}"

    // Navigation
    private const val navComposeHilt =
        "androidx.hilt:hilt-navigation-compose:${Versions.navigationComposeHilt}"
    private const val navCompose =
        "androidx.navigation:navigation-compose:${Versions.navigationCompose}"
    private const val accompanistNavigationAnimated =
        "com.google.accompanist:accompanist-navigation-animation:${Versions.accompanistNavigationAnimated}"

    // WorkManager
    private const val workRuntimeKtx =
        "androidx.work:work-runtime-ktx:${Versions.workRuntime}"
    private const val workHilt =
        "androidx.hilt:hilt-work:${Versions.workHilt}"

    val appLibraries = arrayListOf<String>().apply {
        add(coreKtx)
        //   add(appcompat)
        add(material)
    }

    val appComposeLibraries = arrayListOf<String>().apply {
        add(activityCompose)
        add(composeMaterial)
        add(composeUi)
        add(composeTooling)
        add(composeFoundation)
        add(composeIcons)
        add(composeIconsExtended)
    }

    val logging = arrayListOf<String>().apply {
        add(timber)
    }

    val preference = arrayListOf<String>().apply {
        add(dataStore)
    }

    val datetime = arrayListOf<String>().apply {
        add(kotlinxDatetime)
    }

    val annotation = arrayListOf<String>().apply {
        add(annotationX)
    }

    val startupRuntime = arrayListOf<String>().apply {
        add(startUp)
    }

    val coil = arrayListOf<String>().apply {
        add(coilKt)
        add(coilKtCompose)
    }

    val pagerCompose = arrayListOf<String>().apply {
        add(accompanistPager)
        add(accompanistPagerIndicators)
    }

    val navigationCompose = arrayListOf<String>().apply {
        add(navCompose)
        add(accompanistNavigationAnimated)
        add(navComposeHilt)
    }

    val workManager = arrayListOf<String>().apply {
        add(workRuntimeKtx)
        add(workHilt)
    }

    val lifecycleCompose = arrayListOf<String>().apply {
        add(viewModelCompose)
        add(livedataCompose)
    }

    val coroutines = arrayListOf<String>().apply {
        add(coroutineCoreKtx)
        add(coroutineAndroidKtx)
    }

    val network = arrayListOf<String>().apply {
        add(retrofit)
        add(gsonConverter)
        add(loggingInterceptor)
        add(gson)
    }

    val androidTestLibraries = arrayListOf<String>().apply {
        add(extJUnit)
        add(espressoCore)
    }

    val testLibraries = arrayListOf<String>().apply {
        add(junit)
    }
    val testComposeLibraries = arrayListOf<String>().apply {
        add(composeJunit)
    }
}

fun DependencyHandler.implementationHilt() {
    implementation(Dependencies.hilt)
    kapt(Dependencies.hiltDaggerCompiler)
    kapt(Dependencies.hiltAndroidCompiler)
}

fun DependencyHandler.implementationRoom() {
    implementation(Dependencies.roomRuntime)
    implementation(Dependencies.roomKtx)
    kapt(Dependencies.roomCompiler)
}
