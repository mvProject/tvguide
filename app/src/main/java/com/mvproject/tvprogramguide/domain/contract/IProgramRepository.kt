package com.mvproject.tvprogramguide.domain.contract

import com.mvproject.tvprogramguide.data.model.domain.Program
import com.mvproject.tvprogramguide.data.model.parse.ProgramDTO

interface IProgramRepository {
    suspend fun loadProgramsForChannels(channelsIds: List<String>): List<Program>
    suspend fun loadProgramsForChannel(channelId: String): List<Program>
    suspend fun updatePrograms(channelId: String, programs: List<ProgramDTO>)
    suspend fun cleanProgramsBeforeDate(date: Long)
    suspend fun updateProgram(program: Program)
}
