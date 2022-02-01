package com.wizeline.heroes.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.wizeline.heroes.ui.activities.MainActivity
import com.wizeline.heroes.R
import com.wizeline.heroes.databinding.FragmentDetailsBinding
import com.wizeline.heroes.getDescription
import com.wizeline.heroes.models.ComicDetails
import com.wizeline.heroes.models.IMarvelDetailsType
import com.wizeline.heroes.models.MarvelCharacterDetailsViewState
import com.wizeline.heroes.models.SeriesDetails
import com.wizeline.heroes.ui.CharacterItem
import com.wizeline.heroes.ui.CharacterMapper.mapCharacterForUi
import com.wizeline.heroes.ui.FragmentExtensionFunctions.dismissDialog
import com.wizeline.heroes.ui.FragmentExtensionFunctions.showDialog
import com.wizeline.heroes.ui.adapters.CharacterDetailsRecyclerAdapter
import com.wizeline.heroes.viewmodels.MarvelDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val mBinding: FragmentDetailsBinding get() = _binding!!

    private lateinit var comicDetailsAdapter: CharacterDetailsRecyclerAdapter
    private lateinit var seriesDetailsAdapter: CharacterDetailsRecyclerAdapter

    private val mMarvelViewModel: MarvelDetailsViewModel by viewModels()

    private lateinit var mCharacter: CharacterItem

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        val safeArgs: DetailsFragmentArgs by navArgs()
        mCharacter = mapCharacterForUi(safeArgs.marvelCharacter)
        init()
        return mBinding.root
    }

    private fun init(){
        setupViewModel()
        changeLoadingState(true)
        setupRecyclerViews()
        setDataToView()
        (activity as MainActivity).updateTitle(mCharacter.name)
    }

    private fun setupRecyclerViews() {
        comicDetailsAdapter = CharacterDetailsRecyclerAdapter(context = context!!)
        seriesDetailsAdapter = CharacterDetailsRecyclerAdapter(context = context!!)

        mBinding.rvComics.adapter = comicDetailsAdapter
        mBinding.rvSeries.adapter = seriesDetailsAdapter
    }

    private fun setDataToView() {
        val imgPath = mCharacter.imgPath

        mBinding.tvCharacterName.text = mCharacter.name
        mBinding.tvCharacterDescription.text = mCharacter.getDescription(context!!)

        Glide.with(context!!)
            .load(imgPath)
            .centerCrop()
            .placeholder(R.drawable.ic_superhero_placeholder)
            .into(mBinding.imageView)
    }

    private fun setupViewModel(){
        mMarvelViewModel.marvelDetailsViewState.observe(viewLifecycleOwner, {
            if (it != null) {
                renderViewState(it)
            }
        })

        mMarvelViewModel.getCharacterDetails(mCharacter.id)
    }

    private fun renderViewState(detailsViewState: MarvelCharacterDetailsViewState) {

        if (detailsViewState.comics.isNotEmpty()){
            addDataToRecyclerView(detailsViewState.comics)
            changeLoadingState(false)
        }

        if (detailsViewState.series.isNotEmpty()){
            addDataToRecyclerView(detailsViewState.series)
            changeLoadingState(false)
        }

        if (detailsViewState.error?.isNotEmpty() == true) {
            Toast.makeText(context, "Error detectado: ${detailsViewState.error}", Toast.LENGTH_LONG)
                .show()
            changeLoadingState(false)
        }

        if (detailsViewState.isLoading) {
            showDialog(context)
        } else {
            dismissDialog()
        }
    }

    private fun addDataToRecyclerView(dataList: List<IMarvelDetailsType>) {
        when(dataList.first()){
            is ComicDetails -> comicDetailsAdapter.addData(dataList)
            is SeriesDetails -> seriesDetailsAdapter.addData(dataList)
        }
    }

    private fun changeLoadingState(loading: Boolean) {
        mMarvelViewModel.setLoading(loading)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            DetailsFragment()
    }
}