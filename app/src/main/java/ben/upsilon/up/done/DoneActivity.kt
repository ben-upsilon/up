package ben.upsilon.up.done;

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import ben.upsilon.up.Exp
import ben.upsilon.up.R
import ben.upsilon.up.done.DoneItemAdapter.OnListInteractionListener
import java.util.*

class DoneActivity : AppCompatActivity() {
    @Exp.id(R.id.done_list)
    val mRecyclerView: RecyclerView? = null;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_done);
        val toolbar = findViewById(R.id.toolbar) as Toolbar;
        setSupportActionBar(toolbar);
        val fab: FloatingActionButton = findViewById(R.id.fab) as FloatingActionButton;
        fab.setOnClickListener({ view -> Snackbar.make(view, "omg", Snackbar.LENGTH_INDEFINITE).setAction("action", null).show() })
        Exp.init(this);
    }

    var mListener: OnListInteractionListener? = null

    override fun onResume() {
        super.onResume()
        mRecyclerView?.layoutManager = LinearLayoutManager(this)
        val items: ArrayList<DoneItem> = arrayListOf(DoneItem("1", "omg"), DoneItem("1", "omg"), DoneItem("1", "omg"), DoneItem("1", "omg"), DoneItem("1", "omg"), DoneItem("1", "omg"))
        val adapter: DoneItemAdapter = DoneItemAdapter(items, mListener)
        mRecyclerView?.adapter = adapter

    }
}




