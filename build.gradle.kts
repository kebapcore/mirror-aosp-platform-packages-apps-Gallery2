plugins {
    id("com.android.application") version "7.2.2"
}

android {
    namespace = "com.nefiora.galerry"
    compileSdk = 31

    defaultConfig {
        applicationId = "com.nefiora.galerry"
        minSdk = 14
        targetSdk = 31
        versionCode = 40030
        versionName = "1.1.40030"

        vectorDrawables.useSupportLibrary = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard.flags")
            signingConfig = signingConfigs.getByName("debug")
        }
        debug {
            isDebuggable = true
        }
    }

    lint {
        checkReleaseBuilds = false
        abortOnError = false
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.3.1")
    implementation("androidx.support:support-v13:28.0.0")
    implementation("com.google.android.renderscript:renderscript-toolkit:1.0.0-beta1")
}
