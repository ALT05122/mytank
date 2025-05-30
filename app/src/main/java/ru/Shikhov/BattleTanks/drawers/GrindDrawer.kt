package ru.Shikhov.BattleTanks.drawers

import android.graphics.Color
import android.view.View
import android.widget.FrameLayout
import ru.Shikhov.BattleTanks.activities.CELL_SIZE


class GridDrawer(private val container: FrameLayout?) {
    private val allLines = mutableListOf<View>()

    fun removeGrid() {
        allLines.forEach {
            container?.removeView(it)
        }
    }

    fun drawGrid() {
        drawHorizontalLines()
        drawVerticalLines()
    }


    private fun drawHorizontalLines() {
        var topMarqin = 0
        while (topMarqin <= container!!.height) {
            var horizontalLine = View(container.context)
            var layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, 1)
            topMarqin += CELL_SIZE
            layoutParams.topMargin = topMarqin
            horizontalLine.layoutParams = layoutParams
            horizontalLine.setBackgroundColor(Color.WHITE)
            allLines.add(horizontalLine)
            container.addView(horizontalLine)
        }
    }

    private fun drawVerticalLines() {
        var leftMarqin = 0
        while (leftMarqin <= container!!.width) {
            var verticalLine = View(container.context)
            var layoutParams = FrameLayout.LayoutParams(1, FrameLayout.LayoutParams.MATCH_PARENT)
            leftMarqin += CELL_SIZE
            layoutParams.leftMargin = leftMarqin
            verticalLine.layoutParams = layoutParams
            verticalLine.setBackgroundColor(Color.WHITE)
            allLines.add(verticalLine)
            container.addView(verticalLine)
        }
    }
}


