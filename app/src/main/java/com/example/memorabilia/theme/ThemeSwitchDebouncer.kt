import android.os.Handler
import android.os.Looper

class ThemeSwitchDebouncer(private val debounceTime: Long = 300) {
    private val handler = Handler(Looper.getMainLooper())
    private var runnable: Runnable? = null

    fun debounce(action: () -> Unit) {
        runnable?.let { handler.removeCallbacks(it) }
        runnable = Runnable { action() }
        handler.postDelayed(runnable!!, debounceTime)
    }

    fun cancel() {
        handler.removeCallbacksAndMessages(null)
    }
}
