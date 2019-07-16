package com.xattacker.android.view.polygon

import java.lang.reflect.Type

import android.graphics.PointF

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer

internal class PointFParserSerializer : JsonSerializer<PointF>, JsonDeserializer<PointF> {
    @Throws(JsonParseException::class)
    override fun deserialize(aElem: JsonElement, aArg1: Type, aArg2: JsonDeserializationContext): PointF {
        val obj = aElem as JsonObject

        val point = PointF(0f, 0f)
        point.x = obj.get("x").asFloat
        point.y = obj.get("y").asFloat

        return point
    }

    override fun serialize(aPoint: PointF, aArg1: Type, aArg2: JsonSerializationContext): JsonElement {
        val obj = JsonObject()
        obj.add("x", JsonPrimitive(aPoint.x))
        obj.add("y", JsonPrimitive(aPoint.y))

        return obj
    }
}
