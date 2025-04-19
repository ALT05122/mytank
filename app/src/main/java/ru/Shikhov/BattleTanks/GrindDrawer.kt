package ru.Shikhov.BattleTanks

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.FrameLayout

class GridDrawer(private val context: Context) {
    private val allLines = mutableListOf<View>()

    fun removeGrid() {
        val container = binding.container
        allLines.forEach {
            container.removeView(it)
        }
    }

    fun drawGrid() {
        val container = binding.container
        drawHorizontalLines(container)
        drawVerticalLines(container)
    }


    private fun drawHorizontalLines(container: FrameLayout?) {
        var topMarqin = 0
        while (topMarqin <= container!!.height) {
            var horizontalLine = View(context)
            var layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, 1)
            topMarqin += CELL_SIZE
            layoutParams.topMargin = topMarqin
            horizontalLine.layoutParams = layoutParams
            horizontalLine.setBackgroundColor(Color.WHITE)
            allLines.add(horizontalLine)
            container.addView(horizontalLine)
        }
    }

    private fun drawVerticalLines(container: FrameLayout?) {
        var leftMarqin = 0
        while (leftMarqin <= container!!.width) {
            var verticalLine = View(context)
            var layoutParams =
                FrameLayout.LayoutParams(1, FrameLayout.LayoutParams.MATCH_PARENT)
            leftMarqin += CELL_SIZE
            layoutParams.leftMargin = leftMarqin
            verticalLine.layoutParams = layoutParams
            verticalLine.setBackgroundColor(Color.WHITE)
            allLines.add(verticalLine)
            container.addView(verticalLine)
        }
    }
}


