package com.wizeline.heroes.ui

import com.wizeline.heroes.R
import com.wizeline.heroes.Thumbnail
import com.wizeline.heroes.models.Character

object CharacterMapper {
    fun mapCharacterForUi(character: Character): CharacterItem{
        return CharacterItem(
            character.name,
            getDescription(character.description),
            character.comics.available,
            getImagePath(character.thumbnail)
        )
    }

    private fun getImagePath(thumbnail: Thumbnail): String {
        return thumbnail.path+"."+thumbnail.extension
    }

    // Return either the description(String) or the empty resource(Int).
    private fun getDescription(description: String):Any{
        return if (description.isEmpty()) R.string.description_not_available else description
    }
}

data class CharacterItem(
    val name: String,
    val description: Any,
    val numOfComics: Int,
    val imgPath: String
){
    fun isDescriptionString(): Boolean{
        if (description is String){
            return true
        }
        return false
    }
}
