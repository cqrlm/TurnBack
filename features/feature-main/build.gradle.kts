plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.detekt)
}

android {
    namespace = "com.example.feature.main"
    compileSdk = 35

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions {
        jvmTarget = "21"
    }
}

dependencies {

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)

    ksp(libs.hilt.android.compiler)
    implementation(libs.hilt.android)

    implementation(projects.core.architecture)
    implementation(projects.core.ui.theme)
    implementation(projects.core.ui.common)
    implementation(projects.core.data)
    implementation(projects.core.common)
    implementation(projects.core.sharedPreferences)
    implementation(projects.core.timer)
    implementation(projects.core.timerPreset)
    implementation(projects.core.navigation)
    implementation(projects.resources)

    implementation(projects.features.featureTimer)
    implementation(projects.features.featureStopwatch)

    detektPlugins(libs.detekt.formatting)
}
