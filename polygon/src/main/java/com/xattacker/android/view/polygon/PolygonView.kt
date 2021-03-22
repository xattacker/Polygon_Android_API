package com.xattacker.android.view.polygon

import android.content.Context
import android.graphics.*
import android.graphics.Paint.Align
import android.graphics.Paint.Style
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import java.lang.ref.WeakReference
import java.util.*

class PolygonView : View
{
    private var _regions: ArrayList<PolygonRegion>? = null
    private var _clickedRegion: PolygonRegion? = null
    private var _clickedMark: RegionMark? = null
    private var _listener: WeakReference<PolygonViewListener>? = null

    private var _borderColor: Int = 0
    private var _highlightColor: Int = 0
    private var _titleColor: Int = 0
    private var _highlightMarkColor: Int = 0
    private var _titleFontSize: Float = 0f
    private lateinit var _paint: Paint
    private lateinit var _path: Path
    private var _baseWidth: Int = 0
    private var _baseHeight: Int = 0
    private var _ratioW: Float = 0f
    private var _ratioH: Float = 0f
    private var _fitToCenter: Boolean = false

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {

        initView()
    }

    constructor(context: Context) : super(context) {

        initView()
    }

    fun setListener(aListener: PolygonViewListener) {
        _listener = WeakReference(aListener)
    }

    fun loadMap(aMap: PolygonMap?) {
        if (aMap != null) {
            _borderColor = aMap.borderColor
            _highlightColor = aMap.highlightColor
            _highlightMarkColor = aMap.highlightMarkColor
            _titleColor = aMap.titleColor
            setBaseSize(aMap.width, aMap.height)

            _regions?.clear()

            if (!aMap.regions.isEmpty()) {
                addRegions(aMap.regions)
            }
        }
    }

    fun addRegion(aRegion: PolygonRegion)
    {
        _regions?.add(aRegion)
        requestLayout()
        invalidate()
    }

    fun addRegion(vararg aRegions: PolygonRegion)
    {
        if (aRegions.size > 0)
        {
            addRegions(Arrays.asList(*aRegions))
        }
    }

    fun addRegions(aRegions: List<PolygonRegion>?) {
        if (aRegions != null && !aRegions.isEmpty()) {
            _regions?.addAll(aRegions)
            requestLayout()
            invalidate()
        }
    }

    fun clearRegions()
    {
        _regions?.clear()
        _ratioH = 1f
        _ratioW = _ratioH
        invalidate()
    }

    fun setBorderColor(aColor: Int) {
        _borderColor = aColor
    }

    fun setHighlightColor(aColor: Int) {
        _highlightColor = aColor
    }

    fun setTitleColor(aColor: Int) {
        _titleColor = aColor
    }

    fun setTitleFontSize(aSize: Float) {
        _titleFontSize = aSize
    }

    fun setBaseSize(aWidth: Int, aHeight: Int) {
        _baseWidth = aWidth
        _baseHeight = aHeight
    }

    fun setFitToCenter(aCenter: Boolean) {
        _fitToCenter = aCenter
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isEnabled) {
            return false
        }


        if (_regions != null && !_regions!!.isEmpty()) {
            val point = PointF(event.x, event.y)

            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    for (region in _regions!!) {
                        if (region.hasMark()) {
                            var hit = false

                            for (mark in region._marks!!) {
                                if (mark.isPointInMark(point)) {
                                    _clickedMark = mark
                                    _clickedMark?._belongRegion = region
                                    invalidate()
                                    hit = true

                                    break
                                }
                            }

                            if (hit) {
                                break
                            }
                        } else if (region.isPointInRegion(point)) {
                            _clickedRegion = region
                            invalidate()

                            break
                        }
                    }
                }

                MotionEvent.ACTION_UP -> {

                    if (_clickedMark != null)
                    {
                        _listener?.get()?.onRegionMarkClicked(_clickedMark!!, _clickedMark!!._belongRegion!!)
                    }
                    else if (_clickedRegion != null)
                    {
                        _listener?.get()?.onRegionClicked(_clickedRegion!!)
                    }

                    _clickedRegion = null
                    _clickedMark = null
                    invalidate()
                }

