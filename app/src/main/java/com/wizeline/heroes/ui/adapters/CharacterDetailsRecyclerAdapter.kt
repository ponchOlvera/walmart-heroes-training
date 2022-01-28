package com.wizeline.heroes.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wizeline.heroes.R
import com.wizeline.heroes.databinding.ItemRecyclerViewDetailsBinding
import com.wizeline.heroes.models.Character
import com.wizeline.heroes.models.IMarvelDetailsType
import com.wizeline.heroes.ui.adapters.CharacterDetailsRecyclerAdapter.ViewHolder

class CharacterDetailsRecyclerAdapter(
    val list: ArrayList<IMarvelDetailsType> = ArrayList(),
    val context: Context) :
    ListAdapter<Character, ViewHolder>(DiffCallback()) {

    class ViewHolder(val binding: ItemRecyclerViewDetailsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(marvelDetails: IMarvelDetailsType, context: Context) {

            Glide.with(context)
                .load(marvelDetails.getImagePath())
                .centerCrop()
                .placeholder(R.drawable.ic_superhero_placeholder)
                .into(binding.imgDetail)
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<Character>() {
        override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemRecyclerViewDetailsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        with(holder) {
            with(list[position]) {
                bind(this, context)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun addData(list: List<IMarvelDetailsType>) {
        val size = this.list.size
        this.list.addAll(list)
        val sizeNew = this.list.size
        notifyItemRangeChanged(size, sizeNew)
    }
}