import java.util.Properties
import java.io.FileInputStream

val secretsProps = Properties()
val secretsFile = file("secrets.properties")
if (secretsFile.exists()) {
    secretsProps.load(FileInputStream(secretsFile))
    println("✅ secrets.properties loaded: ${secretsProps.keys}")
} else {
    println("⚠️ secrets.properties NOT FOUND - using empty defaults")
}

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.devtools.ksp)
    alias(libs.plugins.secrets)
    alias(libs.plugins.google.services)
}

android {
    namespace = "com.jomambo.app"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.jomambo.app"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        manifestPlaceholders["ADMOB_APP_ID"] = secretsProps.getProperty("ADMOB_APP_ID", "")

        buildConfigField("String", "BANNER_ID", "\"${secretsProps.getProperty("BANNER_ID", "")}\"")
        buildConfigField("String", "INTERSTITIAL_ID", "\"${secretsProps.getProperty("INTERSTITIAL_ID", "")}\"")
        buildConfigField("String", "NATIVE_ID", "\"${secretsProps.getProperty("NATIVE_ID", "")}\"")
        buildConfigField("String", "REWARDED_ID", "\"${secretsProps.getProperty("REWARDED_ID", "")}\"")
        buildConfigField("String", "REWARDED_INTERSTITIAL_ID", "\"${secretsProps.getProperty("REWARDED_INTERSTITIAL_ID", "")}\"")
        buildConfigField("String", "PAYSTACK_PUBLIC_KEY", "\"${secretsProps.getProperty("PAYSTACK_PUBLIC_KEY", "")}\"")
        buildConfigField("String", "ADMOB_APP_ID", "\"${secretsProps.getProperty("ADMOB_APP_ID", "")}\"")
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.play.services.ads)
}
