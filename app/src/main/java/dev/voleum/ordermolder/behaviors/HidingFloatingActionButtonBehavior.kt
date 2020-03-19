package dev.voleum.ordermolder.behaviors

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HidingFloatingActionButtonBehavior(context: Context, attrs: AttributeSet) : FloatingActionButton.Behavior(context, attrs) {

    override fun onStartNestedScroll(coordinatorLayout: CoordinatorLayout, child: FloatingActionButton, directTargetChild: View, target: View, axes: Int, type: Int): Boolean {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL
    }

    override fun onNestedScroll(coordinatorLayout: CoordinatorLayout, child: FloatingActionButton, target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int, type: Int, consumed: IntArray) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type, consumed)

        when {
            dyConsumed > 0  -> {
                val layoutParams = child.layoutParams as CoordinatorLayout.LayoutParams
                child.animate()
                        .translationY(child.height.toFloat() + layoutParams.bottomMargin)
                        .setInterpolator(LinearInterpolator())
                        .start()
            }
            dyConsumed < 0 -> {
                child.animate()
                        .translationY(0F)
                        .setInterpolator(LinearInterpolator())
                        .start()
            }
        }
    }
}