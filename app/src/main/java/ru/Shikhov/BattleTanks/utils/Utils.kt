package ru.Shikhov.BattleTanks.utils

import android.app.Activity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import ru.Shikhov.BattleTanks.activities.CELL_SIZE
import ru.Shikhov.BattleTanks.activities.binding
import ru.Shikhov.BattleTanks.models.Coordinate
import ru.Shikhov.BattleTanks.models.Element
import ru.Shikhov.BattleTanks.models.Tank

const val TOTAL_PERCENT = 100

fun View.checkViewCanMoveThroughBorder(coordinate: Coordinate): Boolean {
    return coordinate.top >= 0 &&
        coordinate.top + this.height <= binding.container.height  &&
        coordinate.left >= 0 &&
        coordinate.left + this.width <= binding.container.width
}

 fun getElementByCoordinates (
     coordinate: Coordinate,
     elementsOnContainer: List<Element>
): Element? {
     for (element in elementsOnContainer.toList()) {
         for (height in 0 until element.height){
             for (width in 0 until element.width){
                 val searchingCoordinate = Coordinate(
                     top = element.coordinate.top + height * CELL_SIZE,
                     left = element.coordinate.left + width * CELL_SIZE
                 )
                 if (coordinate == searchingCoordinate){
                     return element
                 }
             }
         }
     }
     return null
 }
fun getTankByCoordinates(coordinate: Coordinate, tankList: List<Tank>): Element?{
    return getElementByCoordinates(coordinate,tankList.map {it.element})
}

 fun Element.drawElement(container: FrameLayout){
    val view = ImageView(container.context)
    val layoutParams= FrameLayout.LayoutParams(
        this.material.width * CELL_SIZE,
        this.material.height * CELL_SIZE
    )
    this.material.image?.let { view.setImageResource(it) }
    layoutParams.topMargin = coordinate.top
    layoutParams.leftMargin = coordinate.left
    view.id = this.viewId
    view.layoutParams = layoutParams
    view.scaleType = ImageView.ScaleType.FIT_XY
    container.runOnUiThread{
        container.addView(view)
    }
}
fun  FrameLayout.runOnUiThread(block: () -> Unit){
    (this.context as Activity).runOnUiThread{
        block()
    }
}
fun checkIfChanceBiggerThanRandom(percentChance: Int): Boolean{
    return kotlin.random.Random.nextInt(TOTAL_PERCENT) <= percentChance
}

 fun View.getViewCoordinate():Coordinate{
    return  Coordinate(
        (this.layoutParams as FrameLayout.LayoutParams).topMargin,
        (this.layoutParams as FrameLayout.LayoutParams).leftMargin
    )
}