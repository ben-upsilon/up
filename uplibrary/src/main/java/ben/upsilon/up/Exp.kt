package ben.upsilon.up

import android.os.Handler
import android.os.Looper

class Exp {
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