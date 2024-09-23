plugins {
    id("philo.fillsketch.android.application")
}

android {
    namespace = "com.dev.philo.fillsketch_princess"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.dev.philo.fillsketch_princess"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(project(":feature:main"))
}