package com.ewake.restclient.presentation.ui.fragment.scriptdetail.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ewake.restclient.databinding.ScriptViewPagerItemBinding
import com.ewake.restclient.presentation.model.RequestMethod
import com.ewake.restclient.presentation.model.RequestModel
import com.ewake.restclient.presentation.model.RequestResponseModel
import com.ewake.restclient.presentation.ui.view.requestresponse.RequestResponseView
import com.google.android.material.slider.Slider

class ScriptViewPagerAdapter : RecyclerView.Adapter<ScriptViewPagerAdapter.ScriptDataViewHolder>() {

    var items: MutableList<RequestResponseModel> = mutableListOf()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onRequestSendClickListener: ((id: Int, requestModel: RequestModel) -> Unit)? = null

    inner class ScriptDataViewHolder(private val binding: ScriptViewPagerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.requestResponseContainer.onChangedListener =
                RequestResponseView.OnModelChangedListener {
                    items[adapterPosition] = it
                }

            binding.requestResponseContainer.onSendClickListener =
                RequestResponseView.OnSendRequestClickListener {
                    onRequestSendClickListener?.invoke(items[adapterPosition].id!!, it)
                }

        }

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