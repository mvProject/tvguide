pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            mavenContent {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "TV Program Guide"
include(":app")
include(":core:core-models")
include(":core:core-utils")
include(":core:core-database")
include(":core:core-network")
include(":core:core-datastore")
include(":core:core-domain")
include(":core:core-ui")
include(":core:core-data")
include(":platform")
include(":infrastructure:dependency-injection")
include(":features:feature-onboard")
include(":features:feature-settings")
include(":features:feature-settings-app")
include(":features:feature-settings-backup")
include(":features:feature-settings-channels")
include(":features:feature-channellist")
include(":features:feature-channels")
