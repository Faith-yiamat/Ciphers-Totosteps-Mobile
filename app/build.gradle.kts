plugins {
    id ("com.android.application")
    id ("org.jetbrains.kotlin.android")
}


android {
    namespace = "com.akirachix.totosteps"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.akirachix.totosteps"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    // Enable view binding
    buildFeatures {
        viewBinding = true
    }

    // Compatibility options for Java and Kotlin
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation (libs.androidx.core.ktx.v1120)
    implementation (libs.androidx.appcompat.v161)
    implementation (libs.material.v190)
    implementation (libs.androidx.activity.ktx)
    implementation (libs.androidx.constraintlayout)
    implementation (libs.circleimageview)
    implementation (libs.retrofit)
    implementation (libs.converter.gson)
    implementation (libs.kotlinx.coroutines.core.v171)
    implementation (libs.kotlinx.coroutines.android.v171)
    implementation(libs.androidx.activity)
    testImplementation (libs.junit)
    androidTestImplementation (libs.androidx.junit.v115)
    androidTestImplementation (libs.androidx.espresso.core.v351)
    implementation ("com.squareup.okhttp3:okhttp:4.9.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.0")
}
