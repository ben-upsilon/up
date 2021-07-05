package ben.upsilon.up.done

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import ben.upsilon.up.R
import ben.upsilon.up.done.DoneItemAdapter.OnListInteractionListener

class DoneActivity : FragmentActivity() {

    val TAG = javaClass.simpleName
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_done)
        val fragmentA = BlankFragment.newInstance("A", "B")
        val fragmentB = BlankFragment.newInstance("C", "D")

        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()

        ft.add(R.id.layout,fragmentA)
        ft.commit()



    }

    var mListener: OnListInteractionListener? = null

}




