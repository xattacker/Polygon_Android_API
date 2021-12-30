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
import kotlin.math.max
import kotlin.math.min

class PolygonView : View
{
    var borderColor: Int = 0
    var highlightColor: Int = 0
    var titleColor: Int = 0
    var highlightMarkColor: Int = 0
    var titleFontSize: Float = 0f
    var fitToCenter: Boolean = true

    var listener: PolygonViewListener?
        get() = _listener?.get()
        set(listener)
        {
            if (listener != null)
            {
                _listener = WeakReference(listener)
            }
        }

    private var _regions: ArrayList<PolygonRegion>? = null
    private var _clickedRegion: PolygonRegion? = null
    private var _clickedMark: RegionMark? = null
    private var _listener: WeakReference<PolygonViewListener>? = null

    private lateinit var _paint: Paint
    private lateinit var _path: Path
    private var _baseWidth: Int = 0
    private var _baseHeight: Int = 0
    private var _ratioW: Float = 0f
    private var _ratioH: Float = 0f

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    {
        initView()
    }

    constructor(context: Context) : super(context)
    {
        initView()
    }

    fun loadMap(aMap: PolygonMap)
    {
        borderColor = aMap.borderColor
        highlightColor = aMap.highlightColor
        highlightMarkColor = aMap.highlightMarkColor
        titleColor = aMap.titleColor
        setBaseSize(aMap.width, aMap.height)

        _regions?.clear()

        if (!aMap.regions.isEmpty())
        {
            addRegions(aMap.regions)
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

    fun addRegions(aRegions: List<PolygonRegion>)
    {
        if (!aRegions.isEmpty())
        {
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

    fun setBaseSize(width: Int, height: Int)
    {
        _baseWidth = width
        _baseHeight = height
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isEnabled) {
            return false
        }


        val regions = _regions
        if (regions != null && !regions.isEmpty())
        {
            val point = PointF(event.x, event.y)

            when (event.action) {
                MotionEvent.ACTION_DOWN ->
                {
                    for (region in regions)
                    {
                        if (region.hasMark)
                        {
                            var hit = false

                            region.marks?.let {
                                for (mark in it)
                                {
                                    if (mark.isPointInMark(point))
                                    {
                                        _clickedMark = mark
                                        _clickedMark?._belongRegion = region
                                        invalidate()
                                        hit = true

                                        break
                                    }
                                }
                            }

                            if (hit)
                            {
                                break
                            }
                        }
                        else if (region.isPointInRegion(point))
                        {
                            _clickedRegion = region
                            invalidate()

                            break
                        }
                    }
                }

                MotionEvent.ACTION_UP ->
                {
                    if (_clickedMark != null)
                    {
                        _clickedMark?.let {
                            mark ->
                            mark._belongRegion?.let {
                                region ->
                                _listener?.get()?.onRegionMarkClicked(mark, region)
                            }
                        }
                    }
                    else if (_clickedRegion != null)
                    {
                        _clickedRegion?.let {
                            _listener?.get()?.onRegionClicked(it)
                        }
                    }

                    _clickedRegion = null
                    _clickedMark = null
                    invalidate()
                }

                MotionEvent.ACTION_CANCEL ->
                {
                    _clickedRegion = null
                    _clickedMark = null
                    invalidate()
                }
            }
        }

        return true
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int)
    {
        super.onLayout(changed, l, t, r, b)

        if (_baseWidth == 0 || _baseHeight == 0)
        {
            return
        }


        val ratio_w = width.toFloat() / _baseWidth.toFloat()
        val ratio_h = height.toFloat() / _baseHeight.toFloat()
        if (ratio_w != _ratioW || ratio_h != _ratioH)
        {
            _ratioW = ratio_w
            _ratioH = ratio_h

            var min_x = width.toFloat()
            var min_y = height.toFloat()
            var max_x = 0f
            var max_y = 0f
            val ratio = if (_ratioW > _ratioH) _ratioH else _ratioW

            _regions?.let {
                regions ->
                for (region in regions)
                {
                    region.points?.let {
                        points ->
                        for (point in points)
                        {
                            point.x *= ratio
                            point.y *= ratio

                            if (this.fitToCenter)
                            {
                                min_x = min(min_x, point.x)
                                max_x = max(max_x, point.x)

                                min_y = min(min_y, point.y)
                                max_y = max(max_y, point.y)
                            }
                        }

                        val point = region.titleInfo.position
                        if (point.x >= 0 && point.y >= 0)
                        {
                            point.x *= ratio
                            point.y *= ratio
                        }
                    }

                    region.marks?.let {
                        marks ->
                        for (mark in marks)
                        {
                            val point = mark.position
                            point.x *= ratio
                            point.y *= ratio
                        }
                    }
                }
            }

            if (this.fitToCenter)
            {
                val offset_x = width / 2 - (max_x + min_x) / 2
                val offset_y = height / 2 - (max_y + min_y) / 2
                if (offset_x != 0f || offset_y != 0f)
                {
                    fitCenter(offset_x, offset_y)
                }
            }
        }
    }

    override fun onDraw(canvas: Canvas)
    {
        val regions = _regions
        if (regions == null || regions.isEmpty())
        {
            return
        }


        for (region in regions)
        {
            val points = region.points
            if (points?.isEmpty() == false)
            {
                // draw region area
                _paint.style = Paint.Style.FILL
                _paint.color = region.regionColor
                drawPath(canvas, points)

                if (_clickedRegion == region)
                {
                    _paint.color = highlightColor
                    drawPath(canvas, points)
                }

                // draw region border
                _paint.style = Paint.Style.STROKE
                _paint.color = borderColor
                drawPath(canvas, points)

                if (region.titleInfo.title?.length ?: 0 > 0) {
                    // draw region title
                    var p = region.titleInfo.position
                    if (p.x < 0 && p.y < 0)
                    {
                        p = region.central
                    }

                    _paint.textAlign = Align.CENTER
                    _paint.color = titleColor
                    _paint.textSize = titleFontSize
                    canvas.drawText(region.titleInfo.title ?: "", p.x, p.y, _paint)
                }
            }


            region.marks?.let {
                marks ->
                // draw region mark
                _paint.style = Style.FILL
                _paint.textAlign = Align.CENTER
                _paint.textSize = titleFontSize

                for (mark in marks)
                {
                    _paint.color = if (_clickedMark != null && _clickedMark == mark) highlightMarkColor else mark.color?.color ?: Color.TRANSPARENT
                    canvas.drawCircle(mark.position.x, mark.position.y, RegionMark.MARK_RADIUS, _paint)

                    val title = mark.title
                    if ( title?.length ?: 0 > 0)
                    {
                        canvas.drawText(title ?: "", mark.position.x, mark.position.y + RegionMark.MARK_RADIUS * 2, _paint)
                    }
                }
            }
        }
    }

    private fun fitCenter(offsetX: Float, offsetY: Float)
    {
        _regions?.let {
            regions ->
            for (region in regions)
            {
                region.points?.let {
                    for (point in it)
                    {
                        point.x += offsetX
                        point.y += offsetY
                    }

                    val point = region.titleInfo.position
                    if (point.x >= 0 && point.y >= 0)
                    {
                        point.x += offsetX
                        point.y += offsetY
                    }
                }

                region.marks?.let {
                    marks ->
                    for (mark in marks)
                    {
                        val point = mark.position
                        point.x += offsetX
                        point.y += offsetY
                    }
                }
            }
        }
    }

    private fun drawPath(aCanvas: Canvas, aPoints: ArrayList<PointF>)
    {
        _path.reset()

        val point = aPoints[0]
        _path.moveTo(point.x, point.y) // first point

        for (p in aPoints)
        {
            _path.lineTo(p.x, p.y)
        }

        _path.close()

        aCanvas.drawPath(_path, _paint)
    }

    private fun initView()
    {
        _regions = ArrayList()
        _clickedRegion = null
        _clickedMark = null

        borderColor = Color.DKGRAY
        highlightColor = 0x77000000 and Color.LTGRAY
        highlightMarkColor = 0x77000000 and Color.DKGRAY
        titleColor = Color.BLACK
        titleFontSize = 20f
        _ratioH = 0f
        _ratioW = _ratioH
        _baseHeight = 0
        _baseWidth = _baseHeight
        _path = Path()
        fitToCenter = true

        _paint = Paint()
        _paint.isAntiAlias = true
        _paint.style = Paint.Style.FILL
        _paint.strokeWidth = 2f
    }
}
