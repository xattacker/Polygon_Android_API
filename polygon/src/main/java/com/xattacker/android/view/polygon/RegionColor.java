package com.xattacker.android.view.polygon;

import android.graphics.Color;

public final class RegionColor
{
	int _color = Color.TRANSPARENT;
	
	public String getRGBStr()
	{
       return Integer.toHexString(_color);
	}
}
