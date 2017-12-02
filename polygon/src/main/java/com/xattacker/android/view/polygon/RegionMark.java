package com.xattacker.android.view.polygon;

import android.graphics.PointF;

import com.google.gson.annotations.SerializedName;

public final class RegionMark
{
	final static float MARK_RADIUS = 15f;

	@SerializedName("id")
	private String _id;
	
	@SerializedName("title")
	String _title;
	
	@SerializedName("position")
	PointF _position;
	
	@SerializedName("color")
	private RegionColor _color;
	
	PolygonRegion _belongRegion;
	
	public String getId()
	{
		return _id;
	}

	public void setId(String aId)
	{
		_id = aId;
	}

	public String getTitle()
	{
		return _title;
	}

	public void setTitle(String aTitle)
	{
		_title = aTitle;
	}

	public PointF getPosition()
	{
		return _position;
	}

	public void setPosition(PointF aPosition)
	{
		_position = aPosition;
	}

	public RegionColor getColor()
	{
		return _color;
	}

	public void setColor(RegionColor aColor)
	{
		_color = aColor;
	}
	
	public boolean isPointInMark(PointF aPoint)
   {
		 boolean hit = false;
		 float size = MARK_RADIUS*2;
		 PointF tl = new PointF(_position.x - size, _position.y - size);
		 PointF br = new PointF(_position.x + size, _position.y + size);
		 
		 if (
			 aPoint.x >= tl.x && aPoint.x <= br.x &&
			 aPoint.y >= tl.y && aPoint.y <= br.y
			 )
		 {
			 hit = true;
		 }
		 		 
		 return hit;
   }
}
