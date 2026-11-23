package com.example.coloria.utils

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.example.coloria.R
import java.util.Collections


interface SoundPlayer {


    fun playSelect()


    fun playStreak()
}


object SilentSoundPlayer : SoundPlayer {

    override fun playSelect() {
    }


    override fun playStreak() {
    }
}


val LocalSoundPlayer = compositionLocalOf<SoundPlayer> { SilentSoundPlayer }


class SoundPoolSoundPlayer(context: Context) : SoundPlayer {

    private val soundPool = SoundPool.Builder()
        .setMaxStreams(MAX_STREAMS)
        .setAudioAttributes(
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build(),
        )
        .build()

    private val loadedSoundIds = Collections.synchronizedSet(mutableSetOf<Int>())

    private var selectSoundId = NOT_LOADED

    private var streakSoundId = NOT_LOADED

    init {
        soundPool.setOnLoadCompleteListener { _, sampleId, status ->
            if (status == LOAD_SUCCESS) {
                loadedSoundIds += sampleId
            }
        }

        selectSoundId = soundPool.load(context, R.raw.select, LOAD_PRIORITY)
        streakSoundId = soundPool.load(context, R.raw.streak, LOAD_PRIORITY)
    }


    override fun playSelect() {
        play(selectSoundId)
    }


    override fun playStreak() {
        play(streakSoundId)
    }


    fun release() {
        soundPool.release()
    }


    private fun play(soundId: Int) {
        if (!loadedSoundIds.contains(soundId)) return

        soundPool.play(
            soundId,
            VOLUME,
            VOLUME,
            PLAY_PRIORITY,
            NO_LOOP,
            NORMAL_RATE,
        )
    }


    private companion object {
        const val NOT_LOADED = 0
        const val MAX_STREAMS = 4
        const val LOAD_PRIORITY = 1
        const val LOAD_SUCCESS = 0
        const val VOLUME = 1f
        const val PLAY_PRIORITY = 1
        const val NO_LOOP = 0
        const val NORMAL_RATE = 1f
    }
}


@Composable
fun SoundsProvider(content: @Composable () -> Unit) {
    val context = LocalContext.current.applicationContext
    val player = remember(context) { SoundPoolSoundPlayer(context) }

    DisposableEffect(player) {
        onDispose { player.release() }
    }

    CompositionLocalProvider(
        LocalSoundPlayer provides player,
        content = content,
    )
}