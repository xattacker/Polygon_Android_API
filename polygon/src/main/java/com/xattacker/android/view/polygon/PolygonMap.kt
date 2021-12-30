package com.xattacker.android.view.polygon

import android.content.Context
import android.graphics.Color
import android.graphics.PointF
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import com.xattacker.json.JsonUtility
import java.util.*

class PolygonMap
{
    @SerializedName("width")
    var width: Int = 0

    @SerializedName("height")
    var height: Int = 0

    @SerializedName("border_color")
    private val _borderColor: RegionColor

    @SerializedName("highlight_color")
    private val _highlightColor: RegionColor

    @SerializedName("highlight_mark_color")
    private val _highlightMarkColor: RegionColor? = null

    @SerializedName("title_color")
    private val _titleColor: RegionColor

    @SerializedName("regions")
    val regions: ArrayList<PolygonRegion>

    var borderColor: Int
        get() = _borderColor.color
        set(aBorderColor) {
            _borderColor.color = aBorderColor
        }

    var highlightColor: Int
        get() = _highlightColor.color
        set(aHighlightColor)
        {
            _highlightColor.color = aHighlightColor
        }

    var highlightMarkColor: Int
        get() = _highlightMarkColor?.color ?: Color.TRANSPARENT
        set(aHighlightColor) {
            _highlightMarkColor?.color = aHighlightColor
        }

    var titleColor: Int
        get() = _titleColor.color
        set(aTitleColor) {
            _titleColor.color = aTitleColor
        }

    init
    {
        _borderColor = RegionColor()
        _highlightColor = RegionColor()
        _titleColor = RegionColor()

        regions = ArrayList()
    }

    fun toJson(): String {
        val gson = JsonUtility.createGson(createBuilderVisitor())
        return gson.toJson(this)
    }

    companion object
    {
        @Throws(Exception::class)
        fun parseFromJson(context: Context, aRes: Int): PolygonMap?
        {
            return JsonUtility.fromJsonRes<PolygonMap>(
                    context,
                    aRes,
                    PolygonMap::class.java,
                    createBuilderVisitor()
                    )
        }

        private fun createBuilderVisitor(): (builder: GsonBuilder) -> Unit
        {
            val visitor: (builder: GsonBuilder) -> Unit = {
                                builder: GsonBuilder ->
                                builder.registerTypeAdapter(PointF::class.java, PointFParserSerializer())
                                builder.registerTypeAdapter(RegionColor::class.java, RegionColorParserSerializer())
                            }

            return visitor
        }
    }
}
