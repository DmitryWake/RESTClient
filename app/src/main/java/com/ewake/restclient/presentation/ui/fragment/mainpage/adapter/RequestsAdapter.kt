package com.ewake.restclient.presentation.ui.fragment.mainpage.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ewake.restclient.databinding.RequestResponseItemBinding
import com.ewake.restclient.presentation.model.RequestResponseModel

/**
 * @author Nikolaevsky Dmitry (@d.nikolaevskiy)
 */
class RequestsAdapter : RecyclerView.Adapter<RequestsAdapter.RequestViewHolder>() {

    inner class RequestViewHolder(private val binding: RequestResponseItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.container.setOnClickListener {
                onItemClickListener?.invoke(items[adapterPosition])
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(model: RequestResponseModel) {
            binding.apply {
                title.text = model.request.url
                description.text = "${model.response.code} ${model.response.message}"
            }
        }
    }

    var items: List<RequestResponseModel> = listOf()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onItemClickListener: ((RequestResponseModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return RequestViewHolder(RequestResponseItemBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: RequestViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}