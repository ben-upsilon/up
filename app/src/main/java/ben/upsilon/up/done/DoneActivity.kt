package ben.upsilon.up.done

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import ben.upsilon.up.R
import ben.upsilon.up.done.DoneItemAdapter.OnListInteractionListener
import com.wilddog.client.*
import com.wilddog.wilddogcore.WilddogApp
import com.wilddog.wilddogcore.WilddogOptions
import java.util.*

class DoneActivity : AppCompatActivity() {
    val TAG = javaClass.simpleName
    val mRecyclerView: RecyclerView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_done)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        val fab: FloatingActionButton = findViewById(R.id.fab) as FloatingActionButton
        fab.setOnClickListener({ view -> Snackbar.make(view, "omg", Snackbar.LENGTH_INDEFINITE).setAction("action", null).show() })
    }

    var mListener: OnListInteractionListener? = null

    override fun onResume() {
        super.onResume()
        mRecyclerView?.layoutManager = LinearLayoutManager(this)
        val items: ArrayList<DoneItem> = arrayListOf(DoneItem("1", "omg"), DoneItem("1", "omg"), DoneItem("1", "omg"), DoneItem("1", "omg"), DoneItem("1", "omg"), DoneItem("1", "omg"))
        val adapter: DoneItemAdapter = DoneItemAdapter(items, mListener)
        mRecyclerView?.adapter = adapter


        val options = WilddogOptions.Builder().setSyncUrl("https://nowup.wilddogio.com").build()
        WilddogApp.initializeApp(this, options)
        val ref = WilddogSync.getInstance().reference
        ref.addChildEventListener(object : ChildEventListener {
            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
            }

            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
            }

            override fun onChildRemoved(p0: DataSnapshot?) {
            }

            override fun onCancelled(p0: SyncError?) {
            }
        })

        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: SyncError?) {
            }

            override fun onDataChange(p0: DataSnapshot?) {
                Log.d(TAG, p0.toString())
            }
        })
    }
}




