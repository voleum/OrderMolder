package dev.voleum.ordermolder.behaviors;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.snackbar.Snackbar;

public class BottomNavigationBarBehavior extends CoordinatorLayout.Behavior {

    public BottomNavigationBarBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(@NonNull CoordinatorLayout parent, @NonNull View child, @NonNull View dependency) {
        if (dependency.getClass().equals(Snackbar.SnackbarLayout.class)) {
            updateSnackbar(child, dependency);
        }
        return super.layoutDependsOn(parent, child, dependency);
    }

    private void updateSnackbar(View child, View snackbarLayout) {
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) snackbarLayout.getLayoutParams();
        layoutParams.setAnchorId(child.getId());
        layoutParams.anchorGravity = Gravity.TOP;
        layoutParams.gravity = Gravity.TOP;
        snackbarLayout.setLayoutParams(layoutParams);
    }
}
