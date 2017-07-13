package ben.upsilon.syncup

import android.app.Activity
import android.content.Context
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem

import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

import java.net.ServerSocket

class MainActivity : Activity() {

    private lateinit var nsdManager: NsdManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        nsdManager = applicationContext.getSystemService(Context.NSD_SERVICE) as NsdManager
        reg.setOnClickListener { registerService() }
        dis.setOnClickListener { discoverService() }
    }

    override fun onBackPressed() {

        super.onBackPressed()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId



        return super.onOptionsItemSelected(item)
    }


    private var nsRegListener: NsdManager.RegistrationListener? = null

    fun registerService() {
        // 注意：注冊网络服务时不要对端口进行硬编码，通过例如以下这样的方式为你的网络服务获取
        // 一个可用的端口号.
        var port = 0
        try {
            val sock = ServerSocket(0)
            port = sock.localPort
            sock.close()
        } catch (e: Exception) {
            Toast.makeText(applicationContext, "can not set port", Toast.LENGTH_SHORT)
        }

        // 注冊网络服务的名称、类型、端口
        val nsdServiceInfo = NsdServiceInfo()
        nsdServiceInfo.serviceName = SyncUp.ServiceName
        nsdServiceInfo.serviceType = SyncUp.ServiceType
        nsdServiceInfo.port = port

        // 实现一个网络服务的注冊事件监听器。监听器的对象应该保存起来以便之后进行注销
        nsRegListener = object : NsdManager.RegistrationListener {
            override fun onUnregistrationFailed(arg0: NsdServiceInfo, arg1: Int) {
                Toast.makeText(applicationContext, "Unregistration Failed", Toast.LENGTH_SHORT).show()
            }

            override fun onServiceUnregistered(arg0: NsdServiceInfo) {
                Toast.makeText(applicationContext, "Service Unregistered", Toast.LENGTH_SHORT).show()
            }

            override fun onServiceRegistered(arg0: NsdServiceInfo) {
                Toast.makeText(applicationContext, "Service Registered", Toast.LENGTH_SHORT).show()
            }

            override fun onRegistrationFailed(arg0: NsdServiceInfo, arg1: Int) {
                Toast.makeText(applicationContext, "Registration Failed", Toast.LENGTH_SHORT).show()
            }
        }

        // 获取系统网络服务管理器，准备之后进行注冊
        nsdManager.registerService(nsdServiceInfo, NsdManager.PROTOCOL_DNS_SD, nsRegListener)

    }

    private var nsDicListener: NsdManager.DiscoveryListener? = null

    fun discoverService() {
        nsDicListener = object : NsdManager.DiscoveryListener {
            override fun onStopDiscoveryFailed(serviceType: String, errorCode: Int) {
                runOnUiThread { log.text = "${log.text}\n onStopDiscoveryFailed > $serviceType" }
            }

            override fun onStartDiscoveryFailed(serviceType: String, errorCode: Int) {
                runOnUiThread { log.text = "${log.text}\n onStartDiscoveryFailed > $serviceType" }
            }

            override fun onServiceLost(serviceInfo: NsdServiceInfo) {
                runOnUiThread { log.text = "${log.text}\n onServiceLost > $serviceInfo" }
            }

            override fun onServiceFound(serviceInfo: NsdServiceInfo) {
                // 发现网络服务时就会触发该事件
                // 能够通过switch或if获取那些你真正关心的服务

                runOnUiThread { log.text = "${log.text}\n onServiceFound > $serviceInfo" }

                initResolveListener(serviceInfo)

            }

            override fun onDiscoveryStopped(serviceType: String) {
                runOnUiThread { log.text = "${log.text}\n onDiscoveryStopped > $serviceType" }

            }

            override fun onDiscoveryStarted(serviceType: String) {
                runOnUiThread { log.text = "${log.text}\n onDiscoveryStarted > $serviceType" }

            }
        }
        nsdManager.discoverServices(SyncUp.ServiceType, NsdManager.PROTOCOL_DNS_SD, nsDicListener)
    }

    private var nsResolveListener: NsdManager.ResolveListener? = null

    fun initResolveListener(serviceInfo: NsdServiceInfo) {
        nsResolveListener = object : NsdManager.ResolveListener {
            override fun onServiceResolved(info: NsdServiceInfo) {
                runOnUiThread { log.text = "${log.text}\n onServiceResolved > $info" }
            }

            override fun onResolveFailed(info: NsdServiceInfo, errorCode: Int) {}

        }
        nsdManager.resolveService(serviceInfo, nsResolveListener)

    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onPause() {
        super.onPause()
        unregisterService()
    }

    fun unregisterService() {
        nsdManager.stopServiceDiscovery(nsDicListener) // 关闭网络发现
        nsdManager.unregisterService(nsRegListener)    // 注销网络服务
    }
}
