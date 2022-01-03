package com.mvproject.tvprogramguide.programs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import com.mvproject.tvprogramguide.databinding.ChannelHeaderItemBinding
import com.mvproject.tvprogramguide.databinding.ProgramItemBinding
import com.mvproject.tvprogramguide.model.data.Channel
import com.mvproject.tvprogramguide.model.data.IChannel
import com.mvproject.tvprogramguide.model.data.Program
import com.mvproject.tvprogramguide.sticky.StickyHeaders
import com.mvproject.tvprogramguide.utils.Utils.convertTimeToReadableFormat
import com.mvproject.tvprogramguide.utils.Utils.parseChannelName
import com.mvproject.tvprogramguide.utils.Utils.pxToDp
import timber.log.Timber

class ProgramsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(),
    StickyHeaders {

    override fun getItemCount() = items.size

    override fun isStickyHeader(position: Int) = items[position] is Channel

    override fun getItemViewType(position: Int): Int =
        when (items[position]) {
            is Channel -> TYPE_HEADER
            is Program -> TYPE_MESSAGE
        }

    private val diffCallback = object : DiffUtil.ItemCallback<IChannel>() {
        override fun areItemsTheSame(oldItem: IChannel, newItem: IChannel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: IChannel, newItem: IChannel): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)
    var items: List<IChannel>
        get() = differ.currentList
        set(value) {
            differ.submitList(value)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        TYPE_HEADER -> ChannelViewHolder(
            ChannelHeaderItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
        TYPE_MESSAGE -> ProgramViewHolder(
            ProgramItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
        else -> throw IllegalArgumentException()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        when (holder.itemViewType) {
            TYPE_HEADER -> onBindHeader(holder, items[position] as Channel)
            TYPE_MESSAGE -> onBindMessage(holder, items[position] as Program)
            else -> throw IllegalArgumentException()
        }

    private fun onBindHeader(holder: RecyclerView.ViewHolder, chn: Channel) {
        (holder as ChannelViewHolder).binding.apply {
            channelLogo.apply {
                layoutParams.height = root.context.pxToDp(200f)
                layoutParams.width = root.context.pxToDp(200f)
                load(chn.channelIcon) {
                    crossfade(true)
                    scale(Scale.FIT)
                }
            }
            channelTitle.text = chn.channelName.parseChannelName()
        }
    }

    private fun onBindMessage(holder: RecyclerView.ViewHolder, prg: Program) {
        (holder as ProgramViewHolder).binding.apply {
            programTime.text = prg.dateTimeStart.convertTimeToReadableFormat()
            programName.text = prg.title

            if (prg.description.isNotEmpty()) {
                programDescription.text = prg.description
                programDescriptionLogo.apply {
                    visibility = View.VISIBLE
                    setOnClickListener {
                        programDescription.isVisible = !programDescription.isVisible
                    }
                }
            }

            val time = System.currentTimeMillis()
            if (time > prg.dateTimeStart) {
                programProgress.visibility = View.VISIBLE
                programProgress.max = (prg.dateTimeEnd - prg.dateTimeStart).toInt()
                programProgress.progress = (time - prg.dateTimeStart).toInt()
            } else {
                programProgress.visibility = View.GONE
            }
        }
    }

    inner class ChannelViewHolder(val binding: ChannelHeaderItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class ProgramViewHolder(val binding: ProgramItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_MESSAGE = 1
    }
}