package com.dev.philo.fillsketch.core.database.datasource

import com.dev.philo.fillsketch.core.database.schema.MyWorkSchema
import com.dev.philo.fillsketch.core.database.schema.SettingSchema
import com.dev.philo.fillsketch.core.database.schema.SketchSchema
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.map
import java.nio.file.Files.find
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

    val myWorksList = realm
        .query<MyWorkSchema>()
        .asFlow()
        .map { it.list.toList() }

    fun getMyWorkById(myWorkId: Int) =
        realm.query<MyWorkSchema>("_id == $0", myWorkId).asFlow().map { it.list.first() }

    suspend fun addMyWork(sketchType: Int): Int {

        val id = getNextMyWorksPrimaryKey()

        realm.write {
            copyToRealm(
                MyWorkSchema().apply {
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

    suspend fun updateMyWork(
        myWorkId: Int,
        pathsJsonData: String
    ) {
        realm.write {
            val myWork = query<MyWorkSchema>("_id == $0", myWorkId).find().first()
            myWork.pathsJsonData = pathsJsonData
        }
    }

    suspend fun deleteMyWork(myWorkId: Int) {
        realm.write {
            val myWork = query<MyWorkSchema>("_id == $0", myWorkId).find().first()
            delete(myWork)
        }
    }

    private fun getNextMyWorksPrimaryKey(): Int {
        return realm.query<MyWorkSchema>().find().size + 1
    }
}