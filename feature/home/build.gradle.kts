import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("philo.fillsketch.android.feature")
}

android {
    namespace = "com.dev.philo.fillsketch.feature.home"

    defaultConfig {
        buildConfigField("String", "RECOMMEND_OTHER_VERSION_1", "\"${getPropertyKey("RECOMMEND_OTHER_VERSION_1")}\"")
        buildConfigField("String", "RECOMMEND_OTHER_VERSION_2", "\"${getPropertyKey("RECOMMEND_OTHER_VERSION_2")}\"")
    }
}

fun getPropertyKey(propertyKey: String): String {
    return gradleLocalProperties(rootDir, providers).getProperty(propertyKey)
}

dependencies {
}