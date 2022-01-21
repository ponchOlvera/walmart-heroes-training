package com.wizeline.heroes.models

data class MarvelViewState(
    var characterList: ArrayList<Character> = ArrayList(),
    var error: String? = "",
    var isLoading: Boolean = false,
    var isListUpdated: Boolean = false
)