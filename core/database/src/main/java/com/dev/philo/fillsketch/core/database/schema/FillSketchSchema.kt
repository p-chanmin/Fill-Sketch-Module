package com.dev.philo.fillsketch.core.database.schema

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class SettingSchema : RealmObject {
    var soundEffect: Boolean = true
    var backgroundMusic: Boolean = true
}

class SketchSchema : RealmObject {
    var sketchType: Int = -1
    var hasMagicBrush: Boolean = false
    var isLocked: Boolean = true
}

class DrawingResultSchema : RealmObject {
    @PrimaryKey
    var _id: Int = 0
    var sketchType: Int = -1
    var bitmapByteArray: ByteArray = ByteArray(0)
}