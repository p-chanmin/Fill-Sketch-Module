package com.dev.philo.fillsketch.core.data.di

import com.dev.philo.fillsketch.core.data.repository.DrawingResultRepository
import com.dev.philo.fillsketch.core.data.repository.SettingRepository
import com.dev.philo.fillsketch.core.data.repository.SketchRepository
import com.dev.philo.fillsketch.core.database.datasource.FillSketchDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideSettingRepository(
        fillSketchDataSource: FillSketchDataSource
    ): SettingRepository {
        return SettingRepository(fillSketchDataSource)
    }

    @Provides
    @Singleton
    fun provideSketchRepository(
        fillSketchDataSource: FillSketchDataSource
    ): SketchRepository {
        return SketchRepository(fillSketchDataSource)
    }

    @Provides
    @Singleton
    fun provideMyWorkRepository(
        fillSketchDataSource: FillSketchDataSource
    ): DrawingResultRepository {
        return DrawingResultRepository(fillSketchDataSource)
    }
}