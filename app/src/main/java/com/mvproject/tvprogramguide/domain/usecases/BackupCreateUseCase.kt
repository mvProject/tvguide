package com.mvproject.tvprogramguide.domain.usecases

import com.mvproject.tvprogramguide.data.model.backup.TvBackup
import com.mvproject.tvprogramguide.domain.contract.IChannelListRepository
import com.mvproject.tvprogramguide.domain.contract.IPreferenceRepository
import com.mvproject.tvprogramguide.domain.contract.ISelectedChannelRepository
import com.mvproject.tvprogramguide.utils.TimeUtils
import kotlinx.coroutines.flow.first

/**
 * Use case responsible for creating a backup of the application's data.
 * Combines app settings, channel lists, and selected channels into a single backup object.
 *
 * @property preferenceRepository Repository for accessing application settings
 * @property selectedChannelRepository Repository for accessing selected channel data
 * @property channelListRepository Repository for accessing channel list data
 */
class BackupCreateUseCase(
    private val preferenceRepository: IPreferenceRepository,
    private val selectedChannelRepository: ISelectedChannelRepository,
    private val channelListRepository: IChannelListRepository,
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
    suspend operator fun invoke(lists: List<String>): TvBackup {
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

        return TvBackup(
            timeStamp = TimeUtils.actualDate,
            theme = settings.appTheme,
            programsViewCount = settings.programsViewCount,
            channelsUpdatePeriod = settings.channelsUpdatePeriod,
            programsUpdatePeriod = settings.programsUpdatePeriod,
            channelsData = channelsContent
        )
    }
}
