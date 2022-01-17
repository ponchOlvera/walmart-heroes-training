package com.wizeline.heroes

import java.math.BigInteger
import java.security.MessageDigest

fun String.toMD5(): String {
    val digest = MessageDigest.getInstance("MD5")
    return BigInteger(1, digest.digest(toByteArray())).toString(16).padStart(32, '0')
}
