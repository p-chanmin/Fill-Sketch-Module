package com.dev.philo.fillsketch.core.data.converter

import com.dev.philo.fillsketch.core.model.ActionType
import com.dev.philo.fillsketch.core.model.PathData
import com.dev.philo.fillsketch.core.model.Point
import com.dev.philo.fillsketch.core.model.StrokeColor
import com.google.gson.JsonArray
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type

class PathDataAdapter : JsonSerializer<PathData>, JsonDeserializer<PathData> {
    override fun serialize(
        src: PathData,
        typeOfSrc: Type,
        context: JsonSerializationContext
    ): JsonElement {
        val jsonObject = JsonObject()
        jsonObject.addProperty("actionType", src.actionType.name)
        jsonObject.addProperty("strokeWidth", src.strokeWidth)

        val colorJsonObject = JsonObject()
        colorJsonObject.addProperty("r", src.strokeColor.r)
        colorJsonObject.addProperty("g", src.strokeColor.g)
        colorJsonObject.addProperty("b", src.strokeColor.b)
        colorJsonObject.addProperty("alpha", src.strokeColor.alpha)
        jsonObject.add("strokeColor", colorJsonObject)


        val pointsArray = JsonArray()
        for (p in src.points) {
            val offsetJsonObject = JsonObject()
            offsetJsonObject.addProperty("x", p.x)
            offsetJsonObject.addProperty("y", p.y)
            pointsArray.add(offsetJsonObject)
        }
        jsonObject.add("points", pointsArray)

        return jsonObject
    }

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): PathData {
        val jsonObject = json.asJsonObject
        val pathWrapper = PathData(
            actionType = ActionType.valueOf(jsonObject.get("actionType").asString),
            points = jsonObject.getAsJsonArray("points").map {
                Point(it.asJsonObject.get("x").asFloat, it.asJsonObject.get("y").asFloat)
            },
            strokeColor = jsonObject.getAsJsonObject("strokeColor").let { colorJsonObject ->
                StrokeColor(
                    colorJsonObject.get("r").asInt,
                    colorJsonObject.get("g").asInt,
                    colorJsonObject.get("b").asInt,
                    colorJsonObject.get("alpha").asInt
                )
            },
            strokeWidth = jsonObject.get("strokeWidth").asFloat,
        )
        return pathWrapper
    }
}