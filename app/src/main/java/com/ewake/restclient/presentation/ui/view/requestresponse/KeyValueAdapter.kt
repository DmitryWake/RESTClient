package com.ewake.restclient.presentation.ui.view.requestresponse

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.ewake.restclient.databinding.KeyValueItemBinding

/**
 * @author Nikolaevsky Dmitry (@d.nikolaevskiy)
 */
class KeyValueAdapter : RecyclerView.Adapter<KeyValueAdapter.KeyValueViewHolder>() {

    inner class KeyValueViewHolder(private val binding: KeyValueItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                key.doAfterTextChanged {
                    onItemDataChangedListener?.onChange(
                        adapterPosition,
                        (key.text.toString()) to (value.text.toString())
                    )
                }
                value.doAfterTextChanged {
                    onItemDataChangedListener?.onChange(
                        adapterPosition,
                        (key.text.toString()) to (value.text.toString())
                    )
                }
            }
        }

        fun bind(item: Pair<String, String>) {
            binding.apply {
                key.setText(item.first)
                value.setText(item.second)
            }
        }

    }

    var onItemDataChangedListener: OnKeyValueDataChangedListener? = null

    var items: MutableList<Pair<String, String>> = mutableListOf()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KeyValueViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return KeyValueViewHolder(KeyValueItemBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: KeyValueViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun addItem(pair: Pair<String, String>) {
        items.add(pair)
        notifyItemInserted(items.size)
    }

    fun removeAt(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun getItemCount(): Int = items.size

    fun interface OnKeyValueDataChangedListener {
        fun onChange(position: Int, data: Pair<String, String>)
    }
}