package com.xattacker.android.view.polygon

import java.util.ArrayList

import android.graphics.PointF

import com.google.gson.annotations.SerializedName

class PolygonRegion {
    @SerializedName("id")
    var id: String? = null

    @SerializedName("title_info")
    internal var _titleInfo: TitleInfo

    @SerializedName("color")
    private val _regionColor: RegionColor

    @SerializedName("points")
    internal var _points: ArrayList<PointF>? = null

    @SerializedName("marks")
    internal var _marks: ArrayList<RegionMark>? = null

    var title: String?
        get() = _titleInfo.title
        set(aTitle) {
            _titleInfo.title = aTitle
        }

    var regionColor: Int
        get() = _regionColor._color
        set(aColor) {
            _regionColor._color = aColor
        }

    val central: PointF
        get() {
            val size = _points!!.size
            var central_x = 0f
            var central_y = 0f

            for (point in _points!!) {
                central_x += point.x
                central_y += point.y
            }

            return PointF(central_x / size, central_y / size)
        }

    init {
        _titleInfo = TitleInfo()
        _regionColor = RegionColor()
        _points = ArrayList()
        _marks = ArrayList()
    }

    fun setTitlePosition(aPosition: PointF) {
        _titleInfo._position = aPosition
    }

    fun addPoint(aPoint: PointF?) {
        if (aPoint != null && _points != null) {
            _points!!.add(aPoint)
        }
    }

    fun isPointInRegion(aPoint: PointF): Boolean {
        var inside = false
        var point = _points!![0]
        val size = _points!!.size
        var minX = point.x.toDouble()
        var maxX = point.x.toDouble()
        var minY = point.y.toDouble()
        var maxY = point.y.toDouble()

        for (i in 1 until size) {
            point = _points!![i]
            minX = Math.min(point.x.toDouble(), minX)
            maxX = Math.max(point.x.toDouble(), maxX)
            minY = Math.min(point.y.toDouble(), minY)
            maxY = Math.max(point.y.toDouble(), maxY)
        }

        if (aPoint.x < minX || aPoint.x > maxX || aPoint.y < minY || aPoint.y > maxY) {
            return false
        }

        // http://www.ecse.rpi.edu/Homepages/wrf/Research/Short_Notes/pnpoly.html
        var i = 0
        var j = size - 1
        while (i < size) {
            val p1 = _points!![i]
            val p2 = _points!![j]

            if (p1.y > aPoint.y != p2.y > aPoint.y && aPoint.x < (p2.x - p1.x) * (aPoint.y - p1.y) / (p2.y - p1.y) + p1.x) {
                inside = !inside
            }
            j = i++
        }

        return inside
    }

    fun addMark(aMark: RegionMark?) {
        if (aMark != null && _marks != null) {
            _marks!!.add(aMark)
        }
    }

    fun hasMark(): Boolean {
        return _marks != null && !_marks!!.isEmpty()
    }


    internal inner class TitleInfo {
        @SerializedName("title")
        var title: String? = null

        @SerializedName("position")
        var _position: PointF

        init {
            _position = PointF(-1f, -1f)
        }
    }
}
