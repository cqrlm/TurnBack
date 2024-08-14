enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "TurnBack"
include(":app")
include(":baselineprofile")
include(":core:common")
include(":core:shared-preferences")
include(":core:stopwatch")
include(":core:ui:theme")
include(":core:ui:common")
include(":core:database")
include(":core:data")
