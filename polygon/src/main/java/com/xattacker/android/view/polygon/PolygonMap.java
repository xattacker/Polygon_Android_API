package com.xattacker.android.view.polygon;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.PointF;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import com.xattacker.json.JsonBuilderVisitor;
import com.xattacker.json.JsonUtility;

public final class PolygonMap
{
	@SerializedName("width")
	private int _width;
	
	@SerializedName("height")
	private int _height;
	
	@SerializedName("border_color")
	private RegionColor _borderColor;
	
	@SerializedName("highlight_color")
	private RegionColor _highlightColor;
	
	@SerializedName("highlight_mark_color")
	private RegionColor _highlightMarkColor;
	
	@SerializedName("title_color")
	private RegionColor _titleColor;
	
	@SerializedName("regions")
	private ArrayList<PolygonRegion> _regions;
	
	public static PolygonMap parseFromJson(Context aContext, int aRes) throws Exception
	{
		PolygonMap map = JsonUtility.fromJsonRes
                       (
               		  aContext,  
               		  aRes,
                       PolygonMap.class,
                       createBuilderAdapter()
                       );
		
		return map;
	}
	
	private static JsonBuilderVisitor createBuilderAdapter()
	{
		return new JsonBuilderVisitor()
			    {
					@Override
					public void onBuilderPrepared(GsonBuilder aBuilder)
					{
						aBuilder.registerTypeAdapter(PointF.class, new PointFParserSerializer());
						aBuilder.registerTypeAdapter(RegionColor.class, new RegionColorParserSerializer());
					}
			    };
	}
	
	public PolygonMap()
	{
		_borderColor = new RegionColor();
		_highlightColor = new RegionColor();
		_titleColor = new RegionColor();
		
		_regions = new ArrayList<PolygonRegion>();
	}

	public int getWidth()
	{
		return _width;
	}

	public void setWidth(int aWidth)
	{
		_width = aWidth;
	}

	public int getHeight()
	{
		return _height;
	}

	public void setHeight(int aHeight)
	{
		_height = aHeight;
	}

	public int getBorderColor()
	{
		return _borderColor._color;
	}

	public void setBorderColor(int aBorderColor)
	{
		_borderColor._color = aBorderColor;
	}

	public int getHighlightColor()
	{
		return _highlightColor._color;
	}

	public void setHighlightColor(int aHighlightColor)
	{
		_highlightColor._color = aHighlightColor;
	}

	public void setHighlightMarkColor(int aHighlightColor)
	{
		_highlightMarkColor._color = aHighlightColor;
	}
	
	public int getHighlightMarkColor()
	{
		return _highlightMarkColor._color;
	}

	public int getTitleColor()
	{
		return _titleColor._color;
	}

	public void setTitleColor(int aTitleColor)
	{
		_titleColor._color = aTitleColor;
	}

	public ArrayList<PolygonRegion> getRegions()
	{
		return _regions;
	}
	
	public String toJson()
	{
		Gson gson = JsonUtility.createGson(createBuilderAdapter());
		return gson.toJson(this);
	}
}
