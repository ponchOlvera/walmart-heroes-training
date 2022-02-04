package com.wizeline.heroes.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.wizeline.heroes.Endpoint
import com.wizeline.heroes.R
import com.wizeline.heroes.databinding.FragmentCharactersBinding
import com.wizeline.heroes.interfaces.OnItemClickListener
import com.wizeline.heroes.models.MarvelViewState
import com.wizeline.heroes.toMD5
import com.wizeline.heroes.ui.FragmentExtensionFunctions.dismissDialog
import com.wizeline.heroes.ui.FragmentExtensionFunctions.isInternetAvailable
import com.wizeline.heroes.ui.FragmentExtensionFunctions.showDialog
import com.wizeline.heroes.ui.abstract.PaginationScrollListener
import com.wizeline.heroes.ui.adapters.CharacterRecyclerViewAdapter
import com.wizeline.heroes.viewmodels.MarvelViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers

@AndroidEntryPoint
class CharactersFragment : Fragment() {

    private var _binding: FragmentCharactersBinding? = null
    private var _characterRecyclerViewAdapter: CharacterRecyclerViewAdapter? = null

    private val mBinding get() = _binding!!

    val mMarvelViewModel: MarvelViewModel by viewModels()
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
        mCharacterRecyclerViewAdapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(position: Int) {
                val selectedItem = mCharacterRecyclerViewAdapter.list[position]
                Log.i("onItemClick():", "Click on: $selectedItem")
                val action = CharactersFragmentDirections.actionCharactersFragmentToDetailsFragment(
                    selectedItem
                )
                findNavController().navigate(action)
            }

        })
    }

    private fun renderViewState(marvelViewState: MarvelViewState) {
        if (marvelViewState.isListUpdated) {

            if (mBinding.btnRetry.visibility == View.VISIBLE) mBinding.btnRetry.visibility =
                View.GONE

            mCharacterRecyclerViewAdapter.addData(marvelViewState.characterList)
            mMarvelViewModel.listUpdated()
            changeLoadingState(false)
        }
        if (marvelViewState.error?.isNotEmpty() == true) {
            Toast.makeText(context, "Error detectado: ${marvelViewState.error}", Toast.LENGTH_LONG)
                .show()
            mOffset -= OFFSET_INCREMENT
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
            val timestamp = System.currentTimeMillis().toString()
            val hash = (timestamp + Endpoint.PRIVATE_KEY + Endpoint.API_KEY).toMD5()
            mOffset += offset
            mMarvelViewModel.getCharacters(mOffset, timestamp, hash)
        } else {
            changeLoadingState(false)
            Toast.makeText(context, getString(R.string.no_interet_connection), Toast.LENGTH_LONG)
                .show()
            mBinding.btnRetry.visibility = View.VISIBLE
        }
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