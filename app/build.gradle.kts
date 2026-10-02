import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.jomambo.app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.jomambo.app"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        buildConfig = true
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    
    kotlin {
        jvmToolchain(17)
    }

    // ===== SECRETS LOADER WITH FALLBACK TEST IDS =====
    val secretsFile = rootProject.file("app/secrets.properties")
    val secrets = Properties()
    if (secretsFile.exists()) {
        secrets.load(secretsFile.inputStream())
    }
    // Use TEST ID if secrets empty (so GitHub build go GREEN)
    val admobAppId = secrets.getProperty("ADMOB_APP_ID", "ca-app-pub-3940256099942544~3347511713")
    val bannerId = secrets.getProperty("BANNER_ID", "ca-app-pub-3940256099942544/6300978111")
    val interId = secrets.getProperty("INTERSTITIAL_ID", "ca-app-pub-3940256099942544/1033173712")
    val nativeId = secrets.getProperty("NATIVE_ID", "ca-app-pub-3940256099942544/2247696110")
    val rewardedId = secrets.getProperty("REWARDED_ID", "ca-app-pub-3940256099942544/5224354917")
    val rewInterId = secrets.getProperty("REWARDED_INTERSTITIAL_ID", "ca-app-pub-3940256099942544/5354046379")
    val paystackKey = secrets.getProperty("PAYSTACK_PUBLIC_KEY", "")

    buildTypes {
        getByName("debug") {
            manifestPlaceholders["ADMOB_APP_ID"] = admobAppId
            buildConfigField("String", "ADMOB_APP_ID", "\"$admobAppId\"")
            buildConfigField("String", "BANNER_ID", "\"$bannerId\"")
            buildConfigField("String", "INTERSTITIAL_ID", "\"$interId\"")
            buildConfigField("String", "NATIVE_ID", "\"$nativeId\"")
            buildConfigField("String", "REWARDED_ID", "\"$rewardedId\"")
            buildConfigField("String", "REWARDED_INTERSTITIAL_ID", "\"$rewInterId\"")
            buildConfigField("String", "PAYSTACK_PUBLIC_KEY", "\"$paystackKey\"")
        }
        getByName("release") {
            manifestPlaceholders["ADMOB_APP_ID"] = admobAppId
            buildConfigField("String", "ADMOB_APP_ID", "\"$admobAppId\"")
            buildConfigField("String", "BANNER_ID", "\"$bannerId\"")
            buildConfigField("String", "INTERSTITIAL_ID", "\"$interId\"")
            buildConfigField("String", "NATIVE_ID", "\"$nativeId\"")
            buildConfigField("String", "REWARDED_ID", "\"$rewardedId\"")
            buildConfigField("String", "REWARDED_INTERSTITIAL_ID", "\"$rewInterId\"")
            buildConfigField("String", "PAYSTACK_PUBLIC_KEY", "\"$paystackKey\"")
            isMinifyEnabled = false
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation("androidx.compose.material3:material3:1.1.2")
    implementation("com.google.android.gms:play-services-ads:23.0.0")
    implementation("com.google.firebase:firebase-auth:23.0.0")
    implementation("com.google.firebase:firebase-firestore:25.1.0")
}
