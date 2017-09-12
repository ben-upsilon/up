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
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
            }


            override fun shouldOverrideKeyEvent(view: WebView?, event: KeyEvent?): Boolean {
                return super.shouldOverrideKeyEvent(view, event)
            }

            override fun doUpdateVisitedHistory(view: WebView?, url: String?, isReload: Boolean) {
                super.doUpdateVisitedHistory(view, url, isReload)
            }

            override fun onReceivedError(view: WebView?, errorCode: Int, description: String?, failingUrl: String?) {
                super.onReceivedError(view, errorCode, description, failingUrl)
            }

            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                super.onReceivedError(view, request, error)
            }

            override fun onRenderProcessGone(view: WebView?, detail: RenderProcessGoneDetail?): Boolean {
                return super.onRenderProcessGone(view, detail)
            }

            override fun onReceivedLoginRequest(view: WebView?, realm: String?, account: String?, args: String?) {
                super.onReceivedLoginRequest(view, realm, account, args)
            }

            override fun onReceivedHttpError(view: WebView?, request: WebResourceRequest?, errorResponse: WebResourceResponse?) {
                super.onReceivedHttpError(view, request, errorResponse)
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
            }

            override fun onScaleChanged(view: WebView?, oldScale: Float, newScale: Float) {
                super.onScaleChanged(view, oldScale, newScale)
            }

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                return super.shouldOverrideUrlLoading(view, url)
            }

            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                return super.shouldOverrideUrlLoading(view, request)
            }

            override fun onPageCommitVisible(view: WebView?, url: String?) {
                super.onPageCommitVisible(view, url)
            }

            override fun onUnhandledKeyEvent(view: WebView?, event: KeyEvent?) {
                super.onUnhandledKeyEvent(view, event)
            }

            override fun onReceivedClientCertRequest(view: WebView?, request: ClientCertRequest?) {
                super.onReceivedClientCertRequest(view, request)
            }

            override fun onReceivedHttpAuthRequest(view: WebView?, handler: HttpAuthHandler?, host: String?, realm: String?) {
                super.onReceivedHttpAuthRequest(view, handler, host, realm)
            }

            override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
                super.onReceivedSslError(view, handler, error)
            }

            override fun onTooManyRedirects(view: WebView?, cancelMsg: Message?, continueMsg: Message?) {
                super.onTooManyRedirects(view, cancelMsg, continueMsg)
            }

            override fun onFormResubmission(view: WebView?, dontResend: Message?, resend: Message?) {
                super.onFormResubmission(view, dontResend, resend)
            }

            override fun onLoadResource(view: WebView?, url: String?) {
                super.onLoadResource(view, url)
            }
        }


        web_view.webChromeClient = object : WebChromeClient() {
            override fun onRequestFocus(view: WebView?) {
                super.onRequestFocus(view)
            }

            override fun onJsAlert(view: WebView?, url: String?, message: String?, result: JsResult?): Boolean {
                return super.onJsAlert(view, url, message, result)
            }

            override fun onJsPrompt(view: WebView?, url: String?, message: String?, defaultValue: String?, result: JsPromptResult?): Boolean {
                return super.onJsPrompt(view, url, message, defaultValue, result)
            }

            override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
                super.onShowCustomView(view, callback)
            }

            override fun onShowCustomView(view: View?, requestedOrientation: Int, callback: CustomViewCallback?) {
                super.onShowCustomView(view, requestedOrientation, callback)
            }

            override fun onGeolocationPermissionsShowPrompt(origin: String?, callback: GeolocationPermissions.Callback?) {
                super.onGeolocationPermissionsShowPrompt(origin, callback)
            }

            override fun onPermissionRequest(request: PermissionRequest?) {
                super.onPermissionRequest(request)
            }

            override fun onConsoleMessage(message: String?, lineNumber: Int, sourceID: String?) {
                super.onConsoleMessage(message, lineNumber, sourceID)
            }

            override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
                return super.onConsoleMessage(consoleMessage)
            }

            override fun onPermissionRequestCanceled(request: PermissionRequest?) {
                super.onPermissionRequestCanceled(request)
            }

            override fun onShowFileChooser(webView: WebView?, filePathCallback: ValueCallback<Array<Uri>>?, fileChooserParams: FileChooserParams?): Boolean {

                Log.d(TAG, "onShowFileChooser $filePathCallback , $fileChooserParams")

                return super.onShowFileChooser(webView, filePathCallback, fileChooserParams)
            }

            override fun onReceivedTouchIconUrl(view: WebView?, url: String?, precomposed: Boolean) {
                super.onReceivedTouchIconUrl(view, url, precomposed)
            }

            override fun onReceivedIcon(view: WebView?, icon: Bitmap?) {
                super.onReceivedIcon(view, icon)
            }

            override fun onExceededDatabaseQuota(url: String?, databaseIdentifier: String?, quota: Long, estimatedDatabaseSize: Long, totalQuota: Long, quotaUpdater: WebStorage.QuotaUpdater?) {
                super.onExceededDatabaseQuota(url, databaseIdentifier, quota, estimatedDatabaseSize, totalQuota, quotaUpdater)
            }

            override fun onReceivedTitle(view: WebView?, title: String?) {
                super.onReceivedTitle(view, title)
            }

            override fun onReachedMaxAppCacheSize(requiredStorage: Long, quota: Long, quotaUpdater: WebStorage.QuotaUpdater?) {
                super.onReachedMaxAppCacheSize(requiredStorage, quota, quotaUpdater)
            }

            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
            }

            override fun onJsConfirm(view: WebView?, url: String?, message: String?, result: JsResult?): Boolean {
                return super.onJsConfirm(view, url, message, result)
            }

            override fun getVisitedHistory(callback: ValueCallback<Array<String>>?) {
                super.getVisitedHistory(callback)
            }

            override fun getVideoLoadingProgressView(): View {
                return super.getVideoLoadingProgressView()
            }

            override fun onGeolocationPermissionsHidePrompt() {
                super.onGeolocationPermissionsHidePrompt()
            }

            override fun getDefaultVideoPoster(): Bitmap {
                return super.getDefaultVideoPoster()
            }

            override fun onJsBeforeUnload(view: WebView?, url: String?, message: String?, result: JsResult?): Boolean {
                return super.onJsBeforeUnload(view, url, message, result)
            }

            override fun onHideCustomView() {
                super.onHideCustomView()
            }

            override fun onCreateWindow(view: WebView?, isDialog: Boolean, isUserGesture: Boolean, resultMsg: Message?): Boolean {
                return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg)
            }

            override fun onCloseWindow(window: WebView?) {
                super.onCloseWindow(window)
            }

            override fun onJsTimeout(): Boolean {
                return super.onJsTimeout()
            }
        }


    }


}
