plugins {
    id("philo.fillsketch.android.library")
    id("philo.fillsketch.android.room")
}

android {
    namespace = "com.dev.philo.fillsketch.core.database"
}

dependencies {
    implementation(project(":asset"))
}