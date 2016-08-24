package ben.upsilon.rss

import android.os.Bundle

import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.text.Html

import android.util.Log
import android.util.Xml
import android.view.View
import android.view.ViewGroup
import android.widget.TextView


import ben.upsilon.up.onAsync
import ben.upsilon.up.onUI

import org.xmlpull.v1.XmlPullParser
import java.net.URL

import java.util.*

data class RSSitem(val title: String, val link: String, val pubDate: String, val description: String)
data class RSS(val items: List<RSSitem>)
class RSSActivity : AppCompatActivity() {

    var rv: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rss)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        val fab = findViewById(R.id.fab) as FloatingActionButton
        fab.setOnClickListener { view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show() }
        rv = findViewById(R.id.rv) as RecyclerView?
        rv!!.layoutManager = LinearLayoutManager(this)

    }

    class RssAdapter(val rss_: RSS) : RecyclerView.Adapter<RssAdapter.RssViewHolder>() {
        override fun onCreateViewHolder(viewGroup: ViewGroup?, p1: Int): RssViewHolder? {
            return RssViewHolder(View.inflate(viewGroup!!.context, R.layout.item_rss, null))
        }

        override fun onBindViewHolder(holder: RssViewHolder?, p: Int) {
            holder!!.title.text = rss_.items[p].title
            holder.desc.text = Html.fromHtml(rss_.items[p].description)
        }

        override fun getItemCount(): Int {
            return rss_.items.size
        }

        class RssViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val title: TextView = itemView.findViewById(R.id.title) as TextView
            val desc: TextView = itemView.findViewById(R.id.desc) as TextView
        }
    }

    override fun onResume() {
        super.onResume()
        onAsync {
            val rss = URL("http://rss.cnbeta.com/rss").readText()
            val parser = Xml.newPullParser()
            parser.setInput(rss.byteInputStream(), "utf8")
            val rss_ = rssBuild(parser)

            for (rss__ in rss_.items) {
                Log.d("ben.upsilon", rss__.toString())
            }
            onUI {
                rv!!.adapter = RssAdapter(rss_)
            }
        }
    }

    private fun rssBuild(parser: XmlPullParser): RSS {
        val rss_list = ArrayList<RSSitem>()
        var title: String = ""
        var link: String = ""
        var pubDate: String = ""
        var description: String = ""
        var onItem: Boolean = false
        while (parser.next() != XmlPullParser.END_DOCUMENT) {

            //            Log.d("ben.upsilon", XmlPullParser.TYPES[parser.eventType] + parser.name);

            if (parser.eventType == XmlPullParser.START_TAG) {
                if (parser.name == "item") {
                    onItem = true
                    continue
                }
                if (parser.name == "title")
                    title = text(parser)
                if (parser.name == "link")
                    link = text(parser)
                if (parser.name == "pubDate")
                    pubDate = text(parser)
                if (parser.name == "description")
                    description = text(parser)


            }
            if (parser.eventType == XmlPullParser.END_TAG) {
                if (parser.name == "item" && onItem) {

                    rss_list.add(RSSitem(title, link, pubDate, description))
                    onItem = false
                    continue

                }
            }
        }
        return RSS(rss_list)
    }

    private fun text(parser: XmlPullParser): String {
        var result = ""
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.text
            parser.nextTag()
        }
        return result
    }
}
