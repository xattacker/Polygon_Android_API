package com.xattacker.android.view.polygon

import android.graphics.Color

class RegionColor {
    internal var _color = Color.TRANSPARENT

    val rgbStr: String
        get() = Integer.toHexString(_color)
}
