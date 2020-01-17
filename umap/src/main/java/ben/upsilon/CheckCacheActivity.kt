package ben.upsilon

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ben.upsilon.umap.R

class CheckCacheActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_cache)
        DataManager.with(this)

    }

}
