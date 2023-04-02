package com.cicada.sisi.animation

import android.os.CountDownTimer
import android.widget.TextView

class TypingAnim(private val textView: TextView, private val text: CharSequence) :
    CountDownTimer(text.length.toLong() * 36, 36) {

    private var index = 0

    override fun onTick(millisUntilFinished: Long) {

        textView.text = text.subSequence(0, index++)
    }

    override fun onFinish() {

        textView.text = text
    }
}