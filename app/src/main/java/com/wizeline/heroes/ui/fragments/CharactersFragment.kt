package com.wizeline.heroes.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.wizeline.heroes.RetrofitRepository
import com.wizeline.heroes.databinding.FragmentCharactersBinding
import com.wizeline.heroes.models.MarvelViewState
import com.wizeline.heroes.ui.ExtensionFunctions.dismissDialog
import com.wizeline.heroes.ui.ExtensionFunctions.showDialog
import com.wizeline.heroes.ui.abstract.PaginationScrollListener
import com.wizeline.heroes.ui.adapters.CharacterRecyclerViewAdapter
import com.wizeline.heroes.viewmodels.MarvelViewModel

class CharactersFragment : Fragment() {

    private var _binding: FragmentCharactersBinding? = null
    private var _marvelViewModel: MarvelViewModel? = null
    private var _characterRecyclerViewAdapter: CharacterRecyclerViewAdapter? = null

    private val mBinding get() = _binding!!
    private val mMarvelViewModel get() = _marvelViewModel!!
    private val mCharacterRecyclerViewAdapter get() = _characterRecyclerViewAdapter!!
    private val mRetrofitRepository = RetrofitRepository()
    private var mOffset: Int = 0
    private var isLastPage: Boolean = false

    private val OFFSET_INCREMENT = 20

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCharactersBinding.inflate(inflater, container, false)
        init()
        return mBinding.root
    }

    private fun init() {
        setupViewModel()
        changeLoadingState(true)
        setupRecyclerView()
        refreshCharacters()
    }

    private fun setupViewModel() {
        // Retrieving characters from retrofit
        _marvelViewModel = ViewModelProvider(this).get(MarvelViewModel::class.java)
        mMarvelViewModel.marvelViewState.observe(viewLifecycleOwner, {
            if (it != null) {
                renderViewState(it)
            }
        })
    }

    private fun setupRecyclerView() {

        mBinding.charactersRecyclerView.addOnScrollListener(object : PaginationScrollListener(
            mBinding.charactersRecyclerView.layoutManager as LinearLayoutManager
        ) {
            override fun isLastPage(): Boolean = isLastPage

            override fun isLoading(): Boolean = mMarvelViewModel.marvelViewState.value!!.isLoading

            override fun loadMoreItems() {
                Log.w(this::class.java.toString(), "LoadMoreItems")
                changeLoadingState(true)
                refreshCharacters(OFFSET_INCREMENT)
            }
        })
        _characterRecyclerViewAdapter = CharacterRecyclerViewAdapter(arrayListOf(), context!!)
        mBinding.charactersRecyclerView.adapter = mCharacterRecyclerViewAdapter
    }

    private fun renderViewState(marvelViewState: MarvelViewState) {
        if (marvelViewState.isListUpdated) {
            // TODO: 17/01/22 render the list into the recyclerView
            Log.i("renderViewState", "" + marvelViewState.characterList.size)
            mCharacterRecyclerViewAdapter.addData(marvelViewState.characterList)
            mMarvelViewModel.listUpdated()
            changeLoadingState(false)
        }
        if (marvelViewState.error.isNotEmpty()) {
            Toast.makeText(context, "Error detectado: ${marvelViewState.error}", Toast.LENGTH_SHORT)
                .show()
        }
        if (marvelViewState.isLoading) {
            showDialog(context)
        } else {
            dismissDialog()
        }
    }

    private fun refreshCharacters(offset: Int = 0) {
        mOffset += offset
        mMarvelViewModel.getCharacters(mRetrofitRepository, mOffset)
    }

    private fun changeLoadingState(loading: Boolean) {
        mMarvelViewModel.setLoading(loading)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            CharactersFragment()
    }
}