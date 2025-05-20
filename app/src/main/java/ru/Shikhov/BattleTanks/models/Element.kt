package ru.Shikhov.BattleTanks.models

import android.view.View
import ru.Shikhov.BattleTanks.enums.Material
data class Element constructor(
    val viewId: Int = View.generateViewId(),
    val material: Material,
    var coordinate: Coordinate,
    val width: Int,
    val height: Int,
){
}
