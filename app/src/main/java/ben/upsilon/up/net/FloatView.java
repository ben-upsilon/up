package ben.upsilon.up.net;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

/**
 * 浮动view
 * Created by ben on 1/10/16.
 */
public class FloatView extends View {
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mLayoutParams;
    private LayoutInflater mLayoutInflater;
    private View mFloatView;

    public FloatView(Context context) {
        super(context);
    }

    public void show() {

    }
}
