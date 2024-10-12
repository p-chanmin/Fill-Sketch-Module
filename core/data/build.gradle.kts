plugins {
    id("philo.fillsketch.android.library")
    id("philo.fillsketch.android.room")
    id("philo.fillsketch.android.gson")
}

android {
    namespace = "com.dev.philo.fillsketch.core.data"
}

dependencies {
    implementation(project(":core:database"))
    implementation(project(":core:model"))
}