package ru.Shikhov.BattleTanks.drawers

import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import ru.Shikhov.BattleTanks.CELL_SIZE
import ru.Shikhov.BattleTanks.binding
import ru.Shikhov.BattleTanks.enums.Direction
import ru.Shikhov.BattleTanks.enums.Material
import ru.Shikhov.BattleTanks.models.Coordinate
import ru.Shikhov.BattleTanks.models.Element
import ru.Shikhov.BattleTanks.utils.getElementByCoordinates




class ElementsDrawer(val container: FrameLayout) {
    var currentMaterial = Material.EMPTY
    val elementsOnContainer = mutableListOf<Element>()

    fun onTuchContainer(x: Float,y : Float){
        val topMargin = y.toInt() - (y.toInt() % CELL_SIZE)
        val leftMargin = x.toInt() - (x.toInt() % CELL_SIZE)
        val coordinate = Coordinate(topMargin, leftMargin)
        if (currentMaterial == Material.EMPTY){
            eraseView(coordinate)
        } else {
            drawOrReplaceView(coordinate)
        }
    }

    private fun drawOrReplaceView(coordinate: Coordinate){
        val viewOnCoordinate = getElementByCoordinates(coordinate,elementsOnContainer)
        if (viewOnCoordinate == null){
            drawView(coordinate)
            return
        }
        if (viewOnCoordinate.material != currentMaterial) {
            replaceView(coordinate)
        }
    }

    fun drawElementsList(elements: List<Element>?) {
        if (elements == null) {
            return
        }
        for (element in elements) {
            currentMaterial = element.material
            drawView((element.coordinate))
    }
}

    private fun replaceView(coordinate: Coordinate){
        eraseView(coordinate)
        drawView(coordinate)
    }

    private fun eraseView(coordinate: Coordinate){
        removeElement (getElementByCoordinates(coordinate,elementsOnContainer))
        for (element in getElementsUnderCurrentCoordinate(coordinate)){
            removeElement(element)
        }
    }

    private fun removeElement(element: Element?){
        if (element != null) {
            val erasingView = container.findViewById<View>(element.veiwId)
            container.removeView(erasingView)
            elementsOnContainer.remove(element)
        }
    }

    private fun getElementsUnderCurrentCoordinate (coordinate: Coordinate): List<Element> {
        val elements = mutableListOf<Element>()
        for (element in elementsOnContainer) {
            for (height in 0 until currentMaterial.height) {
                for (width in 0 until currentMaterial.width) {
                    if (element.coordinate == Coordinate(
                            coordinate.top + height * CELL_SIZE,
                            coordinate.left + width * CELL_SIZE

                    )
                ) {
                    elements.add(element)
                }
                }
            }
        }
        return elements
    }

    private fun removeIfSingleInstance() {
        elementsOnContainer.firstOrNull{it.material == Material.EAGLE}?.coordinate?.let{
            eraseView(it)
        }
    }
         private fun drawView(coordinate: Coordinate) {
             removeIfSingleInstance()
             val view = ImageView(container.context)
             val layoutParams= FrameLayout.LayoutParams(
                 currentMaterial.width * CELL_SIZE,
                 currentMaterial.height * CELL_SIZE
             )
             view.setImageResource(currentMaterial.image)
             layoutParams.topMargin = coordinate.top
             layoutParams.leftMargin = coordinate.left
             val viewId = View.generateViewId()
             val element = Element(
                 material = currentMaterial,
                 coordinate = coordinate,
                 width = currentMaterial.width,
                 height = currentMaterial.height
             )

             view.id = viewId
             view.layoutParams = layoutParams
             view.scaleType = ImageView.ScaleType.FIT_XY
             container.addView(view)
             elementsOnContainer.add(element)
         }

    fun move(myTank: View,direction: Direction){
        val layoutParams = myTank.layoutParams as FrameLayout.LayoutParams
        val currentCoordinate = Coordinate(layoutParams.topMargin, layoutParams.leftMargin)
        when (direction) {
            Direction.UP ->{
                myTank.rotation = 0f
                    (myTank.layoutParams as FrameLayout.LayoutParams).topMargin += -CELL_SIZE

            }

            Direction.DOWN -> {

                    myTank.rotation = 180f
                    (myTank.layoutParams as FrameLayout.LayoutParams).topMargin += CELL_SIZE

            }

            Direction.LEFT -> {
                    myTank.rotation = 270f
                    (myTank.layoutParams as FrameLayout.LayoutParams).leftMargin -= CELL_SIZE
                }

            Direction.RIGHT -> {
                    myTank.rotation = 90f
                    (myTank.layoutParams as FrameLayout.LayoutParams).leftMargin -= CELL_SIZE

                }
            }

                val nextCoordinate = Coordinate(layoutParams.topMargin, layoutParams.leftMargin)
            if (checkTankCanMoveThroughBorder(
                        nextCoordinate,
            myTank
                ) && checkTankCanMoveThroughMaterial(nextCoordinate)
                ) {

                binding.container.removeView(myTank)
                binding.container.addView(myTank)
            } else {
            (myTank.layoutParams as FrameLayout.LayoutParams).topMargin = currentCoordinate.top
                (myTank.layoutParams as FrameLayout.LayoutParams).leftMargin = currentCoordinate.left
        }
    }

    private fun checkTankCanMoveThroughMaterial (coordinate: Coordinate): Boolean {
        getTankCoordinates(coordinate).forEach {
            val element = getElementByCoordinates(it,elementsOnContainer)
            if (element != null && !element.material.tankConGoThrough) {
                return false
            }
        }
        return true
    }

    private fun checkTankCanMoveThroughBorder(coordinate: Coordinate, myTank: View): Boolean {
        if (coordinate.top >= 0 &&
            coordinate.top + myTank.height <= binding.container.height  &&
            coordinate.left >= 0 &&
            coordinate.left + myTank.width <= binding.container.width
            ) {
            return true
        }
        return false
    }

    private fun getTankCoordinates(topLeftCoordinate: Coordinate): List<Coordinate> {
        val coordinateList = mutableListOf<Coordinate>()
        coordinateList.add(topLeftCoordinate)
        coordinateList.add(Coordinate(topLeftCoordinate.top + CELL_SIZE, topLeftCoordinate.left))
        coordinateList.add(Coordinate(topLeftCoordinate.top, topLeftCoordinate.left + CELL_SIZE))
        coordinateList.add(
            Coordinate(
                topLeftCoordinate.top + CELL_SIZE,
                topLeftCoordinate.left + CELL_SIZE
            )
        )
        return coordinateList
    }
}