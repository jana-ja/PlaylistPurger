import com.android.tools.r8.internal.me
import java.io.FileInputStream
import java.util.Properties
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(FileInputStream(localPropertiesFile))
}
val clientSecret = localProperties.getProperty("CLIENT_SECRET") ?: ""
val clientId = localProperties.getProperty("CLIENT_ID") ?: ""

plugins {
    alias(libs.plugins.android.application)
//    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    // cmp
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    // navigation
    alias(libs.plugins.jetbrains.kotlin.serialization)
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {

        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)

//                    implementation(libs.androidx.core.ktx)
//        implementation(libs.androidx.lifecycle.runtime.ktx)
//        implementation(libs.androidx.activity.compose)
//        implementation(platform(libs.androidx.compose.bom))
//        implementation(libs.androidx.ui)
//        implementation(libs.androidx.ui.graphics)
//        implementation(libs.androidx.ui.tooling.preview)
//        implementation(libs.androidx.material3)
//        testImplementation(libs.junit)
//        androidTestImplementation(libs.androidx.junit)
//        androidTestImplementation(libs.androidx.espresso.core)
//        androidTestImplementation(platform(libs.androidx.compose.bom))
//        androidTestImplementation(libs.androidx.ui.test.junit4)
//        debugImplementation(libs.androidx.ui.tooling)
//        debugImplementation(libs.androidx.ui.test.manifest)
            // koin
            implementation(libs.koin.android)
            implementation(libs.koin.androidx.compose)
            // ktor
            implementation(libs.ktor.client.okhttp)

        }

        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            // spotify
            implementation(libs.spotify.auth)
            // navigation
            implementation(libs.jetbrains.compose.navigation)
            implementation(libs.kotlinx.serialization)
            // coil
            implementation(libs.coil.compose)
            implementation(libs.coil.compose.core)
            implementation(libs.coil.network.ktor2)
            implementation(libs.coil.network.ktor3)
            implementation(libs.coil.mp)
            // data store
            implementation(libs.androidx.datastore)
            implementation(libs.androidx.datastore.preferences)
            // koin
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            api(libs.koin.core)
            // ktor
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.auth)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            // ktor logging
            implementation(libs.ktor.client.logging)
        }
        nativeMain.dependencies {
            // ktor
            implementation(libs.ktor.client.darwin)
        }
    }
}

android {
    namespace = "de.janaja.playlistpurger"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "de.janaja.playlistpurger"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        manifestPlaceholders["redirectSchemeName"] = "asdf"
        manifestPlaceholders["redirectHostName"] = "callback"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
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
//    // todo behalten?
//    kotlinOptions {
//        jvmTarget = "11"
//    }
    buildFeatures {
        // todo behalten?
//        compose = true
        buildConfig = true
    }
}

dependencies {
    // todo bleiben die hier? - nö

    // cmp
    debugImplementation(compose.uiTooling)

}