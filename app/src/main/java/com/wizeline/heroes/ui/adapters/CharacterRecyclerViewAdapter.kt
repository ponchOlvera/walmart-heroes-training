package com.wizeline.heroes.ui.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wizeline.heroes.R
import com.wizeline.heroes.models.Character
import java.util.*
import kotlin.collections.ArrayList

class CharacterRecyclerViewAdapter(
    private val list: ArrayList<Character>,
    private val context: Context
) :
    RecyclerView.Adapter<CharacterRecyclerViewAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgCharacter: ImageView = view.findViewById(R.id.imgCharacter)
        val tvCharacterName: TextView = view.findViewById(R.id.tvCharacterName)
        val tvCharacterDescription: TextView = view.findViewById(R.id.tvCharacterDescription)
        val tvAvailableComics: TextView = view.findViewById(R.id.tvAvailableComics)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.recycler_view_character_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val character = list.get(position)
        val description =
            if (character.description.isEmpty()) context.getString(R.string.description_not_available) else character.description
        val imagePath = "${character.thumbnail.path}.${character.thumbnail.extension}"
        Log.w("onBindViewHolder()", imagePath)
        holder.tvCharacterName.text = character.name
        holder.tvCharacterDescription.text = description
        holder.tvAvailableComics.text = String.format(
            Locale.US,
            context.getString(R.string.available_comics),
            character.comics.available
        )

        //Set image
        Glide.with(context)
            .load(imagePath)
            .centerCrop()
            .placeholder(R.drawable.ic_superhero_placeholder)
            .into(holder.imgCharacter)
    }

    override fun getItemCount(): Int = list.size

    fun addData(list: List<Character>) {
        val size = this.list.size
        this.list.addAll(list)
        val sizeNew = this.list.size
        notifyItemRangeChanged(size, sizeNew)
    }

}