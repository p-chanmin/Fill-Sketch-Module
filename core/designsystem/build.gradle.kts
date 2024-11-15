plugins {
    id("philo.fillsketch.android.library")
    id("philo.fillsketch.android.compose")
}

android {
    namespace = "com.dev.philo.fillsketch.core.designsystem"
}

dependencies {
    implementation(project(":asset"))
    implementation(project(":core:model"))
}