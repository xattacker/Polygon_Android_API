package com.xattacker.android.view.polygon;

import java.util.ArrayList;

import android.graphics.PointF;

import com.google.gson.annotations.SerializedName;

public final class PolygonRegion
{
	@SerializedName("id")
	private String _id;
	
	@SerializedName("title_info")
	TitleInfo _titleInfo;
	
	@SerializedName("color")
	private RegionColor _regionColor;
	
	@SerializedName("points")
	ArrayList<PointF> _points;
	
	@SerializedName("marks")
	ArrayList<RegionMark> _marks;
	
	public PolygonRegion()
	{
		_titleInfo = new TitleInfo();
		_regionColor = new RegionColor();
	   _points = new ArrayList<PointF>();
		_marks = new ArrayList<RegionMark>();
	}
	
	public String getId()
	{
		return _id;
	}
	
	public void setId(String aId)
	{
		_id = aId;
	}
	
	public void setTitle(String aTitle)
	{
		_titleInfo.title = aTitle;
	}
	
	public String getTitle()
	{
		return _titleInfo.title;
	}
	
	public void setTitlePosition(PointF aPosition)
	{
		_titleInfo._position = aPosition;
	}
	
	public int getRegionColor()
	{
		return _regionColor._color;
	}
	
	public void setRegionColor(int aColor)
	{
		_regionColor._color = aColor;
	}
	
	public void addPoint(PointF aPoint)
	{
		if (aPoint != null && _points != null)
		{
			_points.add(aPoint);
		}
	}

	public boolean isPointInRegion(PointF aPoint)
   {
		 boolean inside = false;
		 PointF point = _points.get(0);
		 int size = _points.size();
       double minX = point.x;
       double maxX = point.x;
       double minY = point.y;
       double maxY = point.y;
       
       for (int i = 1; i < size; i++)
       {
      	  point = _points.get(i);
           minX = Math.min(point.x, minX);
           maxX = Math.max(point.x, maxX);
           minY = Math.min(point.y, minY);
           maxY = Math.max(point.y, maxY);
       }

       if (aPoint.x < minX || aPoint.x > maxX || aPoint.y < minY || aPoint.y > maxY)
       {
           return false;
       }

       // http://www.ecse.rpi.edu/Homepages/wrf/Research/Short_Notes/pnpoly.html
       for (int i = 0, j = size - 1; i < size; j = i++)
       {
      	 PointF p1 = _points.get(i);
      	 PointF p2 = _points.get(j);
      	 
          if ( 
         	 (p1.y > aPoint.y) != (p2.y > aPoint.y) &&
         	 aPoint.x < (p2.x - p1.x) * (aPoint.y - p1.y) / (p2.y - p1.y) + p1.x 
             )
          {
         	 inside = !inside;
          }
       }

       return inside;
   }
	
	public void addMark(RegionMark aMark)
	{
		if (aMark != null && _marks != null)
		{
			_marks.add(aMark);
		}
	}
	
	public boolean hasMark()
	{
		return _marks != null && !_marks.isEmpty();
	}

	public PointF getCentral()  
	{
		 int size = _points.size();
	    float central_x = 0, central_y = 0;
	    
       for (PointF point : _points)
       {
          central_x += point.x;
          central_y += point.y;
       }
	        
	    return new PointF(central_x/size, central_y/size);
	}
	

	class TitleInfo
	{		
		@SerializedName("title")
		String title;
		
		@SerializedName("position")
		PointF _position;
		
		public TitleInfo()
		{
			_position = new PointF(-1, -1);
		}
	}
}
