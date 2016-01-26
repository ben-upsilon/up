package ben.upsilon.exp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.R;

/**
 * Created by ben on 14/11/8.
 */
public class BaseActivity extends Activity {
    @ViewExp.id(R.id.addToDictionary)
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewExp.init(this);
    }
}
