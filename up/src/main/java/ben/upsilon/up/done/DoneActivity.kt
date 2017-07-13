package ben.upsilon.up.done

import android.app.Activity
import android.app.Fragment
import android.app.FragmentManager
import android.app.FragmentTransaction
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import ben.upsilon.up.R
import ben.upsilon.up.done.DoneItemAdapter.OnListInteractionListener
import java.util.*

class DoneActivity : Activity() {

    val TAG = javaClass.simpleName
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_done)
        val fragmentA=BlankFragment.newInstance("A","B")
        val fragmentB=BlankFragment.newInstance("C","D")

        val ft:FragmentTransaction  = fragmentManager.beginTransaction()

        ft.add(R.id.layout,fragmentA)
        ft.commit()



    }

    var mListener: OnListInteractionListener? = null

    override fun onResume() {
        super.onResume()


    }
}




