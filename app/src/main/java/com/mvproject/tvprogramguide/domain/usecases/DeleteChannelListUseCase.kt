package com.mvproject.tvprogramguide.domain.usecases

import com.mvproject.tvprogramguide.data.model.domain.ChannelList
import com.mvproject.tvprogramguide.data.repository.ChannelListRepository
import javax.inject.Inject

/**
 * Use case to removing specified playlist
 * @property channelListRepository the CustomListRepository repository
 */
class DeleteChannelListUseCase
@Inject
constructor(
    private val channelListRepository: ChannelListRepository
) {
    suspend operator fun invoke(list: ChannelList) {
        if (list.isSelected) {
            val updateSelected = channelListRepository.loadChannelsLists()
                .firstOrNull { !it.isSelected }

            updateSelected?.let { selected ->
                channelListRepository.addChannelList(selected.copy(isSelected = true))
            }
        }
        channelListRepository.deleteList(item = list)
    }
}
