
plugins {
    id("com.android.dynamic-feature")
    id("kotlin-android")
    id("kotlin-kapt")
}
android {
    namespace = "com.saneet.dynamicfeature"
    compileSdk = 32

    defaultConfig {
        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        named("release") {
            isMinifyEnabled = false
            setProguardFiles(listOf(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"))
        }
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(project(":app"))
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.2.1")
    implementation("com.google.android.play:core-ktx:1.8.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.annotation:annotation:1.6.0")
}

