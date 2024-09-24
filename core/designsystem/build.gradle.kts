plugins {
    id("philo.fillsketch.android.library")
    id("philo.fillsketch.android.compose")
}

android {
    namespace = "com.dev.philo.fillsketch.core.designsystem"
}

dependencies {
    implementation(libs.coil.compose)
    implementation(project(":asset"))
}