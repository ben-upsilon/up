package ben.upsilon.rss

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.webkit.*
import kotlinx.android.synthetic.main.activity_web.*

class WebActivity : Activity() {

    private val TAG = "WebActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)
        config()
        btn_go.setOnClickListener {
            val url = input_url.text.toString()
            Log.d(TAG, "url > $url")
            web_view.loadUrl(url)
        }
    }

    private fun config() {

        val settings = web_view.settings
        settings.pluginState = WebSettings.PluginState.ON
        settings.javaScriptEnabled = true
        settings.supportMultipleWindows()
        settings.javaScriptCanOpenWindowsAutomatically = true
        //支持下viewport
        settings.loadWithOverviewMode = true
        settings.useWideViewPort = true
        //启用数据库
        settings.databaseEnabled = true
        val dir = this.applicationContext.getDir("database", Context.MODE_PRIVATE).path
        //启用地理定位
        settings.setGeolocationEnabled(true)
        //设置定位的数据库路径
        settings.setGeolocationDatabasePath(dir)
        //最重要的方法，一定要设置，这就是出不来的主要原因
        settings.domStorageEnabled = true
        settings.savePassword = false
        if (Build.VERSION.SDK_INT >= 21)
            settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW

        val ua = settings.userAgentString
        settings.userAgentString = ua + ";what-App-Android-version-xx"

        web_view.webViewClient = object : WebViewClient() {


        }


        web_view.webChromeClient = object : WebChromeClient() {

            override fun onShowFileChooser(webView: WebView?, filePathCallback: ValueCallback<Array<Uri>>?, fileChooserParams: FileChooserParams?): Boolean {

                Log.d(TAG, "onShowFileChooser $filePathCallback , $fileChooserParams")

                return super.onShowFileChooser(webView, filePathCallback, fileChooserParams)
            }


        }


    }


}
