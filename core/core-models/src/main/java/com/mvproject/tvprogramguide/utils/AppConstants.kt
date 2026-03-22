package com.mvproject.tvprogramguide.utils

import kotlinx.serialization.json.Json

object AppConstants {
    const val NO_VALUE_LONG = -1L
    const val NO_VALUE_INT = -1

    const val COUNT_ONE = 1
    const val COUNT_ZERO = 0
    const val COUNT_ZERO_LONG = 0L
    const val COUNT_ZERO_FLOAT = 0f

    const val DEFAULT_DELAY = 500L
    const val REFRESH_DELAY = 1500L
    const val WORKER_DELAY = 2000L

    const val DEFAULT_PROGRAMS_UPDATE_PERIOD = 2
    const val DEFAULT_CHANNELS_UPDATE_PERIOD = 7
    const val DEFAULT_PROGRAMS_VISIBLE_COUNT = 3

    const val SELECTED_CHANNELS_PAGE = 1

    const val USER_LIST_MAX_LENGTH = 20

    const val ANIM_DURATION_200 = 200
    const val ANIM_DURATION_300 = 300
    const val ANIM_DURATION_600 = 600
    const val ANIM_DURATION_750 = 750
    const val ANIM_DURATION_900 = 900

    const val PROGRESS_STATE_COMPLETE = 1f

    const val ROTATION_STATE_UP = 180f
    const val ROTATION_STATE_DOWN = 0f

    const val PROGRAM_TIME_MEASURE_DELIMITER = ":"
    const val PROGRAM_TIME_MEASURE_COUNT = 2

    const val ONBOARD_PAGES_COUNT = 3
    const val ONBOARD_LAST_PAGES_INDEX = 2

    const val TOTAL_CHANNELS_COUNT = 2300

    val json = Json {
        prettyPrint = true
        isLenient = true
        ignoreUnknownKeys = true
    }

    inline val String.Companion.empty get() = ""
}
