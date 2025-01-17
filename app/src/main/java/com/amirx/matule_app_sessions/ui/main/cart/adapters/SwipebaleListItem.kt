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

class SwipeableListItem(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var startX = 0f
    private var currentSwipeDistance = 0f
    private val swipeThreshold = 0.5f
    private val maxSwipeDistance = 200f
    private val contentView: ViewGroup = itemView.findViewById(R.id.content_view)
    private val leftActionsView: ViewGroup = itemView.findViewById(R.id.left_actions)
    private val rightActions: ViewGroup = itemView.findViewById(R.id.right_actions)

    init {
        setTouchListener()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setTouchListener() {
        itemView.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    startX = motionEvent.rawX
                    true
                }

                MotionEvent.ACTION_MOVE -> {
                    val delta = motionEvent.rawX - startX
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
        currentSwipeDistance = min(maxSwipeDistance, max(-maxSwipeDistance, delta))

        contentView.translationX = currentSwipeDistance

        if (currentSwipeDistance > 0) {
            leftActionsView.visibility = View.VISIBLE
            rightActions.visibility = View.GONE
        } else {
            leftActionsView.visibility = View.GONE
            rightActions.visibility = View.VISIBLE
        }
        val progress = abs(currentSwipeDistance) / maxSwipeDistance

        if (currentSwipeDistance > 0) {
            leftActionsView.alpha = progress
        } else {
            rightActions.alpha = progress
        }
    }

    private fun handleSwipeRelease() {
        val progress = abs(currentSwipeDistance) / maxSwipeDistance

        if (progress > swipeThreshold) {
            animateSwipe(
                currentSwipeDistance,
                if (currentSwipeDistance > 0) maxSwipeDistance else -maxSwipeDistance
            )
        } else {
            animateSwipe(currentSwipeDistance, 0f)
        }
    }

    private fun animateSwipe(from: Float, to: Float) {
        ValueAnimator.ofFloat(from, to).apply {
            duration = 200
            addUpdateListener { animator ->
                val delta = animator.animatedValue as Float
                handleSwipe(delta)
            }
            start()
        }
    }
}