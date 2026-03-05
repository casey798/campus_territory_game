package com.campus.territory.data

import android.graphics.Bitmap
import android.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap

object PlaceholderMapGenerator {

    fun generate(width: Int, height: Int): ImageBitmap {
        val pixels = IntArray(width * height)

        val grassA = Color.rgb(96, 153, 100)
        val grassB = Color.rgb(88, 142, 92)
        val pathColor = Color.rgb(184, 164, 122)
        val waterColor = Color.rgb(84, 139, 171)

        for (y in 0 until height) {
            val rowStart = y * width
            val tileY = y / 16
            for (x in 0 until width) {
                val tileX = x / 16
                val checker = (tileX + tileY) and 1
                var color = if (checker == 0) grassA else grassB

                if (((x / 64) + (y / 64)) % 7 == 0) {
                    color = pathColor
                }
                if (x in 1140..1510 && y in 40..280) {
                    color = waterColor
                }

                pixels[rowStart + x] = color
            }
        }

        drawBlock(pixels, width, height, left = 140, top = 110, blockWidth = 180, blockHeight = 130, fill = Color.rgb(151, 120, 90))
        drawBlock(pixels, width, height, left = 380, top = 210, blockWidth = 240, blockHeight = 150, fill = Color.rgb(164, 128, 94))
        drawBlock(pixels, width, height, left = 760, top = 160, blockWidth = 190, blockHeight = 120, fill = Color.rgb(157, 116, 92))
        drawBlock(pixels, width, height, left = 1000, top = 420, blockWidth = 300, blockHeight = 170, fill = Color.rgb(148, 113, 90))
        drawBlock(pixels, width, height, left = 180, top = 540, blockWidth = 260, blockHeight = 200, fill = Color.rgb(140, 108, 82))
        drawBlock(pixels, width, height, left = 580, top = 700, blockWidth = 220, blockHeight = 180, fill = Color.rgb(156, 122, 95))

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
        return bitmap.asImageBitmap()
    }

    private fun drawBlock(
        pixels: IntArray,
        width: Int,
        height: Int,
        left: Int,
        top: Int,
        blockWidth: Int,
        blockHeight: Int,
        fill: Int
    ) {
        val right = (left + blockWidth).coerceAtMost(width)
        val bottom = (top + blockHeight).coerceAtMost(height)
        val border = Color.rgb(68, 49, 33)
        val detail = Color.rgb(198, 182, 138)

        for (y in top until bottom) {
            val row = y * width
            for (x in left until right) {
                pixels[row + x] = fill
            }
        }

        for (x in left until right) {
            pixels[top * width + x] = border
            pixels[(bottom - 1) * width + x] = border
        }
        for (y in top until bottom) {
            pixels[y * width + left] = border
            pixels[y * width + (right - 1)] = border
        }

        var windowY = top + 10
        while (windowY + 6 < bottom) {
            var windowX = left + 10
            while (windowX + 8 < right) {
                if (((windowX / 12) + (windowY / 10)) % 3 == 0) {
                    paintRect(pixels, width, windowX, windowY, (windowX + 7).coerceAtMost(right), (windowY + 5).coerceAtMost(bottom), detail)
                }
                windowX += 16
            }
            windowY += 14
        }
    }

    private fun paintRect(
        pixels: IntArray,
        width: Int,
        left: Int,
        top: Int,
        right: Int,
        bottom: Int,
        color: Int
    ) {
        for (y in top until bottom) {
            val row = y * width
            for (x in left until right) {
                pixels[row + x] = color
            }
        }
    }
}
