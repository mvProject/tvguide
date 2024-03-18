package com.mvproject.tvprogramguide.domain.usecases

import com.mvproject.tvprogramguide.data.repository.CustomListRepository
import com.mvproject.tvprogramguide.data.repository.PreferenceRepository
import com.mvproject.tvprogramguide.utils.AppConstants
import kotlinx.coroutines.flow.first
import javax.inject.Inject

/**
 * Use case to adding new playlist
 * @property customListRepository the CustomListRepository repository
 * @property preferenceRepository the preferences repository
 */
class AddPlaylistUseCase
    @Inject
    constructor(
        private val customListRepository: CustomListRepository,
        private val preferenceRepository: PreferenceRepository,
    ) {
        suspend operator fun invoke(listName: String) {
            customListRepository.addCustomList(name = listName)
            checkForDefaultAfterAdd()
        }

        private suspend fun checkForDefaultAfterAdd() {
            val playlists = customListRepository.loadChannelsLists().first()
            if (playlists.count() == AppConstants.COUNT_ONE) {
                val listName = playlists.first().listName
                preferenceRepository.setDefaultUserList(listName = listName)
            }
        }
    }
