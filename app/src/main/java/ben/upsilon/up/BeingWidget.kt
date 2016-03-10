package ben.upsilon.up

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.RemoteViews

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Collections
import java.util.Date
import java.util.EnumSet
import java.util.LinkedHashMap
import java.util.Locale
import java.util.concurrent.TimeUnit

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in [BeingWidgetConfigureActivity]
 */
class BeingWidget : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        // There may be multiple widgets active, so update all of them
        Log.d(TAG, "onUpdate")
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        // When the user deletes the widget, delete the preference associated with it.
        Log.d(TAG, "onDeleted")
        //        for (int appWidgetId : appWidgetIds) {
        //            BeingWidgetConfigureActivity.Companion.deleteTitlePref(context, appWidgetId);
        //        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
        Log.d(TAG, "onUpdate")
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
        Log.d(TAG, "onDisabled")
    }

    override fun onAppWidgetOptionsChanged(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int, newOptions: Bundle) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions)
        Log.d(TAG, "onAppWidgetOptionsChanged")
    }

    companion object {

        private val TAG = BeingWidget::class.java.simpleName

        internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager,
                                     appWidgetId: Int) {
            Log.d(TAG, "updateAppWidget")

            try {
                val start = SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.CHINA).parse("2016-01-28 08:25")
                Log.d(TAG, "updateAppWidget: start >" + start)
                Log.d(TAG, "updateAppWidget: now >" + Date())
                val r = computeDiff(start, Date())
                Log.d(TAG, "updateAppWidget: " + r.toString())

                val widgetText = context.getString(R.string.appwidget_text, r[TimeUnit.DAYS], r[TimeUnit.HOURS], r[TimeUnit.MINUTES])

                // Construct the RemoteViews object
                val views = RemoteViews(context.packageName, R.layout.being_widget)
                views.setTextViewText(R.id.appwidget_text, widgetText)

                // Instruct the widget manager to update the widget
                appWidgetManager.updateAppWidget(appWidgetId, views)
            } catch (e: ParseException) {
                e.printStackTrace()
            }


        }

        fun computeDiff(date1: Date, date2: Date): Map<TimeUnit, Long> {
            val diffInMillies = date2.time - date1.time
            val units = ArrayList(EnumSet.allOf(TimeUnit::class.java))
            Collections.reverse(units)
            val result = LinkedHashMap<TimeUnit, Long>()
            var milliesRest = diffInMillies
            for (unit in units) {
                val diff = unit.convert(milliesRest, TimeUnit.MILLISECONDS)
                val diffInMilliesForUnit = unit.toMillis(diff)
                milliesRest -= diffInMilliesForUnit

                result.put(unit, diff)
            }
            return result
        }
    }
}

