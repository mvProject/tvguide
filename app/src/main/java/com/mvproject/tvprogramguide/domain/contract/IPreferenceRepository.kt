package com.mvproject.tvprogramguide.domain.contract

import com.mvproject.tvprogramguide.data.model.settings.AppSettingsModel
import kotlinx.coroutines.flow.Flow

interface IPreferenceRepository {
    suspend fun setOnBoardState(onBoardState: Boolean)
    fun loadOnBoardState(): Flow<Boolean>
    val isNeedFullProgramsUpdate: Flow<Boolean>
    val isNeedAvailableChannelsUpdate: Flow<Boolean>
    suspend fun setChannelsUpdateLastTime(timeInMillis: Long)
    suspend fun setProgramsUpdateLastTime(timeInMillis: Long)
    suspend fun setProgramsUpdateRequiredState(state: Boolean)
    fun getProgramsUpdateRequiredState(): Flow<Boolean>
    suspend fun setAppSettings(appSettings: AppSettingsModel)
    fun loadAppSettings(): Flow<AppSettingsModel>
    suspend fun setProgramsCleanTime(timeInMillis: Long)
    suspend fun getProgramsCleanTime(): Long
}
