package com.amirx.matule_app_sessions.ui.main.cart.adapters

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amirx.matule_app_sessions.R
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class SwipebaleListItem(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var startX = 0f
    private var currentDistance = 0f
    private val maxSwipeDistance = 200f
    private val swipeThread = 0.5f
    private val contentView: ViewGroup = itemView.findViewById(R.id.content_view)
    private val leftActions: ViewGroup = itemView.findViewById(R.id.left_actions)
    private val rightActions: ViewGroup = itemView.findViewById(R.id.right_actions)


    init {
        setTouchListener()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setTouchListener() {
        itemView.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    startX = event.rawX
                    true
                }

                MotionEvent.ACTION_MOVE -> {
                    val delta = event.rawX - startX
                    handleSwipe(delta)
                    true
                }

                MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                    handleSwipeRelease()
                    true
                }

                else -> false
            }
        }
    }


    private fun handleSwipe(delta: Float) {
        currentDistance = min(maxSwipeDistance, max(-maxSwipeDistance, delta))

        contentView.translationX = currentDistance

        if (currentDistance > 0) {
            leftActions.visibility = View.VISIBLE
            rightActions.visibility = View.GONE
        } else {
            leftActions.visibility = View.GONE
            rightActions.visibility = View.VISIBLE
        }

        val progress = abs(currentDistance) / maxSwipeDistance

        if (currentDistance > 0) {
            leftActions.alpha = progress
        } else {
            rightActions.alpha = progress
        }
    }

    private fun handleSwipeRelease() {
        val progress = abs(currentDistance) / maxSwipeDistance

        if (progress > swipeThread) {
            animateSwipe(
                currentDistance,
                if (currentDistance > 0) maxSwipeDistance else -maxSwipeDistance
            )
        } else {
            animateSwipe(currentDistance, 0f)
        }
    }

    private fun animateSwipe(from: Float, to: Float) {
        ValueAnimator.ofFloat(from, to).apply {
            duration = 200
            addUpdateListener { animator ->
                val delta = animator.animatedValue as Float
                handleSwipe(delta)
            }
        }
    }
}