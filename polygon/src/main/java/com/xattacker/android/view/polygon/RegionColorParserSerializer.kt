package com.xattacker.android.view.polygon

import java.lang.reflect.Type

import android.graphics.Color

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer

internal class RegionColorParserSerializer : JsonSerializer<RegionColor>, JsonDeserializer<RegionColor>
{
    @Throws(JsonParseException::class)
    override fun deserialize(aElem: JsonElement, aArg1: Type, aArg2: JsonDeserializationContext): RegionColor
    {
        val color = RegionColor()

        try
        {
            color._color = Color.parseColor(aElem.asString)
        }
        catch (th: Throwable)
        {
        }

        return color
    }

    override fun serialize(aColor: RegionColor, aArg1: Type, aArg2: JsonSerializationContext): JsonElement
    {
        return JsonPrimitive("#" + aColor.rgbStr)
    }
}
