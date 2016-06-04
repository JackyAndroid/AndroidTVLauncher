package com.jacky.launcher_42;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by Sebastiano Gottardo on 19/07/15.
 */
public class DemoFocusRelativeLayout extends RelativeLayout {

    private static final String TAG = "TestFrameLayout";

    public DemoFocusRelativeLayout(Context context) {
        super(context);
    }

    public DemoFocusRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DemoFocusRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DemoFocusRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public View focusSearch(View focused, int direction) {
        int nextViewId = -1;

        switch (direction) {
            case View.FOCUS_UP :
                nextViewId = focused.getNextFocusUpId();
                break;
            case View.FOCUS_DOWN :
                nextViewId = focused.getNextFocusDownId();
                break;
            case FOCUS_LEFT :
                nextViewId = focused.getNextFocusLeftId();
                break;
            case FOCUS_RIGHT :
                nextViewId = focused.getNextFocusRightId();
                break;
        }

        if (nextViewId < 0) {
            return super.focusSearch(focused, direction);
        }

        return findViewById(nextViewId);
    }
}
