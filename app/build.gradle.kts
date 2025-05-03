import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    namespace = "cit.edu.gamego"
    compileSdk = 35

    defaultConfig {
        applicationId = "cit.edu.gamego"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        // added these
        val properties = Properties()
        val localPropertiesFile = rootProject.file("local.properties")
        if(localPropertiesFile.exists()){properties.load(localPropertiesFile.inputStream())}
        val apiKey = properties.getProperty("Giant_Bomb_API_KEY", "")
        buildConfigField("String","GIANT_BOMB_API_KEY","\"$apiKey\"")

        val firebaseDbUrl = properties.getProperty("FIREBASE_DB_URL", "")
        buildConfigField("String", "FIREBASE_DB_URL", "\"$firebaseDbUrl\"")
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        // added this
        buildConfig = true
        //
        compose = true
        viewBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.database)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // navigation drawer
    implementation(libs.material)
    implementation(libs.androidx.appcompat)

    //bottom nav view
    //implementation("com.google.android.material:material:1.1.0")

    //Recycler view
    implementation("androidx.recyclerview:recyclerview:1.2.1")

    //card view
    implementation("androidx.cardview:cardview:1.0.0")

    // Retrofit core library
    implementation("com.squareup.retrofit2:retrofit:2.9.0")

    // Gson converter for parsing JSON responses
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.12.0")


    //shimmer effect
    implementation("com.facebook.shimmer:shimmer:0.5.0")

    //Coroutine this if for making 1 second delays in each api request

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // for UI thread
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")

    //Firebase auth & modules
        implementation(enforcedPlatform("com.google.firebase:firebase-bom:32.7.3"))
        implementation("com.google.firebase:firebase-auth-ktx")
        implementation("com.google.firebase:firebase-database-ktx")
        implementation("com.google.firebase:firebase-firestore-ktx")
        implementation("com.google.firebase:firebase-storage-ktx")


    //Refresh layoout
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    //sticky
    implementation("androidx.coordinatorlayout:coordinatorlayout:1.2.0")
    implementation("com.google.android.material:material:1.6.1")
    implementation("androidx.appcompat:appcompat:1.3.1")
//    // OkHttp (optional but recommended for logging)
//    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")


}