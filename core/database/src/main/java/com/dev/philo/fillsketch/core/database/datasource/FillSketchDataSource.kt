package com.dev.philo.fillsketch.core.database.datasource

import com.dev.philo.fillsketch.core.database.schema.DrawingResultSchema
import com.dev.philo.fillsketch.core.database.schema.SettingSchema
import com.dev.philo.fillsketch.core.database.schema.SketchSchema
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.max
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FillSketchDataSource @Inject constructor(
    private val realm: Realm
) {

    val settings = realm
        .query<SettingSchema>()
        .asFlow()
        .map { it.list.toList().first() }

    val sketchList = realm
        .query<SketchSchema>()
        .asFlow()
        .map { it.list.toList() }

    val drawingResultList = realm
        .query<DrawingResultSchema>()
        .asFlow()
        .map { it.list.toList() }

    fun getDrawingResultById(myWorkId: Int) =
        realm.query<DrawingResultSchema>("_id == $0", myWorkId).asFlow().map { it.list.first() }

    suspend fun addDrawingResult(sketchType: Int): Int {

        val id = getNextDrawingResultPrimaryKey()

        realm.write {
            copyToRealm(
                DrawingResultSchema().apply {
                    this._id = id
                    this.sketchType = sketchType
                }
            )
        }

        return id
    }

    suspend fun updateBackgroundMusicSetting() {
        realm.write {
            val setting = query<SettingSchema>().find().first()
            setting.backgroundMusic = !setting.backgroundMusic
        }
    }

    suspend fun updateSoundEffectSetting() {
        realm.write {
            val setting = query<SettingSchema>().find().first()
            setting.soundEffect = !setting.soundEffect
        }
    }

    suspend fun updateLockState(sketchType: Int, isLocked: Boolean = false) {
        realm.write {
            val sketch = query<SketchSchema>("sketchType == $0", sketchType).find().first()
            sketch.isLocked = isLocked
        }
    }

    suspend fun updateMagicBrushState(sketchType: Int, hasMagicBrush: Boolean = true) {
        realm.write {
            val sketch = query<SketchSchema>("sketchType == $0", sketchType).find().first()
            sketch.hasMagicBrush = hasMagicBrush
        }
    }

    suspend fun updateDrawingResult(
        drawingResultId: Int,
        pathsJsonData: String
    ) {
        realm.write {
            val drawingResult = query<DrawingResultSchema>("_id == $0", drawingResultId).find().first()
            drawingResult.pathsJsonData = pathsJsonData
        }
    }

    suspend fun deleteDrawingResult(drawingResultId: Int) {
        realm.write {
            val drawingResult = query<DrawingResultSchema>("_id == $0", drawingResultId).find().first()
            delete(drawingResult)
        }
    }

    private fun getNextDrawingResultPrimaryKey(): Int {
        val maxId = realm.query<DrawingResultSchema>().max<Int>("_id").find() ?: 0
        return maxId + 1
    }
}