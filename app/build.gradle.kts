import java.util.Properties
import java.io.FileInputStream
import java.io.File

// ✅ Helper function to read keys from local.properties
fun getLocalProperty(key: String): String {
    val properties = Properties()
    val localPropertiesFile = File("local.properties")
    if (localPropertiesFile.exists()) {
        properties.load(FileInputStream(localPropertiesFile))
    }
    return properties.getProperty(key, "")
}

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.example.spotifyrecommender"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.spotifyrecommender"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        // ✅ Pass secrets to BuildConfig
        buildConfigField("String", "SPOTIFY_CLIENT_ID", "\"${getLocalProperty("SPOTIFY_CLIENT_ID")}\"")
        buildConfigField("String", "SPOTIFY_CLIENT_SECRET", "\"${getLocalProperty("SPOTIFY_CLIENT_SECRET")}\"")
    }

    buildFeatures {
        buildConfig = true   // ✅ Important for BuildConfig class generation
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.4"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.1")
    implementation(platform("androidx.compose:compose-bom:2023.10.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material3:material3-window-size-class")
    implementation("androidx.compose.foundation:foundation:1.6.2")
    implementation("androidx.compose.ui:ui-text:1.6.2")
    implementation("androidx.compose.material:material-icons-extended-android:1.6.7")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.0")
    implementation("io.coil-kt:coil-compose:2.5.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    // Accompanist System UI Controller (for status bar color control)
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.33.2-alpha")

    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}
