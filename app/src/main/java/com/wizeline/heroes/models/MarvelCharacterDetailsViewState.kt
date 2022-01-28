package com.wizeline.heroes.models

data class MarvelCharacterDetailsViewState(
    val characterDetails: List<IMarvelDetailsType> = ArrayList(),
    val series: List<SeriesDetails> = ArrayList(),
    val comics: List<ComicDetails> = ArrayList(),
    val isLoading: Boolean = false,
    val error: String? = ""
)
