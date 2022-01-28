package com.wizeline.heroes

import android.content.Context
import com.wizeline.heroes.ui.CharacterItem
import java.math.BigInteger
import java.security.MessageDigest

fun String.toMD5(): String {
    val digest = MessageDigest.getInstance("MD5")
    return BigInteger(1, digest.digest(toByteArray())).toString(16).padStart(32, '0')
}

fun CharacterItem.getDescription(context: Context): String {
    return if (isDescriptionString()) this.description as String else context.getString(
        this.description as Int
    )
}
