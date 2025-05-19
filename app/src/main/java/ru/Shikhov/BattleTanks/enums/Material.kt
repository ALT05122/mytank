package ru.Shikhov.BattleTanks.enums

enum class Material(
    val tankConGoThrough: Boolean,
    val bulletCanGoThread: Boolean,
    val simpleBulletCanDestroy: Boolean
) {
    EMPTY(true, true, true),
    BRICK(false, false, true),
    CONCRETE(false, false, false),
    GRASS(true,true,false),
    EAGLE(false,false,true),
}