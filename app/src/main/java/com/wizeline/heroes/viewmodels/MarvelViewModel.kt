package com.wizeline.heroes.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wizeline.heroes.Characters
import com.wizeline.heroes.interfaces.IRepository
import com.wizeline.heroes.models.Character
import com.wizeline.heroes.models.MarvelViewState
import com.wizeline.heroes.toMD5
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MarvelViewModel: ViewModel() {

    /** WIZELINE KEYS **/
    /*private val privateKey = "244be7c2496cbf6d331145cc489b4892457cc2c0"
    private val apikey = "6ffcf49b680b7250a6983acd33731f55"*/

    /** PERSONAL KEYS **/
    private val privateKey = "e6b903bda99ed895cd906b8f881e59388c611219"
    private val apikey = "1ace567a5fc6ae56303e8e340df61a16"
    private val ts = System.currentTimeMillis().toString()
    private val compositeDisposable = CompositeDisposable()

    private var _marvelViewState = MutableLiveData(MarvelViewState(arrayListOf(), "", false))

    val marvelViewState get() = _marvelViewState

    fun getCharacters(repository: IRepository, offset: Int){

        val hash = (ts + privateKey + apikey).toMD5()

        // Use disposable to prevent observable memory leaks
        compositeDisposable.add(repository.getCharacters(ts, apikey, hash, offset).observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                val currentViewState = marvelViewState.value!!
                val marvelCharacterList = ArrayList<Character>()
                it.data.results.forEach {
                    marvelCharacterList.add(it)
                }
                currentViewState.characterList = marvelCharacterList
                currentViewState.isListUpdated = true
                _marvelViewState.value = currentViewState
            },{
                val currentViewState = marvelViewState.value!!
                currentViewState.error = it.message!!
                _marvelViewState.value = currentViewState
                it.printStackTrace()
            }))
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

    fun setLoading(loading: Boolean) {
        val currentState = marvelViewState.value!!
        currentState.isLoading = loading
        _marvelViewState.value = currentState
    }

    fun listUpdated() {
        val currentState = marvelViewState.value!!
        currentState.isListUpdated = false
        _marvelViewState.value = currentState
    }

}