package com.mvproject.tvprogramguide.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.mvproject.tvprogramguide.model.data.SingleChannelModel
import com.mvproject.tvprogramguide.utils.Utils.calculateProgramProgress
import com.mvproject.tvprogramguide.utils.Utils.convertTimeToReadableFormat

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun SingleChannelData(singleChannelPrograms: List<SingleChannelModel>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxHeight()
            .background(Color.White)
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