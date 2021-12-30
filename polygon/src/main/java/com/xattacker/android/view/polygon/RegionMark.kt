package com.xattacker.android.view.polygon

import android.graphics.PointF

import com.google.gson.annotations.SerializedName

class RegionMark
{
    @SerializedName("id")
    var id: String? = null

    @SerializedName("title")
    var title: String? = null

    @SerializedName("position")
    var position: PointF = PointF()

    @SerializedName("color")
    var color: RegionColor? = null

    internal var _belongRegion: PolygonRegion? = null

    fun isPointInMark(point: PointF): Boolean
    {
        var hit = false
        val size = MARK_RADIUS * 2
        val tl = PointF(position.x - size, position.y - size)
        val br = PointF(position.x + size, position.y + size)

        if (point.x >= tl.x && point.x <= br.x &&
            point.y >= tl.y && point.y <= br.y)
        {
            hit = true
        }

        return hit
    }

    companion object
    {
        internal const val MARK_RADIUS = 15f
    }
}
