plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.android.libraries.mapsplatform.secrets.gradle.plugin)
}

android {
    namespace = "com.example.loving_essentials"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.loving_essentials"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.play.services.base)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    //slider image
    implementation("com.github.denzcoskun:ImageSlideshow:0.1.2")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    // implementation("com.squareup.retrofit2:retrofit:2.5.0")
    // implementation("com.squareup.retrofit2:converter-gson:2.4.0")

    implementation("androidx.cardview:cardview:1.0.0")
    implementation("com.google.android.material:material:1.9.0")

    implementation(libs.play.services.auth)
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    //Cloudinary
    implementation("com.cloudinary:cloudinary-android:2.5.0");

    //OSM
    implementation("org.osmdroid:osmdroid-android:6.1.10")
    implementation("org.osmdroid:osmdroid-mapsforge:6.1.10")
    implementation("org.osmdroid:osmdroid-wms:6.1.10")
    implementation("org.osmdroid:osmdroid-geopackage:6.1.10")

    //Google Map
    implementation("com.google.android.gms:play-services-maps:19.0.0")
}