                MotionEvent.ACTION_CANCEL -> {
                    _clickedRegion = null
                    _clickedMark = null
                    invalidate()
                }
            }
        }

        return true
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)

        if (_baseWidth != 0 && _baseHeight != 0) {
            val ratio_w = width.toFloat() / _baseWidth.toFloat()
            val ratio_h = height.toFloat() / _baseHeight.toFloat()

            if (ratio_w != _ratioW || ratio_h != _ratioH) {
                _ratioW = ratio_w
                _ratioH = ratio_h

                var min_x = width.toFloat()
                var min_y = height.toFloat()
                var max_x = 0f
                var max_y = 0f
                val ratio = if (_ratioW > _ratioH) _ratioH else _ratioW

                for (region in _regions!!) {
                    if (!region._points!!.isEmpty()) {
                        for (point in region._points!!) {
                            point.x *= ratio
                            point.y *= ratio

                            if (_fitToCenter) {
                                if (min_x > point.x) {
                                    min_x = point.x
                                } else if (max_x < point.x) {
                                    max_x = point.x
                                }

                                if (min_y > point.y) {
                                    min_y = point.y
                                } else if (max_y < point.y) {
                                    max_y = point.y
                                }
                            }
                        }

                        val point = region._titleInfo._position
                        if (point.x >= 0 && point.y >= 0) {
                            point.x *= ratio
                            point.y *= ratio
                        }
                    }


                    if (region.hasMark())
                    {
                        for (mark in region._marks!!)
                        {
                            var point = mark.position
                            point.x *= ratio
                            point.y *= ratio
                        }
                    }
                }


                if (_fitToCenter) {
                    val offset_x = width / 2 - (max_x + min_x) / 2
                    val offset_y = height / 2 - (max_y + min_y) / 2

                    if (offset_x != 0f || offset_y != 0f) {
                        fitCenter(offset_x, offset_y)
                    }
                }
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        if (_regions?.isEmpty() == false) {
            for (region in _regions!!) {
                if (region._points?.isEmpty() == false) {
                    // draw region area
                    _paint.style = Paint.Style.FILL
                    _paint.color = region.regionColor
                    drawPath(canvas, region._points)

                    if (_clickedRegion != null && _clickedRegion == region)
                    {
                        _paint.color = _highlightColor
                        drawPath(canvas, region._points)
                    }

                    // draw region border
                    _paint.style = Paint.Style.STROKE
                    _paint.color = _borderColor
                    drawPath(canvas, region._points)

                    if (region._titleInfo.title != null && region._titleInfo.title!!.length > 0) {
                        // draw region title
                        var p = region._titleInfo._position
                        if (p.x < 0 && p.y < 0) {
                            p = region.central
                        }

                        _paint.textAlign = Align.CENTER
                        _paint.color = _titleColor
                        _paint.textSize = _titleFontSize
                        canvas.drawText(region._titleInfo.title!!, p.x, p.y, _paint)
                    }
                }


                if (region.hasMark()) {
                    // draw region mark
                    _paint.style = Style.FILL
                    _paint.textAlign = Align.CENTER
                    _paint.textSize = _titleFontSize

                    for (mark in region._marks!!)
                    {
                        _paint.color = if (_clickedMark != null && _clickedMark == mark) _highlightMarkColor else mark.color!!._color
                        canvas.drawCircle(mark.position.x, mark.position.y, RegionMark.MARK_RADIUS, _paint)

                        val title = mark.title
                        if (title != null && title.length > 0)
                        {
                            canvas.drawText(title, mark.position.x, mark.position.y + RegionMark.MARK_RADIUS * 2, _paint)
                        }
                    }
                }
            }
        }
    }

    private fun fitCenter(aOffsetX: Float, aOffsetY: Float)
    {
        for (region in _regions!!)
        {
            if (region._points?.isEmpty() == false)
            {
                var point: PointF?
                var i = 0
                val size = region._points?.size ?: 0

                while (i < size)
                {
                    point = region._points!![i]
                    point.x += aOffsetX
                    point.y += aOffsetY
                    i++
                }

                point = region._titleInfo._position
                if (point.x >= 0 && point.y >= 0)
                {
                    point.x += aOffsetX
                    point.y += aOffsetY
                }
            }


            if (region.hasMark())
            {
                for (mark in region._marks!!)
                {
                    var point = mark.position
                    point.x += aOffsetX
                    point.y += aOffsetY
                }
            }
        }
    }

    private fun drawPath(aCanvas: Canvas, aPoints: ArrayList<PointF>?) {
        _path.reset()

        var point = aPoints!![0]
        _path.moveTo(point.x, point.y) // first point

        var i = 1
        val size = aPoints.size
        while (i < size) {
            point = aPoints[i]
            _path.lineTo(point.x, point.y)
            i++
        }

        _path.close()

        aCanvas.drawPath(_path, _paint)
    }

    private fun initView()
    {
        _regions = ArrayList()
        _clickedRegion = null
        _clickedMark = null

        _borderColor = Color.DKGRAY
        _highlightColor = 0x77000000 and Color.LTGRAY
        _highlightMarkColor = 0x77000000 and Color.DKGRAY
        _titleColor = Color.BLACK
        _titleFontSize = 20f
        _ratioH = 0f
        _ratioW = _ratioH
        _baseHeight = 0
        _baseWidth = _baseHeight
        _path = Path()
        _fitToCenter = true

        _paint = Paint()
        _paint.isAntiAlias = true
        _paint.style = Paint.Style.FILL
        _paint.strokeWidth = 2f
    }
}
