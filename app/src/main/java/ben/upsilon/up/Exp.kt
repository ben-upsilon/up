package ben.upsilon.up

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * Created by ben on 3/27/16.
 */
object Exp {


    private val LogTag = Exp::class.java.canonicalName

    fun init(_context: Activity) {
        val fields = _context.javaClass.declaredFields
        if (null != fields && fields.size > 0) {
            for (field in fields) {
                Log.d(LogTag, field.toGenericString())
                val _id = field.getAnnotation(id::class.java)
                if (null != _id) {
                    val view = _context.findViewById(_id.value)
                    if (null != view) {
                        field.isAccessible = true
                        try {

                            field.set(_context, view)
                        } catch (e: IllegalAccessException) {
                            e.printStackTrace()
                        }

                    }
                }
            }
        }
    }

    fun init(_context: View) {
        val fields = _context.javaClass.declaredFields
        if (null != fields && fields.size > 0) {
            for (field in fields) {
                Log.d(LogTag, field.toGenericString())
                val _id = field.getAnnotation(id::class.java)
                if (null != _id) {
                    val view = _context.findViewById(_id.value)
                    if (null != view) {
                        field.isAccessible = true
                        try {
                            field.set(_context, view)
                        } catch (e: IllegalAccessException) {
                            e.printStackTrace()
                        }

                    }
                }
            }
        }
    }

    @Target(AnnotationTarget.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    annotation class id(val value: Int = -1)

    fun dump() {

    }



}

fun onAsync(action: () -> Unit) {
    Thread(Runnable(action)).start()
}

fun onUI(action: () -> Unit) {
    Handler(Looper.getMainLooper()).post(Runnable(action))
}

fun runDelayed(delayMillis: Long, action: () -> Unit) {
    Handler().postDelayed(Runnable(action), delayMillis)
}

fun runDelayedOnUiThread(delayMillis: Long, action: () -> Unit) {
    Handler(Looper.getMainLooper()).postDelayed(Runnable(action), delayMillis)
}