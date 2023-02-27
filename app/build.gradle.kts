import com.android.build.gradle.api.ApkVariantOutput
import com.android.build.gradle.api.ApplicationVariant
import com.android.build.gradle.api.BaseVariantOutput

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
}

android {
    namespace = "com.saneet.demo"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.saneet.demo"
        minSdk = 24
        targetSdk = 33
        versionCode = 4
        versionName = "4.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
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
        val options = this
        options.jvmTarget = "1.8"
    }
    dynamicFeatures.add(":dynamicfeature")
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    // Network
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.retrofit2:adapter-rxjava2:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.9.3")
    implementation("com.squareup.okhttp3:okhttp-urlconnection:4.9.3")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")
    implementation("com.jakewharton.byteunits:byteunits:0.9.1")

    // RxJava
    implementation("io.reactivex.rxjava2:rxjava:2.2.21")
    implementation("io.reactivex.rxjava2:rxkotlin:2.4.0")
    implementation("io.reactivex.rxjava2:rxandroid:2.1.1")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")

    // Dagger
    implementation("com.google.dagger:dagger:2.43.2")
    implementation("com.google.android.play:core-ktx:1.8.1")
    implementation("androidx.navigation:navigation-fragment-ktx:2.5.3")
    implementation("androidx.navigation:navigation-ui-ktx:2.5.3")
    kapt("com.google.dagger:dagger-compiler:2.43.2")
    //kapt "com.google.dagger:dagger-android-processor:2.43.2"

    // AndroidX
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")

    implementation("com.google.android.material:material:1.8.0")//1.7.0 has some bug with resource

    // Glide
    implementation("com.github.bumptech.glide:glide:4.13.0")
    kapt("com.github.bumptech.glide:compiler:4.13.0")

    // Testing
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.robolectric:robolectric:4.7.3")
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0")
    testImplementation("org.mockito:mockito-core:3.11.2")
    testImplementation("org.mockito:mockito-inline:3.11.2")
    testImplementation("io.mockk:mockk:1.12.0")
    testImplementation("androidx.test:core:1.5.0")
    testImplementation("androidx.test.ext:junit:1.1.5")
    testImplementation("androidx.arch.core:core-testing:2.2.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.1")
    implementation("androidx.recyclerview:recyclerview:1.2.1")

    //Leak Canary
    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.10")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.7.20")
}

//https://github.com/android/app-bundle-samples/blob/master/DynamicFeatures/app/src/main/java/com/google/android/samples/dynamicfeatures/MainActivity.kt
val bundletoolJar = project.rootDir.resolve("library/bundletool/bundletool-all-1.14.0.jar")
android.applicationVariants.all(object : Action<ApplicationVariant> {
    override fun execute(variant: ApplicationVariant) {
        variant.outputs.forEach { output: BaseVariantOutput? ->
            (output as? ApkVariantOutput)?.let { apkOutput: ApkVariantOutput ->
                var filePath = apkOutput.outputFile.absolutePath
                filePath = filePath.replaceAfterLast(".", "aab")
                filePath = filePath.replace("build/outputs/apk/", "build/outputs/bundle/")
                var outputPath = filePath.replace("build/outputs/bundle/", "build/outputs/apks/")
                outputPath = outputPath.replaceAfterLast(".", "apks")

                tasks.register<JavaExec>("buildApks${variant.name.capitalize()}") {
                    classpath = files(bundletoolJar)
                    args = listOf(
                        "build-apks",
                        "--overwrite",
                        "--local-testing",
                        "--bundle",
                        filePath,
                        "--output",
                        outputPath
                    )
                    dependsOn("bundle${variant.name.capitalize()}")
                }

                tasks.register<JavaExec>("installApkSplitsForTest${variant.name.capitalize()}") {
                    classpath = files(bundletoolJar)
                    args = listOf("install-apks", "--apks", outputPath)
                    dependsOn("buildApks${variant.name.capitalize()}")
                }
            }
        }
    }
})