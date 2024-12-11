package com.fadhly.gestura.ui.dictionary

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fadhly.gestura.data.local.SignDataClass
import com.fadhly.gestura.databinding.SignItemBinding

class SignLanguageAdapter : RecyclerView.Adapter<SignLanguageAdapter.SignViewHolder>() {
    inner class SignViewHolder(private val binding: SignItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val itemNow = listOfLanguage[position]
            Log.d("SignLanguageAdapter", "Binding item at position $position: ${itemNow.letter}")

            binding.tvLetter.text = itemNow.letter

            Glide
                .with(itemView.context)
                .load(itemNow.image)
                .fitCenter()
                .into(binding.ivImage)
        }

    }

    private var listOfLanguage = ArrayList<SignDataClass>()

    fun addSignList(list: List<SignDataClass>) {
        this.listOfLanguage.clear()
        this.listOfLanguage.addAll(list)
        Log.d("SignLanguageAdapter", "List size: ${listOfLanguage.size}")
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SignViewHolder {
        return SignViewHolder(
            SignItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SignViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return listOfLanguage.size
    }
}