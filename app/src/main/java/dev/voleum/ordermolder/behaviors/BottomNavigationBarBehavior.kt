package dev.voleum.ordermolder.behaviors

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.google.android.material.snackbar.Snackbar

class BottomNavigationBarBehavior<T : View>(context: Context, attrs: AttributeSet) : CoordinatorLayout.Behavior<T>(context, attrs) {

//    override fun onStartNestedScroll(
//            coordinatorLayout: CoordinatorLayout, child: T, directTargetChild: View, target: View, axes: Int, type: Int
//    ): Boolean {
//        return axes == ViewCompat.SCROLL_AXIS_VERTICAL
//    }
//
//    override fun onNestedPreScroll(
//            coordinatorLayout: CoordinatorLayout, child: T, target: View, dx: Int, dy: Int, consumed: IntArray, type: Int
//    ) {
//        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)
//        child.translationY = Math.max(0f, Math.min(child.height.toFloat(), child.translationY + dy))
//    }

    override fun layoutDependsOn(parent: CoordinatorLayout, child: T, dependency: View): Boolean {
        if (dependency is Snackbar.SnackbarLayout) {
            updateSnackbar(child, dependency);
        }
        return super.layoutDependsOn(parent, child, dependency)
    }

    private fun updateSnackbar(child: View, snackbarLayout: Snackbar.SnackbarLayout) {
            val params = snackbarLayout.layoutParams as CoordinatorLayout.LayoutParams

            params.anchorId = child.id
            params.anchorGravity = Gravity.TOP
            params.gravity = Gravity.TOP
            snackbarLayout.layoutParams = params
    }
}