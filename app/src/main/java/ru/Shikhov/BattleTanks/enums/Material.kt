package ru.Shikhov.BattleTanks.enums

enum class Material(val tankConGoThrough: Boolean) {
    EMPTY(true),
    BRICK(false),
    CONCRETE(false),
    GRASS(true),
}