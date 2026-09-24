package com.madi.smarttask.core.util.speech

import android.content.Intent
import android.speech.RecognizerIntent
import java.util.Locale

object SpeechToTextHelper {
    fun createSpeechIntent(
        prompt: String? = null,
    ) : Intent {
        return Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            putExtra(RecognizerIntent.EXTRA_PROMPT, prompt)
        }
    }
}