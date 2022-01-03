package com.mvproject.tvprogramguide.customlists

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import coil.transform.RoundedCornersTransformation
import com.mvproject.tvprogramguide.databinding.ItemSelectedChannelsBinding
import com.mvproject.tvprogramguide.model.data.Channel
import com.mvproject.tvprogramguide.utils.OnClickListener
import com.mvproject.tvprogramguide.utils.Utils.parseChannelName
import com.mvproject.tvprogramguide.utils.Utils.pxToDp

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
            channelLogo.apply {
                layoutParams.height = root.context.pxToDp(200f)
                layoutParams.width = root.context.pxToDp(200f)
                load(item.channelIcon) {
                    crossfade(true)
                    scale(Scale.FIT)
                }
            }
            btnDelete.setOnClickListener {
                onClickListener.onItemClick(item)
            }
        }
    }

    inner class ItemViewHolder(val binding: ItemSelectedChannelsBinding) :
        RecyclerView.ViewHolder(binding.root)
}
