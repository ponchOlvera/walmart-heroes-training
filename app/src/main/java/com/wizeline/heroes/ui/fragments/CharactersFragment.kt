package com.wizeline.heroes.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.wizeline.heroes.R
import com.wizeline.heroes.databinding.FragmentCharactersBinding
import com.wizeline.heroes.di.Retrofit
import com.wizeline.heroes.interfaces.IRepository
import com.wizeline.heroes.models.MarvelViewState
import com.wizeline.heroes.ui.FragmentExtensionFunctions.dismissDialog
import com.wizeline.heroes.ui.FragmentExtensionFunctions.isInternetAvailable
import com.wizeline.heroes.ui.FragmentExtensionFunctions.showDialog
import com.wizeline.heroes.ui.abstract.PaginationScrollListener
import com.wizeline.heroes.ui.adapters.CharacterRecyclerViewAdapter
import com.wizeline.heroes.usecases.GetMarvelCharactersUseCase
import com.wizeline.heroes.viewmodels.MarvelViewModel
import com.wizeline.heroes.viewmodels.MarvelViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CharactersFragment : Fragment() {

    private var _binding: FragmentCharactersBinding? = null
    private var _characterRecyclerViewAdapter: CharacterRecyclerViewAdapter? = null

    private val mBinding get() = _binding!!

    @Inject
    lateinit var marvelViewModelFactory: MarvelViewModelFactory
    var mMarvelViewModel: MarvelViewModel? = null
    private val mCharacterRecyclerViewAdapter get() = _characterRecyclerViewAdapter!!
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
        setupRetryButton()
        refreshCharacters()
    }

    private fun setupRetryButton() {
        mBinding.btnRetry.setOnClickListener {
            changeLoadingState(true)
            refreshCharacters(mOffset)
        }
    }

    private fun setupViewModel() {
        // Retrieving characters from retrofit
        val marvelViewModel: MarvelViewModel by viewModels {
            marvelViewModelFactory
        }
        mMarvelViewModel = marvelViewModel
        mMarvelViewModel?.marvelViewState?.observe(viewLifecycleOwner, {
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

            override fun isLoading(): Boolean = mMarvelViewModel?.marvelViewState?.value!!.isLoading

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

            if (mBinding.btnRetry.visibility == View.VISIBLE) mBinding.btnRetry.visibility =
                View.GONE

            mCharacterRecyclerViewAdapter.addData(marvelViewState.characterList)
            mMarvelViewModel?.listUpdated()
            changeLoadingState(false)
        }
        if (marvelViewState.error?.isNotEmpty() == true) {
            Toast.makeText(context, "Error detectado: ${marvelViewState.error}", Toast.LENGTH_LONG)
                .show()
            changeLoadingState(false)
        }
        if (marvelViewState.isLoading) {
            showDialog(context)
        } else {
            dismissDialog()
        }
    }

    private fun refreshCharacters(offset: Int = 0) {
        if (isInternetAvailable(context!!)) {
            mOffset += offset
            mMarvelViewModel?.getCharacters(mOffset)
        } else {
            changeLoadingState(false)
            Toast.makeText(context, getString(R.string.no_interet_connection), Toast.LENGTH_LONG)
                .show()
            mBinding.btnRetry.visibility = View.VISIBLE
        }
    }

    private fun changeLoadingState(loading: Boolean) {
        mMarvelViewModel?.setLoading(loading)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            CharactersFragment()
    }
}