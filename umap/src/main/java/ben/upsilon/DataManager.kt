package ben.upsilon

import android.app.Activity
import android.app.Application
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.lang.Exception
import java.lang.RuntimeException

object DataManager {
    private val TAG = "DataManager"
    private var mContext: Context? = null
    private var mSQLiteOpenHelper: SQLiteOpenHelper? = null
    private var mInit: DataManager? = null
    private var mDatabase: SQLiteDatabase? = null
    fun with(context: Context?) {
        when (context) {
            is Activity -> {
                mContext = context.applicationContext
            }
            is Application -> {
                mContext = context.applicationContext
            }
        }

        init()


    }

    private const val SQLVersion1 = "create table if not exists kvs (" +
            "id integer primary key autoincrement," +
            "key text not null," +
            "value text not null default '');"

    private fun init() {

        mSQLiteOpenHelper = object : SQLiteOpenHelper(mContext, "DataCacheManager.db", null, 1) {
            override fun onCreate(db: SQLiteDatabase?) {
                db?.execSQL(SQLVersion1)
                Log.d(TAG, "db path > ${db?.path}")
            }

            override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {


            }

        }
        Log.d(TAG, "databaseName > ${mSQLiteOpenHelper?.databaseName}")
        mDatabase = mSQLiteOpenHelper?.writableDatabase
    }

    fun set(key: String, value: String? = "") {
        checkContext()

        try {

        } catch (ex: Exception) {

        }
    }

    fun keys(): ArrayList<String> {
        checkContext()
        val r = arrayListOf<String>()
        var c: Cursor? = null
        try {
            c = mDatabase?.rawQuery("select 'key' from kvs", arrayOf())
            c?.let { cur ->
                while (cur.moveToNext()) {
                    r.add(cur.getString(cur.getColumnIndex("keys")))
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        } finally {
            c?.close()
        }
        return r
    }

    private fun checkContext() {
        if (mContext == null) {
            throw RuntimeException("just first invoke <DataManager.with(context)>")
        }
    }
}