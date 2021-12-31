package com.mvproject.tvprogramguide.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mvproject.tvprogramguide.databinding.ItemCustomListBinding
import com.mvproject.tvprogramguide.database.entity.CustomListEntity
import com.mvproject.tvprogramguide.utils.OnItemClickListener

class CustomListAdapter(private val onClickListener: OnItemClickListener<CustomListEntity>) :
    RecyclerView.Adapter<CustomListAdapter.ItemViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<CustomListEntity>() {
        override fun areItemsTheSame(
            oldItem: CustomListEntity,
            newItem: CustomListEntity
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: CustomListEntity,
            newItem: CustomListEntity
        ): Boolean {
            return oldItem.name == newItem.name
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)
    var items: List<CustomListEntity>
        get() = differ.currentList
        set(value) {
            differ.submitList(value)
        }

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ItemCustomListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.binding.apply {
            val item = items[position]
            listName.text = item.name

            root.setOnClickListener {
                onClickListener.onItemClick(item)
            }
            btnDelete.setOnClickListener {
                onClickListener.onDeleteClick(item)
            }
        }
    }

    inner class ItemViewHolder(val binding: ItemCustomListBinding) :
        RecyclerView.ViewHolder(binding.root)
}
