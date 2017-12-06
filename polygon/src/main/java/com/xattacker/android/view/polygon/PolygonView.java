package com.xattacker.android.view.polygon;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class PolygonView extends View
{
	private ArrayList<PolygonRegion> _regions;
	private PolygonRegion _clickedRegion;
	private RegionMark _clickedMark;
	private WeakReference<PolygonViewListener> _listener;
	
	private int _borderColor, _highlightColor, _titleColor, _highlightMarkColor;
	private float _titleFontSize;
	private Paint _paint;
	private Path _path;
	private int _baseWidth, _baseHeight;
	private float _ratioW, _ratioH;
	private boolean _fitToCenter;

	public PolygonView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		
		initView();
	}
	
	public PolygonView(Context context)
	{
		super(context);

		initView();
	}
	
	public void setListener(PolygonViewListener aListener)
	{
		_listener = new WeakReference<PolygonViewListener>(aListener);
	}

	public void loadMap(PolygonMap aMap)
	{
		if (aMap != null)
		{
			_borderColor = aMap.getBorderColor();
			_highlightColor = aMap.getHighlightColor();
			_highlightMarkColor = aMap.getHighlightMarkColor();
			_titleColor = aMap.getTitleColor();
			setBaseSize(aMap.getWidth(), aMap.getHeight());
			
		   _regions.clear();
		   
		   if (aMap.getRegions() != null && !aMap.getRegions().isEmpty())
		   {
		   		addRegions(aMap.getRegions());
		   }
		}
	}

	public void addRegion(PolygonRegion aRegion)
	{
		if (aRegion != null)
		{
			_regions.add(aRegion);
			requestLayout();
			invalidate();
		}
	}

	public void addRegion(PolygonRegion... aRegions)
	{
		if (aRegions != null && aRegions.length > 0)
		{
			addRegions(Arrays.asList(aRegions));
		}
	}
	
	public void addRegions(List<PolygonRegion> aRegions)
	{
		if (aRegions != null && !aRegions.isEmpty())
		{
			_regions.addAll(aRegions);
			requestLayout();
			invalidate();
		}
	}

	public void clearRegions()
	{
		_regions.clear();
		_ratioW = _ratioH = 1;
		invalidate();
	}
	
	public void setBorderColor(int aColor)
	{
		_borderColor = aColor;
	}
	
	public void setHighlightColor(int aColor)
	{
		_highlightColor = aColor;
	}
	
	public void setTitleColor(int aColor)
	{
		_titleColor = aColor;
	}
	
	public void setTitleFontSize(float aSize)
	{
		_titleFontSize = aSize;
	}
	
	public void setBaseSize(int aWidth, int aHeight)
	{
		_baseWidth = aWidth;
		_baseHeight = aHeight;
	}
	
	public void setFitToCenter(boolean aCenter)
	{
		_fitToCenter = aCenter;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if (!isEnabled())
		{
			return false;
		}

		
		if (_regions != null && !_regions.isEmpty())
		{
			PointF point = new PointF(event.getX(), event.getY());

			switch (event.getAction())
			{
				case MotionEvent.ACTION_DOWN:
				{
					for (PolygonRegion region : _regions)
					{
						if (region.hasMark())
						{
							boolean hit = false;
							
							for (RegionMark mark : region._marks)
						   {
								if (mark.isPointInMark(point))
								{
									_clickedMark = mark;
									_clickedMark._belongRegion = region;
									invalidate();
									hit = true;
									
									break;
								}
						   }
							
							if (hit)
							{
								break;
							}
						}
						else if (region.isPointInRegion(point))
						{
							_clickedRegion = region;
							invalidate();
							
							break;
						}
					}
				}
					break;
				
				case MotionEvent.ACTION_UP:
				{
					if (_listener != null && _listener.get() != null)
					{
						if (_clickedMark != null)
						{
							_listener.get().onRegionMarkClicked(_clickedMark, _clickedMark._belongRegion);
						}
						else if (_clickedRegion != null)
						{
							_listener.get().onRegionClicked(_clickedRegion);
						}
					}
					
					_clickedRegion = null;
					_clickedMark = null;
					invalidate();
				}
					break;
					
				case MotionEvent.ACTION_CANCEL:
					_clickedRegion = null;
					_clickedMark = null;
					invalidate();
					break;
			}
		}
		
		return true;
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b)
	{
		super.onLayout(changed, l, t, r, b);

		if (_baseWidth != 0 && _baseHeight != 0)
		{
			float ratio_w = (float)getWidth()  / (float)_baseWidth;
			float ratio_h = (float)getHeight()  / (float)_baseHeight;

			if (ratio_w != _ratioW || ratio_h != _ratioH)
			{
				_ratioW = ratio_w;
				_ratioH = ratio_h;

				float min_x = getWidth(), min_y = getHeight(), max_x = 0, max_y = 0;
				float ratio = _ratioW > _ratioH ? _ratioH : _ratioW;

				for (PolygonRegion region : _regions)
				{
					if (!region._points.isEmpty())
					{
						for (PointF point : region._points)
						{
							point.x *= ratio;
							point.y *= ratio;

							if (_fitToCenter)
							{
								if (min_x > point.x)
								{
									min_x = point.x;
								}
								else if (max_x < point.x)
								{
									max_x = point.x;
								}

								if (min_y > point.y)
								{
									min_y = point.y;
								}
								else if (max_y < point.y)
								{
									max_y = point.y;
								}
							}
						}

						PointF point = region._titleInfo._position;
						if (point.x >= 0 && point.y >= 0)
						{
							point.x *= ratio;
							point.y *= ratio;
						}
					}


					if (region.hasMark())
					{
						PointF point = null;

						for (RegionMark mark : region._marks)
						{
							point = mark._position;
							point.x *= ratio;
							point.y *= ratio;
						}
					}
				}


				if (_fitToCenter)
				{
					float offset_x = getWidth()/2 - ((max_x + min_x)/2);
					float offset_y = getHeight()/2 - ((max_y + min_y)/2);

					if (offset_x != 0 || offset_y != 0)
					{
						fitCenter(offset_x, offset_y);
					}
				}
			}
		}
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		if (_regions != null && !_regions.isEmpty())
		{
			for (PolygonRegion region : _regions)
			{
				if (!region._points.isEmpty())
				{
					// draw region area
					_paint.setStyle(Paint.Style.FILL);
					_paint.setColor(region.getRegionColor());
					drawPath(canvas, region._points);
	
					if (_clickedRegion != null && _clickedRegion == region)
					{
						_paint.setColor(_highlightColor);
						drawPath(canvas, region._points);
					}
					
					// draw region border
					_paint.setStyle(Paint.Style.STROKE);
					_paint.setColor(_borderColor);
					drawPath(canvas, region._points);

					if (region._titleInfo.title != null && region._titleInfo.title.length() > 0)
					{
						// draw region title
						PointF p = region._titleInfo._position;
						if (p.x < 0 && p.y < 0)
						{
							 p = region.getCentral();
						}

						_paint.setTextAlign(Align.CENTER);
						_paint.setColor(_titleColor);
						_paint.setTextSize(_titleFontSize);
						canvas.drawText(region._titleInfo.title, p.x, p.y, _paint);
					}
				}
				
				
				if (region.hasMark())
				{
					// draw region mark
					_paint.setStyle(Style.FILL);
					_paint.setTextAlign(Align.CENTER);
					_paint.setTextSize(_titleFontSize);
					
					for (RegionMark mark : region._marks)
				   {
						_paint.setColor(_clickedMark != null && _clickedMark == mark ? _highlightMarkColor : mark.getColor()._color);
						canvas.drawCircle(mark._position.x, mark._position.y, RegionMark.MARK_RADIUS, _paint);
						
						if (mark._title != null && mark._title.length() > 0)
						{
							canvas.drawText(mark._title, mark._position.x, mark._position.y + RegionMark.MARK_RADIUS*2, _paint);
						}
				   }
				}
			}
		}
	}

	private void fitCenter(float aOffsetX, float aOffsetY)
	{
		for (PolygonRegion region : _regions)
		{
			if (!region._points.isEmpty())
			{
				PointF point = null;

				for (int i = 0, size = region._points.size(); i < size; i++)
				{
					point = region._points.get(i);
					point.x += aOffsetX;
					point.y += aOffsetY;
				}

				point = region._titleInfo._position;
				if (point.x >= 0 && point.y >= 0)
				{
					point.x += aOffsetX;
					point.y += aOffsetY;
				}
			}


			if (region.hasMark())
			{
				PointF point = null;

				for (RegionMark mark : region._marks)
				{
					point = mark._position;
					point.x += aOffsetX;
					point.y += aOffsetY;
				}
			}
		}
	}
	
	private void drawPath(Canvas aCanvas, ArrayList<PointF> aPoints)
	{	
		_path.reset();
		
		PointF point = aPoints.get(0);
		_path.moveTo(point.x, point.y); // first point
		
		for (int i = 1, size = aPoints.size(); i < size; i++)
		{
			point = aPoints.get(i);
			_path.lineTo(point.x, point.y);
		}
		
		_path.close();
		
		aCanvas.drawPath(_path, _paint);
	}
	
	private void initView()
	{
		_regions = new ArrayList<PolygonRegion>();
		_clickedRegion = null;
		_clickedMark = null;
		
		_borderColor = Color.DKGRAY;
		_highlightColor = 0x77000000 & Color.LTGRAY;
		_highlightMarkColor = 0x77000000 & Color.DKGRAY;
		_titleColor = Color.BLACK;
		_titleFontSize = 20;
		_ratioW = _ratioH = 0;
		_baseWidth = _baseHeight = 0;
		_path = new Path();
		_fitToCenter = true;
		 
		_paint = new Paint();
		_paint.setAntiAlias(true);
		_paint.setStyle(Paint.Style.FILL);
		_paint.setStrokeWidth(2);
	}
}
