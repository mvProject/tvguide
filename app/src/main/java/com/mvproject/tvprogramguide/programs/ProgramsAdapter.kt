package com.mvproject.tvprogramguide.programs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.ExperimentalMaterialApi
import androidx.core.view.isVisible
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import com.mvproject.tvprogramguide.components.ChannelItem
import com.mvproject.tvprogramguide.components.DateItem
import com.mvproject.tvprogramguide.components.ProgramItem
import com.mvproject.tvprogramguide.databinding.*
import com.mvproject.tvprogramguide.model.data.Channel
import com.mvproject.tvprogramguide.model.data.DateHeader
import com.mvproject.tvprogramguide.model.data.IChannel
import com.mvproject.tvprogramguide.model.data.Program
import com.mvproject.tvprogramguide.sticky.StickyHeaders
import com.mvproject.tvprogramguide.utils.OnClickListener
import com.mvproject.tvprogramguide.utils.Utils
import com.mvproject.tvprogramguide.utils.Utils.calculateProgramProgress
import com.mvproject.tvprogramguide.utils.Utils.convertDateToReadableFormat
import com.mvproject.tvprogramguide.utils.Utils.convertTimeToReadableFormat
import com.mvproject.tvprogramguide.utils.Utils.parseChannelName
import com.mvproject.tvprogramguide.utils.Utils.pxToDp
import timber.log.Timber

@ExperimentalMaterialApi
class ProgramsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(),
    StickyHeaders {

    private var headerListener: OnClickListener<Channel>? = null

    fun setupHeaderListener(listener: OnClickListener<Channel>) {
        headerListener = listener
    }

    override fun getItemCount() = items.size

    override fun isStickyHeader(position: Int) =
        items[position] is Channel || items[position] is DateHeader

    override fun getItemViewType(position: Int): Int =
        when (items[position]) {
            is Channel -> TYPE_HEADER
            is Program -> TYPE_MESSAGE
            is DateHeader -> TYPE_DATE
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
            ChannelHeaderItemComposeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
        TYPE_MESSAGE -> ProgramViewHolder(
            ProgramItemComposeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
        TYPE_DATE -> DateViewHolder(
            ChannelDateItemComposeBinding.inflate(
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
            TYPE_DATE -> onBindDate(holder, items[position] as DateHeader)
            else -> throw IllegalArgumentException()
        }

    private fun onBindHeader(holder: RecyclerView.ViewHolder, chn: Channel) {
        (holder as ChannelViewHolder).binding.root.setContent {
            ChannelItem(
                channelName = chn.channelName.parseChannelName(),
                channelLogo = chn.channelIcon
            ) {
                headerListener?.onItemClick(chn)
            }
        }
    }

    private fun onBindDate(holder: RecyclerView.ViewHolder, chn: DateHeader) {
        (holder as DateViewHolder).binding.root.setContent {
            DateItem(date = chn.date)
        }
    }

    @ExperimentalMaterialApi
    private fun onBindMessage(holder: RecyclerView.ViewHolder, prg: Program) {
        (holder as ProgramViewHolder).binding.root.setContent {
            ProgramItem(
                prgTime = prg.dateTimeStart.convertTimeToReadableFormat(),
                prgTitle = prg.title,
                prgDescription = prg.description,
                progressValue = calculateProgramProgress(
                    prg.dateTimeStart,
                    prg.dateTimeEnd
                )
            )
        }
    }

    inner class ChannelViewHolder(val binding: ChannelHeaderItemComposeBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class ProgramViewHolder(val binding: ProgramItemComposeBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class DateViewHolder(val binding: ChannelDateItemComposeBinding) :
        RecyclerView.ViewHolder(binding.root)

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_MESSAGE = 1
        private const val TYPE_DATE = 2
    }
}