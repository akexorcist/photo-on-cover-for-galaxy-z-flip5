plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.akexorcist.photooncover.feature.home"
    compileSdk = 34

    defaultConfig {
        minSdk = 33
    }

    buildTypes {
        release {
            isMinifyEnabled = true
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
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.8"
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.10.1")

    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation("io.insert-koin:koin-core:3.4.3")
    implementation("io.insert-koin:koin-android:3.4.3")
    implementation("io.insert-koin:koin-androidx-compose:3.4.6")
    implementation("androidx.navigation:navigation-compose:2.7.0")

    implementation("io.coil-kt:coil-compose:2.4.0")
    implementation("com.vanniktech:android-image-cropper:4.5.0")
    implementation("org.burnoutcrew.composereorderable:reorderable:0.9.6")

    implementation(project(":base:core"))
    implementation(project(":base:resource"))
    implementation(project(":base:ui"))
}
