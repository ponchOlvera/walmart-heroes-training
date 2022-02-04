package com.wizeline.heroes.models

data class MarvelViewState(
    var characterList: List<Character> = ArrayList(),
    var error: String? = "",
    var enablePagination: Boolean = true,
    var clearRecyclerData: Boolean = false,
    var isLoading: Boolean = false,
    var isListUpdated: Boolean = false
)