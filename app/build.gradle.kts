plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)

    alias(libs.plugins.google.gms.google.services)
//    id("com.android.application")
//    id("com.google.gms.google-services")
}

android {
    namespace = "com.uilover.project2002"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.uilover.project2002"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.firebase.database)


    implementation ("com.github.bumptech.glide:glide:4.12.0")
    implementation ("com.github.ismaeldivita:chip-navigation-bar:1.4.0")
    implementation ("com.github.Dimezis:BlurView:version-2.0.3")
    implementation(libs.androidx.activity)
    implementation(libs.firebase.auth)
    implementation(platform("com.google.firebase:firebase-bom:33.5.1"))
    implementation ("com.google.firebase:firebase-auth:23.1.0")
    implementation ("com.google.firebase:firebase-core:20.0.0")

}