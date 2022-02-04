package com.wizeline.heroes

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.wizeline.heroes.models.Character
import com.wizeline.heroes.models.ComicList
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttp
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

class TestUtils {
    companion object {
        const val SEARCH_QUERY = "Spiderman"
        private val emptyComicsList = ComicList(
            0, 0,
            StringUtils.STRING_EMPTY, listOf()
        )
        private val CHARACTERS_ERROR_RESPONSE = Characters(
            500,
            "Error not supported",
            "© 2022 MARVEL",
            "Data provided by Marvel. © 2022 MARVEL",
            "<a href=\"http://marvel.com\">Data provided by Marvel. © 2022 MARVEL</a>",
            "6b4fc8dd1bc3611bf4c689a62a915fb55d941b34",
            Data(0, 0, 0, 0, emptyList())
        )
        val RETROFIT_ERROR_RESPONSE: Response<Characters> = Response.error(
            CHARACTERS_ERROR_RESPONSE.toString()
                .toResponseBody("application/json; charset=utf-8".toMediaTypeOrNull()),
            okhttp3.Response.Builder()
                .code(500)
                .message("Error while doing the request")
                .protocol(Protocol.HTTP_1_1)
                .request(Request.Builder().url("http://localhost/").build())
                .build()
        )

        private val CHARACTERS_RESPONSE = Characters(
            200,
            "Ok",
            "© 2022 MARVEL",
            "Data provided by Marvel. © 2022 MARVEL",
            "<a href=\"http://marvel.com\">Data provided by Marvel. © 2022 MARVEL</a>",
            "6b4fc8dd1bc3611bf4c689a62a915fb55d941b34",
            Data(
                0,
                5,
                1559,
                5,
                listOf(
                    Character(
                        1011334,
                        "3-D Man",
                        StringUtils.STRING_EMPTY,
                        "2014-04-29T14:18:17-0400", emptyComicsList,
                        Thumbnail(
                            path = "http://i.annihil.us/u/prod/marvel/i/mg/c/e0/535fecbbb9784",
                            extension = "jpg"
                        ),
                        "http://gateway.marvel.com/v1/public/characters/1011334"
                    ),
                    Character(
                        1017100,
                        "A-Bomb (HAS)",
                        "Rick Jones has been Hulk's best bud since day one, but now he's more than a friend...he's a teammate! Transformed by a Gamma energy explosion, A-Bomb's thick, armored skin is just as strong and powerful as it is blue. And when he curls into action, he uses it like a giant bowling ball of destruction! ",
                        "2013-09-18T15:54:04-0400",
                        emptyComicsList,
                        Thumbnail(
                            path = "http://i.annihil.us/u/prod/marvel/i/mg/3/20/5232158de5b16",
                            extension = "jpg"
                        ),
                        "http://gateway.marvel.com/v1/public/characters/1017100"
                    ),
                    Character(
                        1009144,
                        "A.I.M.",
                        "AIM is a terrorist organization bent on destroying the world.",
                        "2013-10-17T14:41:30-0400",
                        emptyComicsList,
                        Thumbnail(
                            path = "http://i.annihil.us/u/prod/marvel/i/mg/6/20/52602f21f29ec",
                            extension = "jpg"
                        ),
                        "http://gateway.marvel.com/v1/public/characters/1009144"
                    ),
                    Character(
                        1010699,
                        "Aaron Stack",
                        "",
                        "1969-12-31T19:00:00-0500",
                        emptyComicsList,
                        Thumbnail(
                            path = "http://i.annihil.us/u/prod/marvel/i/mg/b/40/image_not_available",
                            extension = "jpg"
                        ),
                        "http://gateway.marvel.com/v1/public/characters/1010699"
                    ),
                    Character(
                        1009146,
                        "Abomination (Emil Blonsky)",
                        "Formerly known as Emil Blonsky, a spy of Soviet Yugoslavian origin working for the KGB, the Abomination gained his powers after receiving a dose of gamma radiation similar to that which transformed Bruce Banner into the incredible Hulk.",
                        "2012-03-20T12:32:12-0400",
                        emptyComicsList,
                        Thumbnail(
                            path = "http://i.annihil.us/u/prod/marvel/i/mg/9/50/4ce18691cbf04",
                            extension = "jpg"
                        ),
                        "http://gateway.marvel.com/v1/public/characters/1009146"
                    )
                )
            )
        )

        val RETROFIT_SUCCESS_RESPONSE: Response<Characters> = Response.success(CHARACTERS_RESPONSE)

        fun <T> LiveData<T>.getOrAwaitValue(
            time: Long = 5,
            timeUnit: TimeUnit = TimeUnit.SECONDS
        ): T {
            var data: T? = null
            val latch = CountDownLatch(1)
            val observer = object : Observer<T> {
                override fun onChanged(o: T?) {
                    data = o
                    latch.countDown()
                    this@getOrAwaitValue.removeObserver(this)
                }
            }

            this.observeForever(observer)

            // Don't wait indefinitely if the LiveData is not set.
            if (!latch.await(time, timeUnit)) {
                throw TimeoutException("LiveData value was never set.")
            }

            @Suppress("UNCHECKED_CAST")
            return data as T
        }
    }


}