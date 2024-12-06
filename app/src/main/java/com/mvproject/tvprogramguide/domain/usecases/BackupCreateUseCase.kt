package com.mvproject.tvprogramguide.domain.usecases

import com.mvproject.tvprogramguide.data.model.backup.TvBackup
import com.mvproject.tvprogramguide.data.repository.ChannelListRepository
import com.mvproject.tvprogramguide.data.repository.PreferenceRepository
import com.mvproject.tvprogramguide.data.repository.SelectedChannelRepository
import com.mvproject.tvprogramguide.utils.TimeUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

/**
 * Use case responsible for creating a backup of the application's data.
 * Combines app settings, channel lists, and selected channels into a single backup object.
 *
 * @property preferenceRepository Repository for accessing application settings
 * @property selectedChannelRepository Repository for accessing selected channel data
 * @property channelListRepository Repository for accessing channel list data
 */
class BackupCreateUseCase(
    private val preferenceRepository: PreferenceRepository,
    private val selectedChannelRepository: SelectedChannelRepository,
    private val channelListRepository: ChannelListRepository,
) {
    /**
     * Creates a backup of the application data for specified channel lists.
     * The backup includes:
     * - Current app settings (theme, view counts, update periods)
     * - Selected channel lists and their content
     * - Channel order and selection state
     *
     * @param lists List of channel list names to include in the backup
     * @return [TvBackup] object containing all backed up data
     */
    suspend operator fun invoke(lists: List<String>) =
        withContext(Dispatchers.IO) {
            val settings = preferenceRepository.loadAppSettings().first()
            val playlists = channelListRepository.loadChannelsLists()

            val channelsContent = buildList {
                lists.forEach { name ->
                    playlists.firstOrNull { it.listName == name }?.let { playlist ->
                        val channels = selectedChannelRepository
                            .loadSelectedChannels(listName = playlist.listName)
                            .sortedBy { item -> item.order }

                        add(
                            TvBackup.PlaylistBackup(
                                id = playlist.id,
                                name = playlist.listName,
                                isSelected = playlist.isSelected,
                                content = channels.map { it.channelId })
                        )
                    }
                }
            }

            val tvBackup = TvBackup(
                timeStamp = TimeUtils.actualDate,
                theme = settings.appTheme,
                programsViewCount = settings.programsViewCount,
                channelsUpdatePeriod = settings.channelsUpdatePeriod,
                programsUpdatePeriod = settings.programsUpdatePeriod,
                channelsData = channelsContent
            )

            return@withContext tvBackup
        }
}
