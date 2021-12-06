package com.ewake.restclient.presentation.ui.fragment.scriptdetail.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ewake.restclient.databinding.ScriptViewPagerItemBinding
import com.ewake.restclient.presentation.model.RequestResponseModel

class ScriptViewPagerAdapter : RecyclerView.Adapter<ScriptViewPagerAdapter.ScriptDataViewHolder>() {

    var items: MutableList<RequestResponseModel> = mutableListOf()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ScriptDataViewHolder(private val binding: ScriptViewPagerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(model: RequestResponseModel) {
            binding.requestResponseContainer.model = model
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScriptDataViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ScriptViewPagerItemBinding.inflate(inflater, parent, false)
        return ScriptDataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ScriptDataViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun deleteItem(position: Int) {
        notifyItemRemoved(position)
    }

    fun addItem() {
        notifyItemInserted(items.size)
    }
}