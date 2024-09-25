import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

group = "com.dev.innerview.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies {
    implementation(libs.android.gradlePlugin)
    implementation(libs.kotlin.gradlePlugin)
    compileOnly(libs.compose.compiler.gradle.plugin)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "philo.fillsketch.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }

        register("androidHilt") {
            id = "philo.fillsketch.android.hilt"
            implementationClass = "AndroidHiltConventionPlugin"
        }

        register("androidLibrary") {
            id = "philo.fillsketch.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }

        register("androidFeature") {
            id = "philo.fillsketch.android.feature"
            implementationClass = "AndroidFeatureConventionPlugin"
        }

        register("androidCompose") {
            id = "philo.fillsketch.android.compose"
            implementationClass = "AndroidComposeConventionPlugin"
        }

        register("androidKotlinSerialization") {
            id = "philo.fillsketch.android.kotlin.serialization"
            implementationClass = "AndroidKotlinSerializationConventionPlugin"
        }

        register("androidRealm") {
            id = "philo.fillsketch.android.realm"
            implementationClass = "AndroidRealmConventionPlugin"
        }

        register("androidGson") {
            id = "philo.fillsketch.android.gson"
            implementationClass = "AndroidGsonConventionPlugin"
        }
    }
}