package ben.upsilon.rss

import android.app.Activity
import android.os.Bundle
import android.widget.TextView

class MainActivity : Activity() {

    private var mTextMessage: TextView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mTextMessage = findViewById(R.id.message)
    }

}
