package com.mvproject.tvprogramguide.domain.usecases

import com.mvproject.tvprogramguide.data.model.domain.UserChannelsList
import com.mvproject.tvprogramguide.data.repository.CustomListRepository
import com.mvproject.tvprogramguide.data.repository.PreferenceRepository
import com.mvproject.tvprogramguide.utils.AppConstants
import kotlinx.coroutines.flow.first
import javax.inject.Inject

/**
 * Use case to removing specified playlist
 * @property customListRepository the CustomListRepository repository
 * @property preferenceRepository the preferences repository
 */
class DeletePlaylistUseCase
    @Inject
    constructor(
        private val customListRepository: CustomListRepository,
        private val preferenceRepository: PreferenceRepository,
    ) {
        suspend operator fun invoke(list: UserChannelsList) {
            customListRepository.deleteList(item = list)
            val defaultChannelList = preferenceRepository.loadDefaultUserList().first()
            if (defaultChannelList == list.listName) {
                checkForDefaultAfterDelete()
            }
        }

        private suspend fun checkForDefaultAfterDelete() {
            val playlists = customListRepository.loadChannelsLists().first()
            val listName =
                if (playlists.isNotEmpty()) {
                    playlists.first().listName
                } else {
                    AppConstants.NO_VALUE_STRING
                }
            preferenceRepository.setDefaultUserList(listName = listName)
        }
    }
