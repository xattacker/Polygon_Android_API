package com.xattacker.android.view.polygon;

import java.lang.reflect.Type;

import android.graphics.Color;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

final class RegionColorParserSerializer implements JsonSerializer<RegionColor>, JsonDeserializer<RegionColor>
{
	@Override
	public RegionColor deserialize(JsonElement aElem, Type aArg1, JsonDeserializationContext aArg2) throws JsonParseException
	{ 
		RegionColor color = new RegionColor();
		
		try
		{
			color._color = Color.parseColor(aElem.getAsString()); 
		}
		catch (Throwable th)
		{
		}
		
		return color;
	}

	@Override
	public JsonElement serialize(RegionColor aColor, Type aArg1, JsonSerializationContext aArg2)
	{
		return new JsonPrimitive("#" + aColor.getRGBStr());
	}
}
