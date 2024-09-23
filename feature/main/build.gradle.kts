plugins {
    id("philo.fillsketch.android.feature")
}

android {
    namespace = "com.dev.philo.fillsketch.feature.main"
}

dependencies {
    implementation(project(":feature:home"))
    implementation(project(":feature:drawing"))
}