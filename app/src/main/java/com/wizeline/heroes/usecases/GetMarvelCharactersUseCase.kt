package com.wizeline.heroes.usecases

import androidx.lifecycle.MutableLiveData
import com.wizeline.heroes.di.Retrofit
import com.wizeline.heroes.interfaces.IRepository
import com.wizeline.heroes.models.Character
import com.wizeline.heroes.models.MarvelViewState
import com.wizeline.heroes.toMD5
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class GetMarvelCharactersUseCase @Inject constructor(
    @Retrofit var repository: IRepository,
) {

    /** WIZELINE KEYS **/
    /*private val privateKey = "244be7c2496cbf6d331145cc489b4892457cc2c0"
    private val apikey = "6ffcf49b680b7250a6983acd33731f55"*/

    /** PERSONAL KEYS **/
    private val privateKey = "e6b903bda99ed895cd906b8f881e59388c611219"
    private val apikey = "1ace567a5fc6ae56303e8e340df61a16"
    private val timestamp = System.currentTimeMillis().toString()

    private val compositeDisposable = CompositeDisposable()

    // TODO: 24/01/22 Delete livedata instance and return single response to ViewModel.
    operator fun invoke(offset: Int, marvelMutableLiveData: MutableLiveData<MarvelViewState>) {
        val hash = (timestamp + privateKey + apikey).toMD5()
        // Use disposable to prevent observable memory leaks
        compositeDisposable.add(
            repository.getCharacters(timestamp, apikey, hash, offset).observeOn(
                AndroidSchedulers.mainThread()
            )
                .subscribeOn(Schedulers.io())
                .subscribe({
                    val marvelCharacterList = ArrayList<Character>()
                    it.data.results.forEach { character ->
                        marvelCharacterList.add(character)
                    }
                    marvelMutableLiveData.value = MarvelViewState(
                        characterList = marvelCharacterList,
                        isListUpdated = true
                    )
                }, {
                    marvelMutableLiveData.value = MarvelViewState(
                        error = it.message
                    )
                    it.printStackTrace()
                })
        )
    }

    fun clearCompositeDisposable() {
        compositeDisposable.clear()
    }
}