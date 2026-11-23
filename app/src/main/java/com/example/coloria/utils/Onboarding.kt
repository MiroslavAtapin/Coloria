package com.example.coloria.utils

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import java.io.IOException

private const val ONBOARDING_DATASTORE_NAME = "coloria_onboarding"

private val Context.onboardingDataStore by preferencesDataStore(name = ONBOARDING_DATASTORE_NAME)

private val WELCOME_SHOWN_KEY = booleanPreferencesKey("is_welcome_shown")


interface OnboardingStorage {

    val isWelcomeShownFlow: Flow<Boolean>


    suspend fun markWelcomeShown()
}


class DataStoreOnboardingStorage(context: Context) : OnboardingStorage {
    private val dataStore = context.applicationContext.onboardingDataStore

    override val isWelcomeShownFlow: Flow<Boolean> = dataStore.data
        .catch { cause ->
            if (cause is IOException) emit(emptyPreferences()) else throw cause
        }
        .map { prefs -> prefs[WELCOME_SHOWN_KEY] ?: false }

    override suspend fun markWelcomeShown() {
        dataStore.edit { prefs -> prefs[WELCOME_SHOWN_KEY] = true }
    }
}


class InMemoryOnboardingStorage(private var isShown: Boolean = false) : OnboardingStorage {
    override val isWelcomeShownFlow: Flow<Boolean> = flowOf(isShown)

    override suspend fun markWelcomeShown() {
        isShown = true
    }
}
