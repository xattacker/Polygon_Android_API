package com.xattacker.android.view.polygon;

import java.lang.reflect.Type;

import android.graphics.PointF;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

final class PointFParserSerializer implements JsonSerializer<PointF>, JsonDeserializer<PointF>
{
	@Override
	public PointF deserialize(JsonElement aElem, Type aArg1, JsonDeserializationContext aArg2) throws JsonParseException
	{ 
		JsonObject obj = (JsonObject) aElem;
		
		PointF point = new PointF(0, 0);
		point.x = obj.get("x").getAsFloat();
		point.y = obj.get("y").getAsFloat();
		
		return point;
	}

	@Override
	public JsonElement serialize(PointF aPoint, Type aArg1, JsonSerializationContext aArg2)
	{
		JsonObject obj = new JsonObject();
		obj.add("x", new JsonPrimitive(aPoint.x));
		obj.add("y", new JsonPrimitive(aPoint.y));
      
		return obj;
	}
}
