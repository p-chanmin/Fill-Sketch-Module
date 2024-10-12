package com.dev.philo.fillsketch.core.database.di

import android.content.Context
import androidx.room.Room
import com.dev.philo.fillsketch.asset.SketchResource
import com.dev.philo.fillsketch.core.database.FillSketchDatabase
import com.dev.philo.fillsketch.core.database.entity.SettingEntity
import com.dev.philo.fillsketch.core.database.entity.SketchEntity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideFillSketchDatabase(
        @ApplicationContext context: Context,
    ): FillSketchDatabase {
        val db = Room.databaseBuilder(
            context = context,
            FillSketchDatabase::class.java,
            "fill_sketch_database.db"
        ).build()

        CoroutineScope(Dispatchers.IO).launch {
            if (db.settingEntityDao().getSettings().firstOrNull() == null) {
                db.settingEntityDao().insert(SettingEntity())
            }

            if (db.sketchEntityDao().getAllSketch()
                    .first().size != SketchResource.sketchOutlineResourceIds.size
            ) {
                db.sketchEntityDao().deleteAllSketch()
                SketchResource.sketchOutlineResourceIds.forEachIndexed { i, _ ->
                    db.sketchEntityDao().insert(
                        SketchEntity(
                            sketchType = i,
                            isLocked = (i - 1) % 4 == 0 || (i - 1) % 4 == 1
                        )
                    )
                }
            }
        }
        return db
    }

    @Provides
    fun provideSettingEntityDao(fillSketchDatabase: FillSketchDatabase) =
        fillSketchDatabase.settingEntityDao()

    @Provides
    fun provideSketchEntityDao(fillSketchDatabase: FillSketchDatabase) =
        fillSketchDatabase.sketchEntityDao()

    @Provides
    fun provideDrawingEntityDao(fillSketchDatabase: FillSketchDatabase) =
        fillSketchDatabase.drawingEntityDao()
}