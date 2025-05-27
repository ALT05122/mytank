package ru.Shikhov.BattleTanks.drawers

import ru.Shikhov.BattleTanks.utils.checkViewCanMoveThroughBorder
import android.app.Activity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import ru.Shikhov.BattleTanks.CELL_SIZE
import ru.Shikhov.BattleTanks.GameCore.isPlaying
import ru.Shikhov.BattleTanks.R
import ru.Shikhov.BattleTanks.enums.Direction
import ru.Shikhov.BattleTanks.enums.Material
import ru.Shikhov.BattleTanks.models.Bullet
import ru.Shikhov.BattleTanks.models.Coordinate
import ru.Shikhov.BattleTanks.models.Element
import ru.Shikhov.BattleTanks.models.Tank
import ru.Shikhov.BattleTanks.utils.getElementByCoordinates
import ru.Shikhov.BattleTanks.utils.getTankByCoordinates
import ru.Shikhov.BattleTanks.utils.getViewCoordinate
import ru.Shikhov.BattleTanks.utils.runOnUiThread
import kotlin.text.Typography.bullet

private const val BULLET_WIDTH = 15
private const val BULLET_HEIGHT = 15

class BulletDrawer(
    private val container: FrameLayout,
    private val elements: MutableList<Element>,
    private val enemyDrawer: EnemyDrawer

) {

    init {
        moveAllBullets()
    }

    private val allBullets = mutableListOf<Bullet>()

     fun addNewBulletForTank(tank: Tank){
        val view = container.findViewById<View>(tank.element.viewId) ?: return
        if (tank.alreadyHasBullet()) return
        allBullets.add(Bullet(createBullet(view, tank.direction), tank.direction, tank))
    }

    private fun Tank.alreadyHasBullet(): Boolean =
        allBullets.firstOrNull{ it.tank == this } != null

    private fun moveAllBullets() {
        Thread(Runnable{
            while (true){
                if (!isPlaying()){
                    continue
                }
                interactWithAllBullets()
                Thread.sleep(30)
            }
        }).start()
    }

    private fun interactWithAllBullets(){
        allBullets.toList().forEach{ bullet ->
            val view = bullet.view
            if (bullet.canBulletGoFurther()) {
                when (bullet.direction){
                    Direction.UP -> (view.layoutParams as FrameLayout.LayoutParams).topMargin -= BULLET_HEIGHT
                    Direction.DOWN -> (view.layoutParams as FrameLayout.LayoutParams).topMargin += BULLET_HEIGHT
                    Direction.LEFT -> (view.layoutParams as FrameLayout.LayoutParams).leftMargin -= BULLET_HEIGHT
                    Direction.RIGHT -> (view.layoutParams as FrameLayout.LayoutParams).leftMargin += BULLET_HEIGHT
                }
                chooseBehaviorInTermsOfDirections(bullet)
                container.runOnUiThread {
                    container.removeView(view)
                    container.addView(view)
                }
            } else{
                stopBullet(bullet)
            }
            bullet.stopIntersectingBullets()
        }
        removeInconsistentBullet()
    }
    private fun removeInconsistentBullet(){
        val removingList = allBullets.filter { !it.canMoveFurther }
        removingList.forEach{
            container.runOnUiThread {
                container.removeView(it.view)
            }
        }
        allBullets.removeAll(removingList)
    }

  //  private var canBulletGoFurther = true
  //  private var bulletThread: Thread? = null
   // private lateinit var tank: Tank

   // private fun checkBulletThreadDlive() = bulletThread != null && bulletThread!!.isAlive

    private fun Bullet.stopIntersectingBullets(){
        val bulletCoordinate = this.view.getViewCoordinate()
        for (bulletList in allBullets){
            val coordinateList = bulletList.view.getViewCoordinate()
            if (this == bulletList){
                continue
            }
            if (coordinateList == bulletCoordinate){
                stopBullet(this)
                stopBullet(bulletList)
                return
            }
        }
    }

    private fun Bullet.canBulletGoFurther() =
         this.view.checkViewCanMoveThroughBorder(this.view.getViewCoordinate())
            && this.canMoveFurther

    private fun chooseBehaviorInTermsOfDirections(bullet: Bullet){
        when (bullet.direction) {
                Direction.DOWN, Direction.UP -> {
                   compareCollections(getCoordinatesForTopOrBottomDirection(bullet), bullet)
                }

                Direction. LEFT, Direction. RIGHT -> {
                    compareCollections(getCoordinatesForTopOrBottomDirection(bullet), bullet)
                }
        }
    }

    private fun compareCollections(detectedCoordinatesList: List<Coordinate>, bullet: Bullet) {
        for (coordinate in detectedCoordinatesList){
            var element = getElementByCoordinates(coordinate, elements)
            if (element == null){
               element = getTankByCoordinates(coordinate, enemyDrawer.tanks)
            }
            if (element == bullet.tank.element){
                continue
            }
            removeElementsAndStopBullet(element, bullet)
        }
    }

    private fun removeElementsAndStopBullet(element: Element?, bullet: Bullet) {
        if (element != null) {
            if (element.material.bulletCanGoThrough) {
                return
            }
            if (bullet.tank.element.material == Material.ENEMY_TANK
                && element.material == Material.ENEMY_TANK
                ){
                stopBullet(bullet)
                return
            }
            if (element.material.simpleBulletCanDestroy){
                stopBullet(bullet)
                removeView(element)
                elements.remove(element)
                removeTank(element)
            }else{
                stopBullet(bullet)
            }
        }
    }

    private fun removeTank(element: Element) {
        val tanksElement = enemyDrawer.tanks.map { it.element }
        val tanksIndex = tanksElement.indexOf(element)
        enemyDrawer.removeTank(tanksIndex)
    }

    private fun stopBullet(bullet: Bullet){
        bullet.canMoveFurther = false
    }

    private fun removeView(element: Element?) {
        val activity = container.context as Activity
        activity.runOnUiThread{
                container.removeView(activity.findViewById(element!!.viewId))
        }
    }

    private fun getCoordinatesForTopOrBottomDirection (bullet: Bullet): List<Coordinate> {
        val bulletCoordinate = bullet.view.getViewCoordinate()
        val topCell = bulletCoordinate.top - bulletCoordinate.top % CELL_SIZE
        val bottomCell = topCell + CELL_SIZE
        val leftCoordinate = bulletCoordinate.left - bulletCoordinate.left % CELL_SIZE
        return listOf(
            Coordinate(topCell, leftCoordinate),
            Coordinate(bottomCell, leftCoordinate)
        )
    }

    private fun getCoordinatesForLeftOrRightDirection (bulletCoordinate: Coordinate): List<Coordinate> {
        val topCell = bulletCoordinate.top - bulletCoordinate.top % CELL_SIZE
        val bottomCell = topCell + CELL_SIZE
        val leftCoordinate = bulletCoordinate.left - bulletCoordinate.left % CELL_SIZE
                return listOf(
                    Coordinate(topCell, leftCoordinate),
                    Coordinate(bottomCell, leftCoordinate)
                )
    }


  private fun createBullet(myTank: View, currentDirection: Direction):ImageView{
        return ImageView(container.context)
            .apply {
                this.setImageResource(R.drawable.bullet)
                this.layoutParams = FrameLayout.LayoutParams(BULLET_WIDTH, BULLET_HEIGHT)
                val bulletCoordinate = getBulletCoordinates( this, myTank, currentDirection)
                (this.layoutParams as FrameLayout.LayoutParams).topMargin = bulletCoordinate.top
                (this.layoutParams as FrameLayout.LayoutParams).leftMargin = bulletCoordinate.left
                this.rotation = currentDirection.rotation
            }
    }
    private fun getBulletCoordinates(
        bullet: ImageView,
        myTank: View,
        currentDirection: Direction
    ): Coordinate{
        val tankLeftTopCoordinate = Coordinate(myTank.top, myTank.left)
        return when (currentDirection){
            Direction.UP -> Coordinate(
                    top = tankLeftTopCoordinate.top - bullet.layoutParams.height,
                    left = getDistanceToMiddleOfTank(
                        tankLeftTopCoordinate.left,bullet.layoutParams.width
                    )
            )
            Direction.DOWN -> Coordinate(
                top = tankLeftTopCoordinate.top + myTank.layoutParams.height,
                left = getDistanceToMiddleOfTank(
                    tankLeftTopCoordinate.left,bullet.layoutParams.width
                )
            )
            Direction.LEFT -> Coordinate(
                top = getDistanceToMiddleOfTank(
                tankLeftTopCoordinate.top, bullet.layoutParams.height
                ),
                left = tankLeftTopCoordinate.left - bullet.layoutParams.width
            )
            Direction.RIGHT -> Coordinate(
                top = getDistanceToMiddleOfTank(
                    tankLeftTopCoordinate.top, bullet.layoutParams.height
                ),
                left = tankLeftTopCoordinate.left + myTank.layoutParams.width
            )
        }
    }

    private fun getDistanceToMiddleOfTank(startCoordinate: Int, bulletSize: Int): Int{
        return  startCoordinate + (CELL_SIZE - bulletSize / 2)
    }
}