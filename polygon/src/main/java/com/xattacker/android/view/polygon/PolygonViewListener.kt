package com.xattacker.android.view.polygon

interface PolygonViewListener
{
    fun onRegionClicked(aRegion: PolygonRegion)
    fun onRegionMarkClicked(aMark: RegionMark, aRegion: PolygonRegion)
}
