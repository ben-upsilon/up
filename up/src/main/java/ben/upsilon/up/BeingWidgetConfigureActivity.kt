package ben.upsilon.up

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * The configuration screen for the [BeingWidget] AppWidget.
 */
class BeingWidgetConfigureActivity : Activity() {
    internal var mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID

    public override fun onCreate(icicle: Bundle?) {
        super.onCreate(icicle)
        try {
            BeingDateTime = SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.CHINA).parse("2016-01-28 08:25")
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(Activity.RESULT_CANCELED)

        setContentView(R.layout.being_widget_configure)
        var mAppWidgetText = findViewById(R.id.appwidget_text) as EditText
        var mOnClickListener: View.OnClickListener = View.OnClickListener {
            val context = this@BeingWidgetConfigureActivity

            // When the button is clicked, store the string locally
            val widgetText = mAppWidgetText.text.toString()
            saveTitlePref(context, mAppWidgetId, widgetText)

            // It is the responsibility of the configuration activity to update the app widget
            val appWidgetManager = AppWidgetManager.getInstance(context)
            BeingWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId)

            // Make sure we pass back the original appWidgetId
            val resultValue = Intent()
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId)
            setResult(Activity.RESULT_OK, resultValue)
            finish()
        }
        findViewById<View>(R.id.add_button).setOnClickListener(mOnClickListener)

        // Find the widget id from the intent.
        val intent = intent
        val extras = intent.extras
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish()
            return
        }

        mAppWidgetText.setText(loadTitlePref(this@BeingWidgetConfigureActivity, mAppWidgetId))
    }

    companion object {
        private val PREFS_NAME = "ben.upsilon.up.BeingWidget"
        private val PREF_PREFIX_KEY = "appwidget_"
        private var BeingDateTime: Date? = null

        // Write the prefix to the SharedPreferences object for this widget
        internal fun saveTitlePref(context: Context, appWidgetId: Int, text: String) {
            val prefs = context.getSharedPreferences(PREFS_NAME, 0).edit()
            prefs.putString(PREF_PREFIX_KEY + appWidgetId, text)
            prefs.apply()
        }

        // Read the prefix from the SharedPreferences object for this widget.
        // If there is no preference saved, get the default from a resource
        internal fun loadTitlePref(context: Context, appWidgetId: Int): String {
            val prefs = context.getSharedPreferences(PREFS_NAME, 0)
            val titleValue = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null)
            if (titleValue != null) {
                return titleValue
            } else {
                return context.getString(R.string.appwidget_text)
            }
        }

        internal fun deleteTitlePref(context: Context, appWidgetId: Int) {
            val prefs = context.getSharedPreferences(PREFS_NAME, 0).edit()
            prefs.remove(PREF_PREFIX_KEY + appWidgetId)
            prefs.apply()
        }
    }

}

