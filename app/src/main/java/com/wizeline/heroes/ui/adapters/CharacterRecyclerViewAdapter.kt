package com.wizeline.heroes.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wizeline.heroes.R
import com.wizeline.heroes.models.Character
import com.wizeline.heroes.ui.CharacterMapper.mapCharacterForUi
import java.util.*
import kotlin.collections.ArrayList

class CharacterRecyclerViewAdapter(
    private val list: ArrayList<Character>,
    private val context: Context
) :
    ListAdapter<Character, CharacterRecyclerViewAdapter.ViewHolder>(DiffCallback()) {

    private class DiffCallback : DiffUtil.ItemCallback<Character>() {
        override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean {
            return oldItem == newItem
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val imgCharacter: ImageView = view.findViewById(R.id.imgCharacter)
        private val tvCharacterName: TextView = view.findViewById(R.id.tvCharacterName)
        private val tvCharacterDescription: TextView = view.findViewById(R.id.tvCharacterDescription)
        private val tvAvailableComics: TextView = view.findViewById(R.id.tvAvailableComics)

        fun bind(character: Character, context: Context) {
            val characterItem = mapCharacterForUi(character)
            val description: String =
                if (characterItem.isDescriptionString()) characterItem.description as String else context.getString(
                    characterItem.description as Int
                )
            tvCharacterName.text = character.name
            tvCharacterDescription.text = description
            tvAvailableComics.text = String.format(
                Locale.US,
                context.getString(R.string.available_comics),
                character.comics.available
            )

            //Set image
            Glide.with(context)
                .load(characterItem.imgPath)
                .centerCrop()
                .placeholder(R.drawable.ic_superhero_placeholder)
                .into(imgCharacter)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.recycler_view_character_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val character = list.get(position)
        holder.bind(character, context)
    }

    override fun getItemCount(): Int = list.size

    fun addData(list: List<Character>) {
        val size = this.list.size
        this.list.addAll(list)
        val sizeNew = this.list.size
        notifyItemRangeChanged(size, sizeNew)
    }

}