package com.cicada.sisi.animation

import android.animation.ObjectAnimator
import android.app.Activity
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AlphaAnimation
import android.widget.TextView

fun TextView.animateText(text: CharSequence, activity: Activity) {

    // solving changing a view element in UI Thread
    activity.runOnUiThread {

        val fadeInDuration = 1000L // in milliseconds
        val writeDuration = 2000L // in milliseconds

        // Create a fade in animation
        val fadeIn = AlphaAnimation(0f, 1f)
        fadeIn.duration = fadeInDuration

        // Create a type writer effect animation
        val typeWriter = TypingAnim(this, text)
        typeWriter.start()

        // Combine the animations with an ObjectAnimator
        val animator = ObjectAnimator.ofFloat(this, "alpha", 0f, 1f)
        animator.duration = fadeInDuration
        animator.startDelay = writeDuration
        animator.interpolator = AccelerateDecelerateInterpolator()

        // Start the animations
        this.startAnimation(fadeIn)
        animator.start()
    }
}