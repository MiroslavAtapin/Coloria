package com.example.coloria.utils

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import java.io.IOException

private const val STREAK_DATASTORE_NAME = "coloria_streak"

/**
 * Единственный экземпляр DataStore на процесс.
 *
 * Второй экземпляр на тот же файл выбросит IllegalStateException,
 * поэтому делегат объявлен на уровне файла, а не создаётся в композиции.
 */
private val Context.streakDataStore by preferencesDataStore(name = STREAK_DATASTORE_NAME)

private val RECORD_KEY = intPreferencesKey("record")

/** Источник сохранённого рекорда и приёмник его обновлений. */
interface StreakStorage {
    /** Текущий сохранённый рекорд; обновляется при каждой записи. */
    val recordFlow: Flow<Int>

    /** Сохраняет новый рекорд на устройство. */
    suspend fun saveRecord(record: Int)
}

/** Хранит рекорд в DataStore. */
class DataStoreStreakStorage(context: Context) : StreakStorage {
    private val dataStore = context.applicationContext.streakDataStore

    override val recordFlow: Flow<Int> = dataStore.data
        .catch { cause ->
            // Повреждённый или недоступный файл не должен ронять игру.
            if (cause is IOException) emit(emptyPreferences()) else throw cause
        }
        .map { prefs -> prefs[RECORD_KEY] ?: 0 }

    override suspend fun saveRecord(record: Int) {
        dataStore.edit { prefs -> prefs[RECORD_KEY] = record }
    }
}

/** Хранит рекорд в памяти: для превью и юнит-тестов. */
class InMemoryStreakStorage(private var record: Int = 0) : StreakStorage {
    override val recordFlow: Flow<Int> = flowOf(record)

    override suspend fun saveRecord(record: Int) {
        this.record = record
    }
}