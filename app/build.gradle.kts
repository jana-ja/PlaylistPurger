import com.android.tools.r8.internal.me
import java.io.FileInputStream
import java.util.Properties

val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(FileInputStream(localPropertiesFile))
}
val clientSecret = localProperties.getProperty("CLIENT_SECRET") ?: ""
val clientId = localProperties.getProperty("CLIENT_ID") ?: ""

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    // navigation
    alias(libs.plugins.jetbrains.kotlin.serialization)
}

android {
    namespace = "de.janaja.playlistpurger"
    compileSdk = 35

    defaultConfig {
        applicationId = "de.janaja.playlistpurger"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        manifestPlaceholders["redirectSchemeName"] = "asdf"
        manifestPlaceholders["redirectHostName"] = "callback"
    }

    buildTypes {
        debug {
            buildConfigField("String", "CLIENT_SECRET", "\"$clientSecret\"")
            buildConfigField("String", "CLIENT_ID", "\"$clientId\"")
        }
        release {
            buildConfigField("String", "CLIENT_SECRET", "\"$clientSecret\"")
            buildConfigField("String", "CLIENT_ID", "\"$clientId\"")
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    // spotify
    implementation(libs.spotify.auth)
    // viewmodel
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    // navigation
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization)
    // api
    implementation(libs.moshi)
    implementation(libs.retrofit)
    implementation(libs.converterMoshi)
    implementation(libs.logging.interceptor)
    // async image
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
    // data store
    implementation(libs.androidx.datastore.preferences)
    // koin
    implementation(project.dependencies.platform(libs.koin.bom))
    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)
    // compose preference
    implementation (libs.compose.preference)

}