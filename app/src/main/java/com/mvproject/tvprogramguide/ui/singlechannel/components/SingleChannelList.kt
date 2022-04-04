package com.mvproject.tvprogramguide.ui.singlechannel.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import com.mvproject.tvprogramguide.components.ProgramItem
import com.mvproject.tvprogramguide.data.model.SingleChannelModel
import com.mvproject.tvprogramguide.utils.Utils.calculateProgramProgress
import com.mvproject.tvprogramguide.utils.Utils.convertTimeToReadableFormat
import timber.log.Timber

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun SingleChannelList(singleChannelPrograms: List<SingleChannelModel>) {
    SideEffect {
        Timber.i("testing SingleChannelList SideEffect")
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxHeight()
            .background(MaterialTheme.colors.secondary)
    ) {
        singleChannelPrograms.forEach {
            stickyHeader {
                DateItem(date = it.date)
            }
            items(it.programs) { program ->
                ProgramItem(
                    prgTime = program.dateTimeStart.convertTimeToReadableFormat(),
                    prgTitle = program.title,
                    prgDescription = program.description,
                    progressValue = calculateProgramProgress(
                        program.dateTimeStart,
                        program.dateTimeEnd
                    )
                )
            }
        }
    }
}