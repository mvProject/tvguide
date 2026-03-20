package com.mvproject.tvprogramguide.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import app.cash.turbine.test
import com.mvproject.tvprogramguide.data.model.settings.AppSettingsModel
import com.mvproject.tvprogramguide.data.model.settings.AppThemeOptions
import com.mvproject.tvprogramguide.data.repository.PreferenceRepository
import com.mvproject.tvprogramguide.utils.AppConstants.DEFAULT_CHANNELS_UPDATE_PERIOD
import com.mvproject.tvprogramguide.utils.AppConstants.DEFAULT_PROGRAMS_UPDATE_PERIOD
import com.mvproject.tvprogramguide.utils.AppConstants.DEFAULT_PROGRAMS_VISIBLE_COUNT
import com.mvproject.tvprogramguide.utils.AppConstants.NO_VALUE_LONG
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.flow.flowOf
import kotlinx.datetime.Clock
import kotlin.time.Duration.Companion.days

class PreferenceRepositoryTest : FunSpec({

    lateinit var dataStore: DataStore<Preferences>
    lateinit var preferences: Preferences
    lateinit var repository: PreferenceRepository

    // Key constants mirroring the private companion in PreferenceRepository
    val APP_THEME_OPTION = intPreferencesKey("theme_option")
    val PROGRAM_UPDATE_PERIOD_OPTION = intPreferencesKey("program_update_period_option")
    val CHANNELS_UPDATE_PERIOD_OPTION = intPreferencesKey("channels_update_period_option")
    val PROGRAM_VIEW_COUNT_OPTION = intPreferencesKey("program_view_count_option")
    val PROGRAM_CLEAN = longPreferencesKey("programClean")
    val LAST_UPDATE_CHANNELS = longPreferencesKey("LastUpdateChannels")
    val LAST_UPDATE_PROGRAMS = longPreferencesKey("LastUpdatePrograms")
    val ON_BOARD_COMPLETE = booleanPreferencesKey("onBoardComplete")
    val PROGRAMS_UPDATE_REQUIRED = booleanPreferencesKey("ProgramsUpdateRequired")

    beforeTest {
        dataStore = mockk()
        preferences = mockk()
        repository = PreferenceRepository(dataStore)
    }

    afterTest {
        unmockkAll()
    }

    context("loadOnBoardState") {
        test("returns true when key is absent (default)") {
            every { preferences[ON_BOARD_COMPLETE] } returns null
            every { dataStore.data } returns flowOf(preferences)

            repository.loadOnBoardState().test {
                awaitItem() shouldBe true
                awaitComplete()
            }
            confirmVerified(dataStore)
        }

        test("returns stored value when key is present") {
            every { preferences[ON_BOARD_COMPLETE] } returns false
            every { dataStore.data } returns flowOf(preferences)

            repository.loadOnBoardState().test {
                awaitItem() shouldBe false
                awaitComplete()
            }
            confirmVerified(dataStore)
        }
    }

    context("getProgramsCleanTime") {
        test("returns NO_VALUE_LONG when key is absent") {
            every { preferences[PROGRAM_CLEAN] } returns null
            every { dataStore.data } returns flowOf(preferences)

            val result = repository.getProgramsCleanTime()
            result shouldBe NO_VALUE_LONG
            confirmVerified(dataStore)
        }

        test("returns stored value when key is present") {
            val storedTime = 1_700_000_000_000L
            every { preferences[PROGRAM_CLEAN] } returns storedTime
            every { dataStore.data } returns flowOf(preferences)

            val result = repository.getProgramsCleanTime()
            result shouldBe storedTime
            confirmVerified(dataStore)
        }
    }

    context("loadAppSettings") {
        test("returns default AppSettingsModel when no keys are stored") {
            every { preferences[APP_THEME_OPTION] } returns null
            every { preferences[PROGRAM_UPDATE_PERIOD_OPTION] } returns null
            every { preferences[CHANNELS_UPDATE_PERIOD_OPTION] } returns null
            every { preferences[PROGRAM_VIEW_COUNT_OPTION] } returns null
            every { dataStore.data } returns flowOf(preferences)

            repository.loadAppSettings().test {
                awaitItem() shouldBe AppSettingsModel(
                    programsUpdatePeriod = DEFAULT_PROGRAMS_UPDATE_PERIOD,
                    channelsUpdatePeriod = DEFAULT_CHANNELS_UPDATE_PERIOD,
                    programsViewCount = DEFAULT_PROGRAMS_VISIBLE_COUNT,
                    appTheme = AppThemeOptions.SYSTEM.id,
                )
                awaitComplete()
            }
            confirmVerified(dataStore)
        }

        test("returns stored AppSettingsModel when all keys are present") {
            every { preferences[APP_THEME_OPTION] } returns AppThemeOptions.DARK.id
            every { preferences[PROGRAM_UPDATE_PERIOD_OPTION] } returns 5
            every { preferences[CHANNELS_UPDATE_PERIOD_OPTION] } returns 14
            every { preferences[PROGRAM_VIEW_COUNT_OPTION] } returns 6
            every { dataStore.data } returns flowOf(preferences)

            repository.loadAppSettings().test {
                awaitItem() shouldBe AppSettingsModel(
                    programsUpdatePeriod = 5,
                    channelsUpdatePeriod = 14,
                    programsViewCount = 6,
                    appTheme = AppThemeOptions.DARK.id,
                )
                awaitComplete()
            }
            confirmVerified(dataStore)
        }
    }

    context("getProgramsUpdateRequiredState") {
        test("returns false when key is absent (default)") {
            every { preferences[PROGRAMS_UPDATE_REQUIRED] } returns null
            every { dataStore.data } returns flowOf(preferences)

            repository.getProgramsUpdateRequiredState().test {
                awaitItem() shouldBe false
                awaitComplete()
            }
            confirmVerified(dataStore)
        }

        test("returns stored value when key is present") {
            every { preferences[PROGRAMS_UPDATE_REQUIRED] } returns true
            every { dataStore.data } returns flowOf(preferences)

            repository.getProgramsUpdateRequiredState().test {
                awaitItem() shouldBe true
                awaitComplete()
            }
            confirmVerified(dataStore)
        }
    }

    context("isNeedFullProgramsUpdate") {
        test("returns true when last update time is NO_VALUE_LONG (never updated)") {
            // All app-setting keys absent → defaults; LAST_UPDATE_PROGRAMS absent → NO_VALUE_LONG (-1)
            every { preferences[APP_THEME_OPTION] } returns null
            every { preferences[PROGRAM_UPDATE_PERIOD_OPTION] } returns null
            every { preferences[CHANNELS_UPDATE_PERIOD_OPTION] } returns null
            every { preferences[PROGRAM_VIEW_COUNT_OPTION] } returns null
            every { preferences[LAST_UPDATE_PROGRAMS] } returns null
            every { dataStore.data } returns flowOf(preferences)

            repository.isNeedFullProgramsUpdate.test {
                awaitItem() shouldBe true
                awaitComplete()
            }
            confirmVerified(dataStore)
        }

        test("returns false when last update is very recent") {
            val recentTime = Clock.System.now().toEpochMilliseconds()
            every { preferences[APP_THEME_OPTION] } returns null
            every { preferences[PROGRAM_UPDATE_PERIOD_OPTION] } returns DEFAULT_PROGRAMS_UPDATE_PERIOD
            every { preferences[CHANNELS_UPDATE_PERIOD_OPTION] } returns null
            every { preferences[PROGRAM_VIEW_COUNT_OPTION] } returns null
            every { preferences[LAST_UPDATE_PROGRAMS] } returns recentTime
            every { dataStore.data } returns flowOf(preferences)

            repository.isNeedFullProgramsUpdate.test {
                awaitItem() shouldBe false
                awaitComplete()
            }
            confirmVerified(dataStore)
        }

        test("returns true when last update is older than the update period") {
            val oldTime = (Clock.System.now() - (DEFAULT_PROGRAMS_UPDATE_PERIOD + 1).days)
                .toEpochMilliseconds()
            every { preferences[APP_THEME_OPTION] } returns null
            every { preferences[PROGRAM_UPDATE_PERIOD_OPTION] } returns DEFAULT_PROGRAMS_UPDATE_PERIOD
            every { preferences[CHANNELS_UPDATE_PERIOD_OPTION] } returns null
            every { preferences[PROGRAM_VIEW_COUNT_OPTION] } returns null
            every { preferences[LAST_UPDATE_PROGRAMS] } returns oldTime
            every { dataStore.data } returns flowOf(preferences)

            repository.isNeedFullProgramsUpdate.test {
                awaitItem() shouldBe true
                awaitComplete()
            }
            confirmVerified(dataStore)
        }
    }

    context("isNeedAvailableChannelsUpdate") {
        test("returns true when channels were never updated") {
            every { preferences[APP_THEME_OPTION] } returns null
            every { preferences[PROGRAM_UPDATE_PERIOD_OPTION] } returns null
            every { preferences[CHANNELS_UPDATE_PERIOD_OPTION] } returns null
            every { preferences[PROGRAM_VIEW_COUNT_OPTION] } returns null
            every { preferences[LAST_UPDATE_CHANNELS] } returns null
            every { dataStore.data } returns flowOf(preferences)

            repository.isNeedAvailableChannelsUpdate.test {
                awaitItem() shouldBe true
                awaitComplete()
            }
            confirmVerified(dataStore)
        }

        test("returns false when channels were updated recently") {
            val recentTime = Clock.System.now().toEpochMilliseconds()
            every { preferences[APP_THEME_OPTION] } returns null
            every { preferences[PROGRAM_UPDATE_PERIOD_OPTION] } returns null
            every { preferences[CHANNELS_UPDATE_PERIOD_OPTION] } returns DEFAULT_CHANNELS_UPDATE_PERIOD
            every { preferences[PROGRAM_VIEW_COUNT_OPTION] } returns null
            every { preferences[LAST_UPDATE_CHANNELS] } returns recentTime
            every { dataStore.data } returns flowOf(preferences)

            repository.isNeedAvailableChannelsUpdate.test {
                awaitItem() shouldBe false
                awaitComplete()
            }
            confirmVerified(dataStore)
        }
    }

    context("setChannelsUpdateLastTime") {
        test("calls dataStore.edit") {
            val editedPreferences = mockk<MutablePreferences>(relaxed = true)
            coEvery { dataStore.edit(any()) } coAnswers {
                val block = firstArg<suspend (MutablePreferences) -> Unit>()
                block(editedPreferences)
                preferences
            }

            repository.setChannelsUpdateLastTime(12345L)

            coVerify(exactly = 1) { dataStore.edit(any()) }
            confirmVerified(dataStore)
        }
    }

    context("setProgramsUpdateLastTime") {
        test("calls dataStore.edit") {
            val editedPreferences = mockk<MutablePreferences>(relaxed = true)
            coEvery { dataStore.edit(any()) } coAnswers {
                val block = firstArg<suspend (MutablePreferences) -> Unit>()
                block(editedPreferences)
                preferences
            }

            repository.setProgramsUpdateLastTime(99999L)

            coVerify(exactly = 1) { dataStore.edit(any()) }
            confirmVerified(dataStore)
        }
    }

    context("setProgramsCleanTime") {
        test("calls dataStore.edit") {
            val editedPreferences = mockk<MutablePreferences>(relaxed = true)
            coEvery { dataStore.edit(any()) } coAnswers {
                val block = firstArg<suspend (MutablePreferences) -> Unit>()
                block(editedPreferences)
                preferences
            }

            repository.setProgramsCleanTime(77777L)

            coVerify(exactly = 1) { dataStore.edit(any()) }
            confirmVerified(dataStore)
        }
    }

    context("setOnBoardState") {
        test("calls dataStore.edit") {
            val editedPreferences = mockk<MutablePreferences>(relaxed = true)
            coEvery { dataStore.edit(any()) } coAnswers {
                val block = firstArg<suspend (MutablePreferences) -> Unit>()
                block(editedPreferences)
                preferences
            }

            repository.setOnBoardState(true)

            coVerify(exactly = 1) { dataStore.edit(any()) }
            confirmVerified(dataStore)
        }
    }

    context("setProgramsUpdateRequiredState") {
        test("calls dataStore.edit") {
            val editedPreferences = mockk<MutablePreferences>(relaxed = true)
            coEvery { dataStore.edit(any()) } coAnswers {
                val block = firstArg<suspend (MutablePreferences) -> Unit>()
                block(editedPreferences)
                preferences
            }

            repository.setProgramsUpdateRequiredState(true)

            coVerify(exactly = 1) { dataStore.edit(any()) }
            confirmVerified(dataStore)
        }
    }

    context("setAppSettings") {
        test("calls dataStore.edit") {
            val editedPreferences = mockk<MutablePreferences>(relaxed = true)
            coEvery { dataStore.edit(any()) } coAnswers {
                val block = firstArg<suspend (MutablePreferences) -> Unit>()
                block(editedPreferences)
                preferences
            }

            repository.setAppSettings(AppSettingsModel())

            coVerify(exactly = 1) { dataStore.edit(any()) }
            confirmVerified(dataStore)
        }
    }
})
