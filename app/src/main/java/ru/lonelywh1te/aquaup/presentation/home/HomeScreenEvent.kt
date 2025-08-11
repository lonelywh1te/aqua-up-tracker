package ru.lonelywh1te.aquaup.presentation.home

sealed class HomeScreenEvent {
    data class AddWater(val volume: Int): HomeScreenEvent()

}