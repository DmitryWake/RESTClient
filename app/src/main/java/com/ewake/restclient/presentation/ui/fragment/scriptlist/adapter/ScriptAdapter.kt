package com.ewake.restclient.presentation.ui.fragment.scriptlist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ewake.restclient.data.db.room.entity.ScriptEntity
import com.ewake.restclient.databinding.ScriptItemBinding

class ScriptAdapter : RecyclerView.Adapter<ScriptAdapter.ScriptViewHolder>() {

    var items: MutableList<ScriptEntity> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onDeleteItemClickListener: ((scriptEntity: ScriptEntity) -> Unit)? = null
    var onItemClickListener: ((scriptEntity: ScriptEntity) -> Unit)? = null

    inner class ScriptViewHolder(private val binding: ScriptItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.delete.setOnClickListener {
                onDeleteItemClickListener?.invoke(items[adapterPosition])
            }

            binding.root.setOnClickListener {
                onItemClickListener?.invoke(items[adapterPosition])
            }
        }

        fun bind(item: ScriptEntity) {
            binding.title.text = item.name
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScriptViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ScriptItemBinding.inflate(inflater, parent, false)
        return ScriptViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ScriptViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun deleteItem(scriptEntity: ScriptEntity) {
        if (items.contains(scriptEntity)) {
            items.indexOf(scriptEntity).let { index ->
                items.removeAt(index)
                notifyItemRemoved(index)
            }
        }
    }

    fun addItem(scriptEntity: ScriptEntity) {
        items.add(scriptEntity)
        notifyItemInserted(items.size)
    }
}