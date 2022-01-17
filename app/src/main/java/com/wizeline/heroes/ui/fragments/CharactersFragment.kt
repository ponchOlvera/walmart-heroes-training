package com.wizeline.heroes.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.wizeline.heroes.R
import com.wizeline.heroes.databinding.FragmentCharactersBinding


class CharactersFragment : Fragment() {

    var _binding: FragmentCharactersBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCharactersBinding.inflate(inflater, container, false)
        Toast.makeText(this.context, "Fragment created", Toast.LENGTH_SHORT).show()
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            CharactersFragment()
    }
}