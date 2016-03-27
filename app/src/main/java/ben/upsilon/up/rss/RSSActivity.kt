package ben.upsilon.up.rss

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.util.Xml
import android.view.View

import java.util.concurrent.Executors

import ben.upsilon.up.R
import ben.upsilon.up.runAsync
import org.xmlpull.v1.XmlPullParser
import java.net.URL
import java.util.*

data class RSSitem(val title: String, val link: String, val pubDate: Date, val description: String)
data class RSS(val channel: String, val title: String, val link: String, val pubDate: Date, val items: List<RSSitem>)
class RSSActivity : AppCompatActivity()  {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rss)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        val fab = findViewById(R.id.fab) as FloatingActionButton
        fab.setOnClickListener { view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show() }
    }

    override fun onResume() {
        super.onResume()
        runAsync {
            val rss = URL("http://rss.cnbeta.com/rss").openStream();
            val pull = Xml.newPullParser();
            pull.setInput(rss, "utf8")
            pull.next();
            Log.d("ben.upsilon", rss.toString());
        }
    }

}
