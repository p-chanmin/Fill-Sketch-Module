package com.dev.philo.fillsketch.core.database.di

import com.dev.philo.fillsketch.asset.SketchResource
import com.dev.philo.fillsketch.core.database.schema.DrawingResultSchema
import com.dev.philo.fillsketch.core.database.schema.SettingSchema
import com.dev.philo.fillsketch.core.database.schema.SketchSchema
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideRealm(): Realm {
        val config = RealmConfiguration
            .Builder(
                schema = setOf(
                    SettingSchema::class,
                    SketchSchema::class,
                    DrawingResultSchema::class,
                )
            ).name("fillsketch.realm")
            .schemaVersion(1)
            .deleteRealmIfMigrationNeeded() // develop only
            .build()

        val realm = Realm.open(config)

        realm.writeBlocking {
            if (query<SettingSchema>().find().firstOrNull() == null) {
                copyToRealm(SettingSchema())
            }

            if (query<SketchSchema>().find().firstOrNull() == null) {
                SketchResource.sketchOutlineResourceIds.forEachIndexed { i, _ ->
                    copyToRealm(SketchSchema().apply {
                        sketchType = i
                        isLocked = (i - 1) % 4 == 0 || (i - 1) % 4 == 1
                    })
                }
            }
        }

        return realm
    }
}