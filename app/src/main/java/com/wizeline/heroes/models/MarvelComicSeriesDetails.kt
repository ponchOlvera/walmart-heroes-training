package com.wizeline.heroes.models

import android.content.Context
import com.wizeline.heroes.R

sealed interface IMarvelDetailsType{
    fun getImagePath(): String
    fun getDescription(context: Context): String
}
sealed class MarvelDetailsType(): IMarvelDetailsType

data class ComicDetails(
    val comic: Comic
): MarvelDetailsType() {
    override fun getImagePath(): String {
        return "${comic.thumbnail.path}.${comic.thumbnail.extension}"
    }

    override fun getDescription(context: Context): String {
        if (comic.description.isEmpty()){
            return context.getString(R.string.description_not_available)
        }
        return comic.description
    }
}

data class SeriesDetails(
    val series: Series
): MarvelDetailsType() {
    override fun getImagePath(): String {
        return "${series.thumbnail.path}.${series.thumbnail.extension}"
    }

    override fun getDescription(context: Context): String {
        if (series.description.isEmpty()){
            return context.getString(R.string.description_not_available)
        }
        return series.description
    }
}