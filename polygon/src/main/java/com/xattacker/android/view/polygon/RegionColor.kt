package com.xattacker.android.view.polygon

import android.graphics.Color

class RegionColor
{
    internal var color = Color.TRANSPARENT

    val rgbStr: String
        get() = Integer.toHexString(color)
}
