import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("philo.fillsketch.android.feature")
}

android {
    namespace = "com.dev.philo.fillsketch.feature.main"

    buildTypes {
        getByName("debug") {
            buildConfigField("String", "ADMOB_BANNER_ID", "\"${getPropertyKey("TEST_ADMOB_BANNER_ID")}\"")
            buildConfigField("String", "ADMOB_INTERSTITIAL_ID", "\"${getPropertyKey("TEST_ADMOB_INTERSTITIAL_ID")}\"")
            buildConfigField("String", "ADMOB_REWARDED_SKETCH_ID", "\"${getPropertyKey("TEST_ADMOB_REWARDED_SKETCH_ID")}\"")
            buildConfigField("String", "ADMOB_REWARDED_MAGIC_ID", "\"${getPropertyKey("TEST_ADMOB_REWARDED_MAGIC_ID")}\"")
        }

        getByName("release") {
            buildConfigField("String", "ADMOB_BANNER_ID", "\"${getPropertyKey("RELEASE_ADMOB_BANNER_ID")}\"")
            buildConfigField("String", "ADMOB_INTERSTITIAL_ID", "\"${getPropertyKey("RELEASE_ADMOB_INTERSTITIAL_ID")}\"")
            buildConfigField("String", "ADMOB_REWARDED_SKETCH_ID", "\"${getPropertyKey("RELEASE_ADMOB_REWARDED_SKETCH_ID")}\"")
            buildConfigField("String", "ADMOB_REWARDED_MAGIC_ID", "\"${getPropertyKey("RELEASE_ADMOB_REWARDED_MAGIC_ID")}\"")
        }
    }
}

fun getPropertyKey(propertyKey: String): String {
    return gradleLocalProperties(rootDir, providers).getProperty(propertyKey)
}

dependencies {
    implementation(project(":feature:home"))
    implementation(project(":feature:drawing"))
}