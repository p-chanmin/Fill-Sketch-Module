package com.dev.philo.fillsketch.core.data.converter

import com.dev.philo.fillsketch.core.data.model.ActionType
import com.dev.philo.fillsketch.core.data.model.ColorSet
import com.dev.philo.fillsketch.core.data.model.Offset
import com.dev.philo.fillsketch.core.data.model.PathWrapper
import com.google.gson.JsonArray
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type

class OffsetTypeAdapter : JsonSerializer<Offset>, JsonDeserializer<Offset> {
    override fun serialize(src: Offset, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        val jsonObject = JsonObject()
        jsonObject.addProperty("x", src.x)
        jsonObject.addProperty("y", src.y)
        return jsonObject
    }

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Offset {
        val jsonObject = json.asJsonObject
        val x = jsonObject.get("x").asFloat
        val y = jsonObject.get("y").asFloat
        return Offset(x, y)
    }
}

class ColorSetTypeAdapter : JsonSerializer<ColorSet>, JsonDeserializer<ColorSet> {
    override fun serialize(src: ColorSet, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        val jsonObject = JsonObject()
        jsonObject.addProperty("r", src.r)
        jsonObject.addProperty("g", src.g)
        jsonObject.addProperty("b", src.b)
        return jsonObject
    }

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): ColorSet {
        val jsonObject = json.asJsonObject
        val r = jsonObject.get("r").asFloat
        val g = jsonObject.get("g").asFloat
        val b = jsonObject.get("b").asFloat
        return ColorSet(r, g, b)
    }
}

class ActionTypeTypeAdapter : JsonSerializer<ActionType>, JsonDeserializer<ActionType> {
    override fun serialize(src: ActionType, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        return JsonPrimitive(src.name)
    }

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): ActionType {
        return ActionType.valueOf(json.asString)
    }
}

class PathWrapperAdapter : JsonSerializer<PathWrapper>, JsonDeserializer<PathWrapper> {
    override fun serialize(src: PathWrapper, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        val jsonObject = JsonObject()
        jsonObject.addProperty("actionType", src.actionType.name)
        jsonObject.addProperty("strokeWidth", src.strokeWidth)
        jsonObject.addProperty("alpha", src.alpha)

        val colorJsonObject = JsonObject()
        colorJsonObject.addProperty("r", src.strokeColor.r)
        colorJsonObject.addProperty("g", src.strokeColor.g)
        colorJsonObject.addProperty("b", src.strokeColor.b)
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

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): PathWrapper {
        val jsonObject = json.asJsonObject
        val pathWrapper = PathWrapper(
            actionType = ActionType.valueOf(jsonObject.get("actionType").asString),
            points = jsonObject.getAsJsonArray("points").map {
                Offset(it.asJsonObject.get("x").asFloat, it.asJsonObject.get("y").asFloat)
            },
            strokeColor = jsonObject.getAsJsonObject("strokeColor").let { colorJsonObject ->
                ColorSet(colorJsonObject.get("r").asFloat, colorJsonObject.get("g").asFloat, colorJsonObject.get("b").asFloat)
            },
            strokeWidth = jsonObject.get("strokeWidth").asFloat,
            alpha = jsonObject.get("alpha").asFloat
        )
        return pathWrapper
    }
}