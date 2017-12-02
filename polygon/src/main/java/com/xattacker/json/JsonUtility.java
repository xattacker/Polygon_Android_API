package com.xattacker.json;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import android.content.Context;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;

public final class JsonUtility
{
	public static Gson createGson(JsonBuilderVisitor aVisitor)
	{
	   GsonBuilder builder = new GsonBuilder();  
	   
	   if (aVisitor != null)
	   {
	   	aVisitor.onBuilderPrepared(builder);
	   }
		
	   // ignore expose field to serialize
	   builder.setExclusionStrategies
      (
          new ExclusionStrategy()
          {
              @Override
              public boolean shouldSkipField(FieldAttributes f)
              {
                  return f.getAnnotation(Expose.class) != null;
              }

              @Override
              public boolean shouldSkipClass(Class<?> clazz)
              {
                  return false;
              }
          }
      );
	   
	   return builder.create();   
	}
	
	public static Gson createGson()
	{
		return createGson(null);
	}
	
	public static <T> T fromJson(String aJson, TypeToken<T> aToken, JsonBuilderVisitor aVisitor)
	{
		Gson gson = createGson(aVisitor);
		return gson.fromJson(aJson, aToken.getType());
	}
	
	public static <T> T fromJson(String aJson, TypeToken<T> aToken)
	{
		return fromJson(aJson, aToken, null);
	}
	
	public static <T> T fromJson(String aJson, Class<T> aType, JsonBuilderVisitor aVisitor)
	{
		Gson gson = createGson(aVisitor);
		return gson.fromJson(aJson, aType);
	}
	
	public static <T> T fromJson(String aJson, Class<T> aType)
	{
		return fromJson(aJson, aType, null);
	}
	
	public static <T> T fromJsonRes
	(
	Context aContext, 
	int aRes, 
	Class<T> aType, 
	JsonBuilderVisitor aVisitor
	) throws Exception
	{
		T obj = null;
		ByteArrayOutputStream bout = null;
		InputStream in = null;
		
		try
		{
			in = aContext.getResources().openRawResource(aRes);
			bout = new ByteArrayOutputStream();

			byte[] buffer = new byte[256];
			int index = 0;
			
			while ((index = in.read(buffer)) != -1)
			{
				bout.write(buffer, 0, index);
				bout.flush();
			}
			
			buffer = null;

			obj = fromJson 
	      		(
	      		new String(bout.toByteArray()),
	      		aType,
	      		aVisitor
	      	   );
		}
		catch (Exception ex)
		{
			throw ex;
		}
		finally
		{
			if (in != null)
			{
				try
				{
					in.close();
				}
				catch (Exception ex)
				{
				}
				
				in = null;
			}
			
			if (bout != null)
			{
				try
				{
					bout.close();
				}
				catch (Exception ex)
				{
				}
				
				bout = null;
			}
		}

		return obj;
	}
	
	public static <T> T fromJsonRes
	(
	Context aContext, 
	int aRes, 
	Class<T> aType
	) throws Exception
	{
		return fromJsonRes(aContext, aRes, aType, null);
	}
	
	public static <T> T fromJsonRes
	(
	Context aContext, 
	int aRes, 
	TypeToken<T> aToken, 
	JsonBuilderVisitor aVisitor
	) throws Exception
	{
		T obj = null;
		ByteArrayOutputStream bout = null;
		InputStream in = null;
		
		try
		{
			in = aContext.getResources().openRawResource(aRes);
			bout = new ByteArrayOutputStream();

			byte[] buffer = new byte[256];
			int index = 0;
			
			while ((index = in.read(buffer)) != -1)
			{
				bout.write(buffer, 0, index);
				bout.flush();
			}
			
			buffer = null;

			obj = fromJson 
	      		(
	      		new String(bout.toByteArray()),
	      		aToken,
	      		aVisitor
	      	   );
		}
		catch (Exception ex)
		{
			throw ex;
		}
		finally
		{
			if (in != null)
			{
				try
				{
					in.close();
				}
				catch (Exception ex)
				{
				}
				
				in = null;
			}
			
			if (bout != null)
			{
				try
				{
					bout.close();
				}
				catch (Exception ex)
				{
				}
				
				bout = null;
			}
		}

		return obj;
	}
	
	public static <T> T fromJsonRes
	(
	Context aContext, 
	int aRes, 
	TypeToken<T> aToken
	) throws Exception
	{
		return fromJsonRes(aContext, aRes, aToken, null);
	}
}
