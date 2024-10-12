package com.dev.philo.fillsketch.core.database.datasource

import com.dev.philo.fillsketch.core.database.dao.DrawingEntityDao
import com.dev.philo.fillsketch.core.database.dao.SettingEntityDao
import com.dev.philo.fillsketch.core.database.dao.SketchEntityDao
import com.dev.philo.fillsketch.core.database.entity.DrawingEntity
import com.dev.philo.fillsketch.core.database.entity.SettingEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FillSketchDataSource @Inject constructor(
    private val settingEntityDao: SettingEntityDao,
    private val sketchEntityDao: SketchEntityDao,
    private val drawingEntityDao: DrawingEntityDao,
) {

    val settings = settingEntityDao.getSettings().map { it ?: SettingEntity() }

    val sketchList = sketchEntityDao.getAllSketch()

    val drawingResultList = drawingEntityDao.getAllDrawing().map { it.reversed() }

    fun getMagicBrushStateBySketchType(sketchType: Int) =
        sketchEntityDao.getSketchBySketchType(sketchType).map { it.hasMagicBrush }

    fun getDrawingResultById(drawingResultId: Long) =
        drawingEntityDao.getDrawingById(drawingResultId)

    suspend fun addDrawingResult(sketchType: Int, latestByteArray: ByteArray): Long {
        return drawingEntityDao.insert(
            DrawingEntity(
                sketchType = sketchType,
                latestMaskBitmapByteArray = latestByteArray
            )
        )
    }

    suspend fun updateBackgroundMusicSetting() {
        settingEntityDao.update(
            settings.first().let {
                it.copy(
                    backgroundMusic = !it.backgroundMusic
                )
            }
        )
    }

    suspend fun updateSoundEffectSetting() {
        settingEntityDao.update(
            settings.first().let {
                it.copy(
                    soundEffect = !it.soundEffect
                )
            }
        )
    }

    suspend fun updateLockState(sketchType: Int, isLocked: Boolean = false) {
        sketchEntityDao.update(
            sketchEntityDao.getSketchBySketchType(sketchType).first().copy(
                isLocked = isLocked
            )
        )
    }

    suspend fun updateMagicBrushState(sketchType: Int, hasMagicBrush: Boolean = true) {
        sketchEntityDao.update(
            sketchEntityDao.getSketchBySketchType(sketchType).first().copy(
                hasMagicBrush = hasMagicBrush
            )
        )
    }

    suspend fun updateDrawingResult(
        drawingResultId: Long,
        latestMaskBitmapByteArray: ByteArray,
        resultBitmapByteArray: ByteArray,
    ) {
        drawingEntityDao.update(
            drawingEntityDao.getDrawingById(drawingResultId).first().copy(
                latestMaskBitmapByteArray = latestMaskBitmapByteArray,
                resultBitmapByteArray = resultBitmapByteArray
            )
        )
    }

    suspend fun deleteDrawingResult(drawingResultId: Long) {
        drawingEntityDao.deleteDrawingById(drawingResultId)
    }
}