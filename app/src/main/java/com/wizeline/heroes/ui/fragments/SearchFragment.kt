package com.wizeline.heroes.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.wizeline.heroes.databinding.FragmentSearchBinding
import com.wizeline.heroes.models.MarvelCharacterDetailsViewState
import com.wizeline.heroes.viewmodels.MarvelDetailsViewModel

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val mBinding: FragmentSearchBinding get() = _binding!!

    var mMarvelViewModel: MarvelDetailsViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    private fun init(){
        setupViewModel()
    }

    private fun setupViewModel(){
        mMarvelViewModel?.marvelDetailsViewState?.observe(viewLifecycleOwner, {
            if (it != null) {
                renderViewState(it)
            }
        })

        //mMarvelViewModel?.getCharacterDetails()
    }

    private fun renderViewState(detailsViewState: MarvelCharacterDetailsViewState) {
        Toast.makeText(context, "Updated State: $detailsViewState", Toast.LENGTH_SHORT).show()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            SearchFragment()
    }
}