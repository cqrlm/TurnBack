plugins {
    alias(libs.plugins.jetbrains.kotlin.jvm)
    alias(libs.plugins.detekt)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    detektPlugins(libs.detekt.formatting)
}
