package common

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureAndroidCompose(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    commonExtension.apply {
        buildFeatures {
            compose = true
        }

        dependencies {
            val bom = libs.findLibrary("androidx-compose-bom").get()
            implementation(platform(bom))
            implementation(libs.findLibrary("androidx-compose-activity").get())
            implementation(libs.findLibrary("androidx-compose-ui-ui").get())
            implementation(libs.findLibrary("androidx-compose-foundation-foundation").get())
            implementation(libs.findLibrary("androidx-compose-material-icons-core").get())
            implementation(libs.findLibrary("androidx-compose-material-icons-extended").get())
            implementation(libs.findLibrary("androidx-compose-material3-material3").get())
            implementation(libs.findLibrary("androidx-compose-lifecycle-runtime").get())
            implementation(libs.findLibrary("androidx-compose-lifecycle-viewmodel").get())
            implementation(libs.findLibrary("androidx-compose-lifecycle-runtime").get())
            implementation(libs.findLibrary("androidx-compose-ui-tooling-preview").get())
            debugImplementation(libs.findLibrary("androidx-compose-ui-tooling").get())
        }
    }
}
