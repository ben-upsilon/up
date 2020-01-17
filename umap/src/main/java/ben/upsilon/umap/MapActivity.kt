package ben.upsilon.umap

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import ben.upsilon.DataManager
import kotlinx.android.synthetic.main.activity_map.*

class MapActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        map.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        map.onResume()
        DataManager.with(this)
        Log.d("MapActivity", DataManager.keys().toString())
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
    }

    private fun whereIsMe() {

    }

}
