package ru.Shikhov.BattleTanks.models

import android.view.View
import ru.Shikhov.BattleTanks.enums.Direction

data class Bullet (
    val view: View,
    val direction: Direction,
    val tank: Tank,
    var canMoveFurther: Boolean = true
)
