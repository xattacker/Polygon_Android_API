package com.xattacker.android.view.polygon

import android.content.Context
import android.graphics.PointF
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import com.xattacker.json.JsonBuilderVisitor
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
        get() = _borderColor._color
        set(aBorderColor) {
            _borderColor._color = aBorderColor
        }

    var highlightColor: Int
        get() = _highlightColor._color
        set(aHighlightColor) {
            _highlightColor._color = aHighlightColor
        }

    var highlightMarkColor: Int
        get() = _highlightMarkColor?._color ?: 0
        set(aHighlightColor) {
            _highlightMarkColor?._color = aHighlightColor
        }

    var titleColor: Int
        get() = _titleColor._color
        set(aTitleColor) {
            _titleColor._color = aTitleColor
        }

    init
    {
        _borderColor = RegionColor()
        _highlightColor = RegionColor()
        _titleColor = RegionColor()

        regions = ArrayList()
    }

    fun toJson(): String
    {
        val gson = JsonUtility.createGson(createBuilderAdapter())
        return gson.toJson(this)
    }

    companion object
    {
        @Throws(Exception::class)
        fun parseFromJson(aContext: Context, aRes: Int): PolygonMap? {

            return JsonUtility.fromJsonRes<PolygonMap>(
                    aContext,
                    aRes,
                    PolygonMap::class.java,
                    createBuilderAdapter()
            )
        }

        private fun createBuilderAdapter(): JsonBuilderVisitor
        {
            val visitor = object : JsonBuilderVisitor
                                {
                                    override fun onBuilderPrepared(aBuilder: GsonBuilder)
                                    {
                                        aBuilder.registerTypeAdapter(PointF::class.java, PointFParserSerializer())
                                        aBuilder.registerTypeAdapter(RegionColor::class.java, RegionColorParserSerializer())
                                    }
                                }

            return visitor
        }
    }
}
