package com.example.coloria.utils

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/** Минимальный процент схожести, при котором стрик продолжает расти. */
const val STREAK_STEP_PERCENT = 85

/**
 * Стрик игрока и его лучший результат.
 *
 * [soccer] — текущая серия раундов подряд с результатом не ниже [STREAK_STEP_PERCENT].
 * [record] — максимальный стрик за всё время, сохраняется через [storage].
 * [isRecordLoaded] — рекорд прочитан с устройства; до этого момента показывать его нельзя.
 * [isRecordBeaten] — текущая серия уже равна рекорду: подпись рекорда в шапке избыточна.
 * [isRecordBeatenJustNow] — одноразовое событие: серия впервые превысила рекорд,
 * существовавший до начала текущей серии.
 *
 * @param scope область для чтения и записи рекорда. Должна жить не меньше экрана,
 * поэтому не подходит `rememberCoroutineScope()`.
 */
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

    /**
     * Событие «новый рекорд»: `true` только в том раунде, где серия впервые превысила
     * рекорд, зафиксированный до начала серии. Гасится через [consumeRecordBeaten].
     */
    var isRecordBeatenJustNow by mutableStateOf(false)
        private set

    /**
     * Рекорд на момент старта текущей серии.
     *
     * Сравнивать [soccer] нужно именно с ним, а не с постоянно растущим [record]:
     * иначе плашка появлялась бы в каждом успешном раунде, а не один раз.
     */
    private var streakStartRecord = 0

    /** Плашка о новом рекорде уже показана в текущей серии — повторно не показываем. */
    private var isRecordCapsuleShownInStreak = false

    /**
     * Пока серия не прервана, она же и есть рекорд, поэтому подпись рекорда не нужна.
     * Как только раунд ниже порога сбрасывает [soccer] в 0, подпись возвращается.
     */
    val isRecordBeaten: Boolean
        get() = soccer > 0 && soccer >= record

    init {
        scope.launch {
            storage.recordFlow.collect { stored ->
                // Значение из хранилища может отставать от уже набранного за сессию.
                if (stored > record) {
                    record = stored
                }
                // Рекорд, прочитанный с устройства до первого раунда, — база для сравнения.
                if (!isRecordLoaded) {
                    streakStartRecord = record
                }
                isRecordLoaded = true
            }
        }
    }

    /** Применяет результат раунда: продолжает или сбрасывает стрик и обновляет рекорд. */
    fun applyRound(similarityPercent: Int) {
        val isSuccessful = similarityPercent >= STREAK_STEP_PERCENT

        if (isSuccessful) {
            if (soccer == 0) {
                // Серия начинается — запоминаем рекорд, с которым будем сравнивать.
                streakStartRecord = record
                isRecordCapsuleShownInStreak = false
            }
            soccer += 1
        } else {
            soccer = 0
        }

        // Серия впервые превысила рекорд, существовавший до её начала, и плашку ещё не показали.
        isRecordBeatenJustNow = soccer > streakStartRecord && !isRecordCapsuleShownInStreak

        if (soccer > record) {
            record = soccer
            scope.launch { storage.saveRecord(soccer) }
        }
    }

    /** Помечает уведомление о новом рекорде как показанное. */
    fun consumeRecordBeaten() {
        if (isRecordBeatenJustNow) {
            isRecordCapsuleShownInStreak = true
        }
        isRecordBeatenJustNow = false
    }
}