package com.wizeline.heroes.models

import com.google.gson.annotations.SerializedName

data class ComicData(
    @SerializedName("code") val code: Int
) {

}