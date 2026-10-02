plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
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
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.4"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    
    kotlin {
        jvmToolchain(17)
    }

    // ===== SECRETS LOADER — THIS GO FIX YOUR BUILD =====
    val secretsFile = rootProject.file("app/secrets.properties")
    val secrets = java.util.Properties()
    if (secretsFile.exists()) {
        secrets.load(secretsFile.inputStream())
    }

    buildTypes {
        getByName("debug") {
            buildConfigField("String", "ADMOB_APP_ID", "\"${secrets.getProperty("ADMOB_APP_ID", "")}\"")
            buildConfigField("String", "BANNER_ID", "\"${secrets.getProperty("BANNER_ID", "")}\"")
            buildConfigField("String", "INTERSTITIAL_ID", "\"${secrets.getProperty("INTERSTITIAL_ID", "")}\"")
            buildConfigField("String", "NATIVE_ID", "\"${secrets.getProperty("NATIVE_ID", "")}\"")
            buildConfigField("String", "REWARDED_ID", "\"${secrets.getProperty("REWARDED_ID", "")}\"")
            buildConfigField("String", "REWARDED_INTERSTITIAL_ID", "\"${secrets.getProperty("REWARDED_INTERSTITIAL_ID", "")}\"")
            buildConfigField("String", "PAYSTACK_PUBLIC_KEY", "\"${secrets.getProperty("PAYSTACK_PUBLIC_KEY", "")}\"")
        }
        getByName("release") {
            buildConfigField("String", "ADMOB_APP_ID", "\"${secrets.getProperty("ADMOB_APP_ID", "")}\"")
            buildConfigField("String", "BANNER_ID", "\"${secrets.getProperty("BANNER_ID", "")}\"")
            buildConfigField("String", "INTERSTITIAL_ID", "\"${secrets.getProperty("INTERSTITIAL_ID", "")}\"")
            buildConfigField("String", "NATIVE_ID", "\"${secrets.getProperty("NATIVE_ID", "")}\"")
            buildConfigField("String", "REWARDED_ID", "\"${secrets.getProperty("REWARDED_ID", "")}\"")
            buildConfigField("String", "REWARDED_INTERSTITIAL_ID", "\"${secrets.getProperty("REWARDED_INTERSTITIAL_ID", "")}\"")
            buildConfigField("String", "PAYSTACK_PUBLIC_KEY", "\"${secrets.getProperty("PAYSTACK_PUBLIC_KEY", "")}\"")
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
