package com.xattacker.android.view.polygon

import java.util.ArrayList

import android.graphics.PointF

import com.google.gson.annotations.SerializedName

class PolygonRegion {
    @SerializedName("id")
    var id: String? = null

    @SerializedName("title_info")
    internal var titleInfo: TitleInfo

    @SerializedName("color")
    private val _regionColor: RegionColor

    @SerializedName("points")
    internal var points: ArrayList<PointF>? = null

    @SerializedName("marks")
    internal var marks: ArrayList<RegionMark>? = null

    var title: String?
        get() = titleInfo.title
        set(aTitle)
        {
            titleInfo.title = aTitle
        }

    var regionColor: Int
        get() = _regionColor.color
        set(aColor) {
            _regionColor.color = aColor
        }

    val central: PointF
        get() {
            val size = points?.size ?: 0
            var central_x = 0f
            var central_y = 0f

            this.points?.let {
                for (point in it)
                {
                    central_x += point.x
                    central_y += point.y
                }
            }

            return PointF(central_x / size, central_y / size)
        }

    init
    {
        titleInfo = TitleInfo()
        _regionColor = RegionColor()
        points = ArrayList()
        marks = ArrayList()
    }

    fun setTitlePosition(position: PointF)
    {
        titleInfo.position = position
    }

    fun addPoint(point: PointF) {
        points?.add(point)
    }

    fun isPointInRegion(point: PointF): Boolean {

        val points = this.points
        if (points == null || points.isEmpty())
        {
            return false
        }


        var inside = false
        val size = points.size
        var temp_p = points[0]
        var minX = temp_p.x.toDouble()
        var maxX = temp_p.x.toDouble()
        var minY = temp_p.y.toDouble()
        var maxY = temp_p.y.toDouble()

        for (i in 1 until size)
        {
            temp_p = points[i]
            minX = Math.min(temp_p.x.toDouble(), minX)
            maxX = Math.max(temp_p.x.toDouble(), maxX)
            minY = Math.min(temp_p.y.toDouble(), minY)
            maxY = Math.max(temp_p.y.toDouble(), maxY)
        }

        if (point.x < minX || point.x > maxX || point.y < minY || point.y > maxY)
        {
            return false
        }

        // http://www.ecse.rpi.edu/Homepages/wrf/Research/Short_Notes/pnpoly.html
        var i = 0
        var j = size - 1
        while (i < size)
        {
            val p1 = points[i]
            val p2 = points[j]

            if (p1.y > point.y != p2.y > point.y &&
                point.x < (p2.x - p1.x) * (point.y - p1.y) / (p2.y - p1.y) + p1.x)
            {
                inside = !inside
            }

            j = i++
        }

        return inside
    }

    fun addMark(mark: RegionMark)
    {
        marks?.add(mark)
    }

    val hasMark: Boolean
     get() = marks?.isEmpty() == false


    internal inner class TitleInfo
    {
        @SerializedName("title")
        var title: String? = null

        @SerializedName("position")
        var position: PointF = PointF(-1f, -1f)
    }
}
