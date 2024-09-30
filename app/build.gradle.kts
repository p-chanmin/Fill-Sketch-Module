import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("philo.fillsketch.android.application")
}

android {
    namespace = "com.dev.philo.fillsketch"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.dev.philo.fillsketch"
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

    buildTypes {
        getByName("debug") {
            buildConfigField("String", "ADMOB_APP_ID", "\"${getPropertyKey("TEST_ADMOB_APP_ID")}\"")
            manifestPlaceholders["ADMOB_APP_ID"] = getPropertyKey("TEST_ADMOB_APP_ID")
        }

        getByName("release") {
            buildConfigField("String", "ADMOB_APP_ID", "\"${getPropertyKey("RELEASE_ADMOB_APP_ID")}\"")
            manifestPlaceholders["ADMOB_APP_ID"] = getPropertyKey("RELEASE_ADMOB_APP_ID")
            isDebuggable = false
        }
    }
}

fun getPropertyKey(propertyKey: String): String {
    return gradleLocalProperties(rootDir, providers).getProperty(propertyKey)
}

dependencies {
    implementation(project(":feature:main"))
}