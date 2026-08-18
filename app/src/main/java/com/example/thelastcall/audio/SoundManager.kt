package com.example.thelastcall.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlin.math.sin

class SoundManager(private val context: Context) {
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Default + job)

    private val vibrator: Vibrator? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
        vibratorManager?.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
    }

    private var streamingTrack: AudioTrack? = null
    private val sampleRate = 44100

    init {
        try {
            val minBufferSize = AudioTrack.getMinBufferSize(
                sampleRate,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT
            ).coerceAtLeast(sampleRate / 4)

            streamingTrack = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_GAME)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(sampleRate)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                )
                .setBufferSizeInBytes(minBufferSize)
                .setTransferMode(AudioTrack.MODE_STREAM)
                .build()

            streamingTrack?.play()
        } catch (_: Exception) {
            streamingTrack = null
        }
    }

    fun playUiClick(soundEnabled: Boolean, hapticsEnabled: Boolean) {
        if (hapticsEnabled) triggerVibration(15, 60)
        if (soundEnabled) {
            scope.launch {
                writeTone(frequency = 700.0, durationMs = 35, attackMs = 2, decayMs = 25, volume = 0.25f)
            }
        }
    }

    fun playEvidenceDiscovered(soundEnabled: Boolean, hapticsEnabled: Boolean) {
        if (hapticsEnabled) triggerVibration(35, 120)
        if (soundEnabled) {
            scope.launch {
                // Two-tone rising discovery chime
                writeTone(frequency = 587.33, durationMs = 80, attackMs = 5, decayMs = 60, volume = 0.35f)
                writeTone(frequency = 880.0, durationMs = 180, attackMs = 5, decayMs = 150, volume = 0.45f)
            }
        }
    }

    fun playContradiction(soundEnabled: Boolean, hapticsEnabled: Boolean) {
        if (hapticsEnabled) {
            triggerVibrationPattern(longArrayOf(0, 80, 50, 120), intArrayOf(0, 200, 0, 255))
        }
        if (soundEnabled) {
            scope.launch {
                // Dramatic low tension chord
                writeTone(frequency = 164.81, durationMs = 300, attackMs = 10, decayMs = 260, volume = 0.55f)
            }
        }
    }

    fun playDeductionFormed(soundEnabled: Boolean, hapticsEnabled: Boolean) {
        if (hapticsEnabled) triggerVibration(40, 100)
        if (soundEnabled) {
            scope.launch {
                writeTone(frequency = 440.0, durationMs = 70, attackMs = 5, decayMs = 50, volume = 0.35f)
                writeTone(frequency = 554.37, durationMs = 70, attackMs = 5, decayMs = 50, volume = 0.35f)
                writeTone(frequency = 659.25, durationMs = 200, attackMs = 5, decayMs = 160, volume = 0.45f)
            }
        }
    }

    fun playCaseSolved(soundEnabled: Boolean, hapticsEnabled: Boolean) {
        if (hapticsEnabled) {
            triggerVibrationPattern(longArrayOf(0, 100, 80, 200), intArrayOf(0, 180, 0, 255))
        }
        if (soundEnabled) {
            scope.launch {
                writeTone(frequency = 523.25, durationMs = 100, attackMs = 5, decayMs = 80, volume = 0.4f)
                writeTone(frequency = 659.25, durationMs = 100, attackMs = 5, decayMs = 80, volume = 0.4f)
                writeTone(frequency = 783.99, durationMs = 100, attackMs = 5, decayMs = 80, volume = 0.45f)
                writeTone(frequency = 1046.50, durationMs = 400, attackMs = 10, decayMs = 350, volume = 0.6f)
            }
        }
    }

    private fun triggerVibration(durationMs: Long, amplitude: Int) {
        try {
            vibrator?.let {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    it.vibrate(VibrationEffect.createOneShot(durationMs, amplitude.coerceIn(1, 255)))
                } else {
                    @Suppress("DEPRECATION")
                    it.vibrate(durationMs)
                }
            }
        } catch (_: Exception) {}
    }

    private fun triggerVibrationPattern(timings: LongArray, amplitudes: IntArray) {
        try {
            vibrator?.let {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    it.vibrate(VibrationEffect.createWaveform(timings, amplitudes, -1))
                } else {
                    @Suppress("DEPRECATION")
                    it.vibrate(timings, -1)
                }
            }
        } catch (_: Exception) {}
    }

    @Synchronized
    private fun writeTone(frequency: Double, durationMs: Int, attackMs: Int, decayMs: Int, volume: Float) {
        try {
            val track = streamingTrack ?: return
            if (track.playState != AudioTrack.PLAYSTATE_PLAYING) {
                track.play()
            }

            val numSamples = (sampleRate * (durationMs / 1000.0)).toInt().coerceAtLeast(1)
            val samples = ShortArray(numSamples)

            val attackSamples = (sampleRate * (attackMs / 1000.0)).toInt().coerceAtLeast(1)
            val decaySamples = (sampleRate * (decayMs / 1000.0)).toInt().coerceAtLeast(1)
            val sustainSamples = (numSamples - attackSamples - decaySamples).coerceAtLeast(0)

            for (i in 0 until numSamples) {
                val envelope = when {
                    i < attackSamples -> i.toFloat() / attackSamples
                    i < attackSamples + sustainSamples -> 1.0f
                    else -> {
                        val decayProgress = (i - attackSamples - sustainSamples).toFloat() / decaySamples
                        (1.0f - decayProgress).coerceAtLeast(0.0f)
                    }
                }
                val sampleValue = (sin(2.0 * Math.PI * i.toDouble() / (sampleRate / frequency)) * 32767.0 * volume * envelope).toInt()
                samples[i] = sampleValue.coerceIn(-32768, 32767).toShort()
            }

            track.write(samples, 0, samples.size)
        } catch (_: Exception) {
            // Audio fallback - silent
        }
    }

    fun release() {
        try {
            job.cancel()
            streamingTrack?.apply {
                if (playState == AudioTrack.PLAYSTATE_PLAYING) {
                    stop()
                }
                release()
            }
            streamingTrack = null
        } catch (_: Exception) {}
    }
}
