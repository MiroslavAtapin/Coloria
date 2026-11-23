package com.example.coloria.utils

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch


const val STREAK_STEP_PERCENT = 85


class StreakState(
    private val storage: StreakStorage = InMemoryStreakStorage(),
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO),
) {
    var soccer by mutableStateOf(0)
        private set

    var record by mutableStateOf(0)
        private set

    var isRecordLoaded by mutableStateOf(false)
        private set


    var isRecordBeatenJustNow by mutableStateOf(false)
        private set


    private var streakStartRecord = 0


    private var isRecordCapsuleShownInStreak = false


    val isRecordBeaten: Boolean
        get() = soccer > 0 && soccer >= record

    init {
        scope.launch {
            storage.recordFlow.collect { stored ->
                if (stored > record) {
                    record = stored
                }
                if (!isRecordLoaded) {
                    streakStartRecord = record
                }
                isRecordLoaded = true
            }
        }
    }


    fun applyRound(similarityPercent: Int) {
        val isSuccessful = similarityPercent >= STREAK_STEP_PERCENT

        if (isSuccessful) {
            if (soccer == 0) {
                streakStartRecord = record
                isRecordCapsuleShownInStreak = false
            }
            soccer += 1
        } else {
            soccer = 0
        }
        isRecordBeatenJustNow = soccer > streakStartRecord && !isRecordCapsuleShownInStreak

        if (soccer > record) {
            record = soccer
            scope.launch { storage.saveRecord(soccer) }
        }
    }


    fun consumeRecordBeaten() {
        if (isRecordBeatenJustNow) {
            isRecordCapsuleShownInStreak = true
        }
        isRecordBeatenJustNow = false
    }
}
