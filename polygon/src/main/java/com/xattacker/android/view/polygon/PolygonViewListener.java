package com.xattacker.android.view.polygon;

public interface PolygonViewListener
{
	void onRegionClicked(PolygonRegion aRegion);
	void onRegionMarkClicked(RegionMark aMark, PolygonRegion aRegion);
}
