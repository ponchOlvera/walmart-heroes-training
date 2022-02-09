package com.wizeline.heroes.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wizeline.heroes.StringUtils.Companion.STRING_EMPTY
import com.wizeline.heroes.di.CoroutineDispatcherProvider
import com.wizeline.heroes.models.MarvelViewState
import com.wizeline.heroes.usecases.GetMarvelCharactersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
@HiltViewModel
class MarvelViewModel @Inject constructor(
    val getMarvelCharactersUseCase: GetMarvelCharactersUseCase,
    private val dispatcher: CoroutineDispatcherProvider
) :
    ViewModel() {

    private var _marvelViewState = MutableLiveData(MarvelViewState(arrayListOf(), "", false))
    private var searchQuery: String? = null
    private var clearRecyclerData: Boolean = false
    private var job: Job? = null
    val marvelViewState: LiveData<MarvelViewState> get() = _marvelViewState
    val searchDataFlow: Flow<MarvelViewState>

    /**
     * Processor of side effects from the UI which in turn feedback into [state]
     */
    val doUiAction: (UiAction) -> Unit

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
        requestError(throwable.message)
    }

    init {
        val actionStateFlow = MutableSharedFlow<UiAction>()
        val searchAction = actionStateFlow
            .filterIsInstance<UiAction.Search>()
            .onStart {
                MarvelViewState(
                    isLoading = true
                )
            }
            .distinctUntilChanged()

        searchDataFlow = searchAction
            .debounce(500)
            .flatMapLatest {
                setLoading(true)
                searchQuery = it.query
                getMarvelCharactersUseCase.asFlow(it.offset, it.query, it.timestamp, it.hash)
                    .mapLatest { response ->
                        val charactersList = response.data?.data?.results ?: listOf()
                        MarvelViewState(
                            characterList = charactersList,
                            clearRecyclerData = true,
                            isListUpdated = true,
                            enablePagination = true,
                            error = if (charactersList.isEmpty()) "No characters for custom search." else STRING_EMPTY
                        )
                    }
            }

        doUiAction = { uiAction ->
            viewModelScope.launch {
                actionStateFlow.emit(uiAction)
            }
        }
    }

    private fun requestError(message: String?) {
        _marvelViewState.value = MarvelViewState(
            error = message
        )
    }

    fun getCharacters(offset: Int, timestamp: String, hash: String) {
        job = CoroutineScope(dispatcher.name + exceptionHandler).launch {
            val response = getMarvelCharactersUseCase(offset, searchQuery, timestamp, hash)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    val marvelCharactersList = response.body()?.data?.results ?: listOf()
                    _marvelViewState.value = MarvelViewState(
                        characterList = marvelCharactersList,
                        clearRecyclerData = clearRecyclerData,
                        isListUpdated = true
                    )
                } else {
                    requestError(response.message())
                }
            }
        }
    }

    override fun onCleared() {
        job?.cancel()
        super.onCleared()
    }

    fun setLoading(loading: Boolean) {
        _marvelViewState.value = MarvelViewState(
            isLoading = loading
        )
    }

    fun listUpdated() {
        _marvelViewState.value = MarvelViewState(
            isListUpdated = false
        )
    }

    fun includeSearchQuery(query: String) {
        searchQuery = if (query.isNotEmpty()) query else null
        clearRecyclerData = true
    }

    fun togglePagination(enablePagination: Boolean) {
        _marvelViewState.value = MarvelViewState(
            enablePagination = enablePagination
        )
    }

    sealed class UiAction {
        data class Search(
            private val _query: String?,
            val offset: Int,
            val timestamp: String,
            val hash: String
        ) : UiAction() {
            val query: String?
                get() = if (_query.isNullOrEmpty()) null else _query
        }
    }

}