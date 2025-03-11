package ru.Shikhov.BattleTanks

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.KeyEvent.KEYCODE_DPAD_DOWN
import android.view.KeyEvent.KEYCODE_DPAD_LEFT
import android.view.KeyEvent.KEYCODE_DPAD_RIGHT
import android.view.KeyEvent.KEYCODE_DPAD_UP
import ru.Shikhov.BattleTanks.Direction.UP
import ru.Shikhov.BattleTanks.Direction.DOWN
import ru.Shikhov.BattleTanks.Direction.LEFT
import ru.Shikhov.BattleTanks.Direction.RIGHT
import ru.Shikhov.BattleTanks.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

override fun onKeyDown(keyCode:Int,event: KeyEvent?):Boolean {
    when (keyCode){
        KEYCODE_DPAD_UP -> move(UP)
        KEYCODE_DPAD_DOWN -> move(DOWN)
        KEYCODE_DPAD_LEFT -> move(LEFT)
        KEYCODE_DPAD_RIGHT -> move(RIGHT)
    }
    return super.onKeyDown(keyCode, event)
}

private fun move(direction: Direction){
    when(direction){
        UP->{
        }
    }
    }
}