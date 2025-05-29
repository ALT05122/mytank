package ru.Shikhov.BattleTanks

import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaPlayer
import ru.Shikhov.BattleTanks.models.Bullet

class SoundManager(context: Context) {
    private var bulletBurstPlayer = MediaPlayer.create(context, R.raw.bullet_burst)
    private var bulletShotPlayer = MediaPlayer.create(context, R.raw.bullet_shot)
    private var introMusicPlayer = MediaPlayer.create(context, R.raw.tanks_pre_music)
    private var tankMovePlayerFirst = MediaPlayer.create(context, R.raw.tank_move_long)
    private var tankMovePlayerSecond = MediaPlayer.create(context, R.raw.tank_move_long)
    private var isIntroFinished = false

    init {
        prepareGapLessTankMoveSound()
    }

    private fun prepareGapLessTankMoveSound(){
        tankMovePlayerFirst.isLooping = true
        tankMovePlayerSecond.isLooping = true
        tankMovePlayerFirst.setNextMediaPlayer(tankMovePlayerSecond)
        tankMovePlayerSecond.setNextMediaPlayer(tankMovePlayerFirst)
    }
    fun playIntroMusic(){
        if(isIntroFinished){
            return
        }
        introMusicPlayer.setOnCompletionListener {
            isIntroFinished = true
        }
        introMusicPlayer.start()
    }
    fun pauseSounds(){
        bulletBurstPlayer.pause()
        bulletShotPlayer.pause()
        introMusicPlayer.pause()
        tankMovePlayerFirst.pause()
        tankMovePlayerSecond.pause()
    }
    fun bulletShot(){
        bulletShotPlayer.start()
    }
    fun BulletBurst(){
        bulletBurstPlayer.start()
    }
    fun tankMove(){
        tankMovePlayerFirst
    }
    fun tankStop(){
        if (tankMovePlayerFirst.isPlaying){
            tankMovePlayerFirst.pause()
        }
        if (tankMovePlayerSecond.isPlaying){
            tankMovePlayerSecond.pause()
        }
    }
}