package ru.Shikhov.BattleTanks.models

import ru.Shikhov.BattleTanks.enums.Material
data class Element(
    val veiwId:Int,
    val material: Material,
    val coordinate: Coordinate
){
}
