package ru.Shikhov.BattleTanks

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.KeyEvent.*
import android.view.Menu
import android.view.MenuItem
import android.view.View.*
import ru.Shikhov.BattleTanks.enums.Direction.UP
import ru.Shikhov.BattleTanks.enums.Direction.DOWN
import ru.Shikhov.BattleTanks.enums.Direction.LEFT
import ru.Shikhov.BattleTanks.enums.Direction.RIGHT
import ru.Shikhov.BattleTanks.databinding.ActivityMainBinding
import ru.Shikhov.BattleTanks.drawers.BulletDrawer
import ru.Shikhov.BattleTanks.drawers.ElementsDrawer
import ru.Shikhov.BattleTanks.drawers.EnemyDrawer
import ru.Shikhov.BattleTanks.drawers.GridDrawer
import ru.Shikhov.BattleTanks.enums.Direction
import ru.Shikhov.BattleTanks.enums.Material
import ru.Shikhov.BattleTanks.models.Coordinate
import ru.Shikhov.BattleTanks.models.Element
import ru.Shikhov.BattleTanks.models.Tank

const val CELL_SIZE = 50

lateinit var binding: ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var editMode = false
    private val playerTank = Tank(
        Element(
            R.id.myTank,
            Material.PLAYER_TANK,
            Coordinate(0,0),
            Material.PLAYER_TANK.width,
            Material.PLAYER_TANK.height
        ),UP
    )
    private val gridDrawer by lazy{
        GridDrawer(binding.container)
    }

    private val elementsDrawer by lazy{
        ElementsDrawer(binding.container)
    }

    private val bulletDrawer by lazy {
        BulletDrawer(binding.container)
    }

    private val levelStorage by lazy {
        LevelStorage(this)
    }

    private val enemyDrawer by lazy {
        EnemyDrawer(binding.container, elementsDrawer.elementsOnContainer)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title="Menu"

        binding.editorClear.setOnClickListener { elementsDrawer.currentMaterial = Material.EMPTY }
        binding.editorBrick.setOnClickListener { elementsDrawer.currentMaterial = Material.BRICK }
        binding.editorConcrete.setOnClickListener {
            elementsDrawer.currentMaterial = Material.CONCRETE
        }
        binding.editorGrass.setOnClickListener { elementsDrawer.currentMaterial = Material.GRASS }
        binding.editorEagle.setOnClickListener { elementsDrawer.currentMaterial = Material.EAGLE }
        binding.container.setOnTouchListener { _, event ->
            elementsDrawer.onTuchContainer(event.x, event.y)
            return@setOnTouchListener true
        }
        elementsDrawer.drawElementsList(levelStorage.loadLevel())
        hideSettings()
        elementsDrawer.elementsOnContainer.add(playerTank.element)
    }

    private fun switchEditMode(){
        if (editMode){
            showSettings()
        }else{
            hideSettings()
        }
        editMode = !editMode
    }

    private fun showSettings(){
        gridDrawer.drawGrid()
        binding.materialsContainer.visibility = VISIBLE
    }

    private fun hideSettings(){
        gridDrawer.removeGrid()
        binding.materialsContainer.visibility = INVISIBLE
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.settings,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.menu_settings ->{
                gridDrawer.drawGrid()
                switchEditMode()
                return true
            }

            R.id.menu_save ->{
                levelStorage.saveLevel(elementsDrawer.elementsOnContainer)
                return true
            }

            R.id.menu_play -> {
                startTheGame()
                true
            }

            else ->super.onOptionsItemSelected(item)
        }
    }

    private fun startTheGame(){
        if (editMode){
            return
        }
        enemyDrawer.startEnemyCreation()
        enemyDrawer.moveEnemyTanks()
    }

override fun onKeyDown(keyCode:Int,event: KeyEvent?):Boolean {
    when (keyCode){
        KEYCODE_DPAD_UP -> move(UP)
        KEYCODE_DPAD_DOWN -> move(DOWN)
        KEYCODE_DPAD_LEFT -> move(LEFT)
        KEYCODE_DPAD_RIGHT -> move(RIGHT)
        KEYCODE_SPACE -> bulletDrawer.makeBulletMove(
            binding.myTank,
            playerTank.direction,
            elementsDrawer.elementsOnContainer
        )
    }
    return super.onKeyDown(keyCode, event)
}
    private fun move(direction: Direction){
        playerTank.move(direction, binding.container, elementsDrawer.elementsOnContainer)
    }
}