package com.mvproject.tvprogramguide.customlists

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mvproject.tvprogramguide.databinding.ItemSelectedChannelsBinding
import com.mvproject.tvprogramguide.model.data.Channel
import com.mvproject.tvprogramguide.utils.OnClickListener
import com.mvproject.tvprogramguide.utils.Utils.parseChannelName

class SelectedChannelsAdapter(private val onClickListener: OnClickListener<Channel>) :
    RecyclerView.Adapter<SelectedChannelsAdapter.ItemViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<Channel>() {
        override fun areItemsTheSame(
            oldItem: Channel,
            newItem: Channel
        ): Boolean {
            return oldItem.channelId == newItem.channelId
        }

        override fun areContentsTheSame(
            oldItem: Channel,
            newItem: Channel
        ): Boolean {
            return oldItem.channelName == newItem.channelName
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)
    var items: List<Channel>
        get() = differ.currentList
        set(value) {
            differ.submitList(value)
        }

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ItemSelectedChannelsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.binding.apply {
            val item = items[position]
            channelName.text = item.channelName.parseChannelName()

            btnDelete.setOnClickListener {
                onClickListener.onItemClick(item)
            }
        }
    }

    inner class ItemViewHolder(val binding: ItemSelectedChannelsBinding) :
        RecyclerView.ViewHolder(binding.root)
}
