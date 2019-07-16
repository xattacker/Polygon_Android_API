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

    fun isPointInMark(aPoint: PointF): Boolean
    {
        var hit = false
        val size = MARK_RADIUS * 2
        val tl = PointF(position.x - size, position.y - size)
        val br = PointF(position.x + size, position.y + size)

        if (aPoint.x >= tl.x && aPoint.x <= br.x &&
                aPoint.y >= tl.y && aPoint.y <= br.y)
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
