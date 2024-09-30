package com.mvproject.tvprogramguide.data.repository

import androidx.room.Transaction
import com.mvproject.tvprogramguide.data.database.dao.AllChannelDao
import com.mvproject.tvprogramguide.data.mappers.Mappers.asChannelsFromEntities
import com.mvproject.tvprogramguide.data.mappers.Mappers.toAvailableChannelEntities
import com.mvproject.tvprogramguide.data.model.domain.AvailableChannel
import com.mvproject.tvprogramguide.data.model.entity.AvailableChannelEntity
import com.mvproject.tvprogramguide.data.network.EpgService
import com.mvproject.tvprogramguide.utils.AppConstants.COUNT_ZERO
import com.mvproject.tvprogramguide.utils.filterNoEpg
import timber.log.Timber
import javax.inject.Inject

class AllChannelRepository
    @Inject
    constructor(
        private val epgService: EpgService,
        private val allChannelDao: AllChannelDao,
    ) {
        private var databaseChannels: List<AvailableChannelEntity> = emptyList()

        suspend fun loadChannels(): List<AvailableChannel> {
            databaseChannels = allChannelDao.getChannelList()
            return if (databaseChannels.isEmpty()) {
                val networkChannels =
                    epgService
                        .getChannels()
                        .channels
                        .filterNoEpg()
                databaseChannels = networkChannels.toAvailableChannelEntities()
                allChannelDao.insertChannelList(availableChannels = databaseChannels)
                databaseChannels.asChannelsFromEntities()
            } else {
                databaseChannels.asChannelsFromEntities()
            }
        }

        @Transaction
        suspend fun loadProgramFromSource() {
            val networkChannels =
                epgService
                    .getChannels()
                    .channels
                    .filterNoEpg()

/*            val doc: Document =
                Ksoup.parseGetRequest(
                    url = "https://epg.ott-play.com/php/show_prow.php?f=edem/edem.xml.gz",
                )

            val table: Elements = doc.select("tr")
            val parsed = table.drop(1)

            Timber.w("testing parsed ${parsed.count()}")

            val networkChannels =
                buildList {
                    parsed.forEach { element ->
                        val selected = element.select("td")
                        val id = selected[2].text()
                        val logo = selected[0].select("img").attr("src")
                        val names = selected[1].textNodes()

                        names.forEach { name ->
                            add(
                                AvailableChannelResponse(
                                    channelNames = name.text(),
                                    channelId = id,
                                    channelIcon = logo,
                                ),
                            )
                        }
                    }
                }
 */

            val entities = networkChannels.toAvailableChannelEntities()
            Timber.w("testing entities ${entities.count()}")
            if (entities.count() > COUNT_ZERO) {
                updateEntities(entities = entities)
            }
        }

        suspend fun updateEntities(entities: List<AvailableChannelEntity>) {
            allChannelDao.apply {
                deleteChannels()
                insertChannelList(availableChannels = entities)
            }
        }
    }
