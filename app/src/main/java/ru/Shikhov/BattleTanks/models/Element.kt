package ru.Shikhov.BattleTanks.models

import android.view.View
import ru.Shikhov.BattleTanks.enums.Material
data class Element constructor(
    val veiwId: Int = View.generateViewId(),
    val material: Material,
    val coordinate: Coordinate,
    val width: Int,
    val height: Int,
){
}
