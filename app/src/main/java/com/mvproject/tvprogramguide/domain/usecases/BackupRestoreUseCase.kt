package com.mvproject.tvprogramguide.domain.usecases

import com.mvproject.tvprogramguide.data.model.backup.TvBackup
import com.mvproject.tvprogramguide.data.model.domain.ChannelList
import com.mvproject.tvprogramguide.data.model.settings.AppSettingsModel
import com.mvproject.tvprogramguide.data.repository.AllChannelRepository
import com.mvproject.tvprogramguide.data.repository.ChannelListRepository
import com.mvproject.tvprogramguide.data.repository.PreferenceRepository
import com.mvproject.tvprogramguide.data.repository.SelectedChannelRepository
import com.mvproject.tvprogramguide.utils.ChannelUtils.updateOrders

/**
 * Use case responsible for restoring application state from a backup.
 * Handles the restoration of:
 * - Application settings
 * - Channel lists
 * - Selected channels for each list
 * - Channel ordering
 *
 * @property preferenceRepository Repository for managing application preferences
 * @property channelListRepository Repository for managing channel lists
 * @property allChannelRepository Repository for managing available channels
 * @property selectedChannelRepository Repository for managing selected channels
 */
class BackupRestoreUseCase(
    private val preferenceRepository: PreferenceRepository,
    private val channelListRepository: ChannelListRepository,
    private val allChannelRepository: AllChannelRepository,
    private val selectedChannelRepository: SelectedChannelRepository,
) {
    /**
     * Executes the backup restoration process.
     * This operation:
     * 1. Restores application settings
     * 2. Recreates all channel lists
     * 3. Restores selected channels for each list
     * 4. Ensures there is always a selected default list
     *
     * @param tvBackup The backup data to restore from
     */
    suspend operator fun invoke(tvBackup: TvBackup) {
        val appSettings = AppSettingsModel(
            appTheme = tvBackup.theme,
            programsViewCount = tvBackup.programsViewCount,
            programsUpdatePeriod = tvBackup.programsUpdatePeriod,
            channelsUpdatePeriod = tvBackup.channelsUpdatePeriod
        )

        preferenceRepository.setAppSettings(appSettings)

        tvBackup.channelsData.forEach { data ->
            val channelList = ChannelList(
                id = data.id,
                listName = data.name,
                isSelected = data.isSelected
            )
            channelListRepository.addChannelList(list = channelList)

            val channels = allChannelRepository
                .loadChannelsById(selectedIds = data.content)
                .updateOrders()
                .map { it.copy(parentList = data.name) }

            selectedChannelRepository.addChannels(
                listName = data.name,
                selectedChannels = channels,
            )
        }

        val channelsLists = channelListRepository.loadChannelsLists()

        if (channelsLists.none { it.isSelected }) {
            val default = channelsLists.first().copy(isSelected = true)
            channelListRepository.addChannelList(default)
        }
    }
}