import org.gradle.api.artifacts.dsl.DependencyHandler

object Dependencies {

    // android ui
    private const val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
    private const val coreKtx = "androidx.core:core-ktx:${Versions.coreKtx}"
    private const val constraintLayout =
        "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    private const val activityKtx = "androidx.activity:activity-ktx:${Versions.activityKtx}"
    private const val timber = "com.jakewharton.timber:timber:${Versions.timber}"
    private const val material = "com.google.android.material:material:${Versions.material}"
    private const val fragmentKtx = "androidx.fragment:fragment-ktx:${Versions.fragmentKtx}"

    // test libs
    private const val junit = "junit:junit:${Versions.junit}"
    private const val extJUnit = "androidx.test.ext:junit:${Versions.extJunit}"
    private const val espressoCore = "androidx.test.espresso:espresso-core:${Versions.espresso}"

    // network
    private const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    private const val gsonConverter =
        "com.squareup.retrofit2:converter-gson:${Versions.gsonConverter}"
    private const val loggingInterceptor =
        "com.squareup.okhttp3:logging-interceptor:${Versions.loggingInterceptor}"
    private const val gson = "com.google.code.gson:gson:${Versions.gson}"

    // preference
    private const val preferenceKtx = "androidx.preference:preference-ktx:${Versions.preferenceKtx}"

    // annotation
    private const val annotationX = "androidx.annotation:annotation:${Versions.annotation}"

    // navigation
    private const val navigationFragmentKtx =
        "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
    private const val navigationUiKtx =
        "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"

    // lifecycle
    private const val lifecycleLiveDataKtx =
        "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycle}"
    private const val lifecycleViewModelKtx =
        "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"

    // coroutine
    private const val coroutineCoreKtx =
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutinesCoreKtx}"
    private const val coroutineAndroidKtx =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutinesAndroid}"

    // DI
    const val hilt = "com.google.dagger:hilt-android:${Versions.daggerHilt}"
    const val hiltCompiler = "com.google.dagger:hilt-android-compiler:${Versions.daggerHilt}"

    // Room
    const val roomRuntime = "androidx.room:room-runtime:${Versions.room}"
    const val roomKtx = "androidx.room:room-ktx:${Versions.room}"
    const val roomCompiler = "androidx.room:room-compiler:${Versions.room}"

    private const val recyclerview = "androidx.recyclerview:recyclerview:${Versions.recyclerView}"

    private const val viewpager2 = "androidx.viewpager2:viewpager2:${Versions.viewPager2}"

    private const val coilKt = "io.coil-kt:coil:${Versions.coil}"

    // Compose
    private const val coilKtCompose = "io.coil-kt:coil-compose:${Versions.coilCompose}"
    private const val viewModelCompose = "androidx.lifecycle:lifecycle-viewmodel-compose:${Versions.viewModelCompose}"
    private const val activityCompose = "androidx.activity:activity-compose:${Versions.activityCompose}"
    private const val composeMaterial = "androidx.compose.material:material:${Versions.compose}"
    private const val composeUi = "androidx.compose.ui:ui:${Versions.compose}"
    private const val composeTooling = "androidx.compose.ui:ui-tooling:${Versions.compose}"
    private const val composeFoundation = "androidx.compose.foundation:foundation:${Versions.compose}"
    private const val composeIcons = "androidx.compose.material:material-icons-core:${Versions.compose}"
    private const val composeIconsExtended = "androidx.compose.material:material-icons-extended:${Versions.compose}"

    private const val composeJunit = "androidx.compose.ui:ui-test-junit4:${Versions.compose}"

    private const val accompanistPager = "com.google.accompanist:accompanist-pager:${Versions.accompanistPager}"
    private const val accompanistPagerIndicators = "com.google.accompanist:accompanist-pager-indicators:${Versions.accompanistPager}"

    val appLibraries = arrayListOf<String>().apply {
        add(coreKtx)
        add(appcompat)
        add(constraintLayout)
        add(activityKtx)
        add(material)
        add(fragmentKtx)
        add(recyclerview)
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
        add(preferenceKtx)
    }

    val annotation = arrayListOf<String>().apply {
        add(annotationX)
    }

    val pager= arrayListOf<String>().apply {
        add(viewpager2)
    }

    val coil= arrayListOf<String>().apply {
        add(coilKt)
        add(coilKtCompose)
    }

    val pagerCompose= arrayListOf<String>().apply {
        add(accompanistPager)
        add(accompanistPagerIndicators)
    }

    val navigationKtx = arrayListOf<String>().apply {
        add(navigationFragmentKtx)
        add(navigationUiKtx)
    }

    val lifecycleKtx = arrayListOf<String>().apply {
        add(lifecycleLiveDataKtx)
        add(lifecycleViewModelKtx)
    }
    val lifecycleCompose = arrayListOf<String>().apply {
        add(viewModelCompose)
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
    kapt(Dependencies.hiltCompiler)
}

fun DependencyHandler.implementationRoom() {
    implementation(Dependencies.roomRuntime)
    implementation(Dependencies.roomKtx)
    kapt(Dependencies.roomCompiler)
}