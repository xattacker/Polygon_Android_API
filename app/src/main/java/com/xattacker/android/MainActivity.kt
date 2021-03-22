package com.xattacker.android

import android.graphics.Color
import android.graphics.PointF
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import com.xattacker.android.view.polygon.*

class MainActivity : AppCompatActivity(), PolygonViewListener
{
    private var _polygonView: PolygonView? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        try
        {
            _polygonView = this.findViewById<View>(R.id.view_polygon) as PolygonView
            _polygonView?.fitToCenter = true
            _polygonView?.listener = this

            // map from json resource
            val map = PolygonMap.parseFromJson(this, R.raw.region)
            map?.let {
                _polygonView?.loadMap(it)
            }
        }
        catch (ex: Exception)
        {
            ex.printStackTrace()
        }
    }

    fun onLoadRegionClick(aView: View)
    {
        // you could also add map region by code:
        _polygonView?.clearRegions()

        val region = PolygonRegion()
        region.regionColor = Color.YELLOW
        region.id = "north"
        region.title = "北部"
        region.addPoint(PointF(167f, 103f))
        region.addPoint(PointF(194f, 142f))
        region.addPoint(PointF(251f, 170f))
        region.addPoint(PointF(306f, 173f))
        region.addPoint(PointF(322f, 162f))
        region.addPoint(PointF(314f, 81f))
        region.addPoint(PointF(338f, 56f))
        region.addPoint(PointF(330f, 32f))
        region.addPoint(PointF(291f, 19f))
        region.addPoint(PointF(269f, 2f))
        region.addPoint(PointF(254f, 30f))
        region.addPoint(PointF(192f, 49f))

        _polygonView?.addRegion(region)
    }

    override fun onRegionClicked(aRegion: PolygonRegion)
    {
        android.util.Log.i("aaa", "onRegionClicked: " + (aRegion.title ?: ""))
    }

    override fun onRegionMarkClicked(aMark: RegionMark, aRegion: PolygonRegion)
    {
        android.util.Log.i("aaa", "onRegionMarkClicked: " + aMark.title + ", " + aRegion.title)
    }
}
