package ru.lonelywh1te.aquaup.home

sealed class HomeScreenEvent {
    data class AddWater(val volume: Int): HomeScreenEvent()

}