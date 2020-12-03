package ben.upsilon.umap

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import ben.upsilon.DataManager
import ben.upsilon.umap.databinding.ActivityMapBinding
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.amap.api.maps.AMap
import com.amap.api.maps.AMapOptions
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.model.CameraPosition
import com.amap.api.maps.model.MyLocationStyle
import com.amap.api.services.busline.BusStationQuery
import com.amap.api.services.busline.BusStationSearch
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.district.DistrictSearch
import com.amap.api.services.district.DistrictSearchQuery
import com.amap.api.services.geocoder.GeocodeResult
import com.amap.api.services.geocoder.GeocodeSearch
import com.amap.api.services.geocoder.RegeocodeQuery
import com.amap.api.services.geocoder.RegeocodeResult


class MapActivity : AppCompatActivity() {
    private val TAG = "uMap"


    private var mMap: AMap? = null
    private var mCityCode: String? = ""
    private var mCityName: String? = ""
    private var mZoomLevel: Float = -1f

    private lateinit var binding: ActivityMapBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.map.onCreate(savedInstanceState)
        mMap = binding.map.map
        mMap?.uiSettings?.logoPosition = AMapOptions.LOGO_POSITION_BOTTOM_CENTER
        mMap?.setOnMyLocationChangeListener { location ->
            initGeoCoder(LatLonPoint(location.latitude, location.longitude))
            mMap?.animateCamera(CameraUpdateFactory.zoomTo(17f))//3~17,数字越大展示越精细
        }

        mMap?.setOnCameraChangeListener(object : AMap.OnCameraChangeListener {
            override fun onCameraChangeFinish(position: CameraPosition?) {
                mZoomLevel = position?.zoom ?: -1f
                updateInfo()
            }

            override fun onCameraChange(p0: CameraPosition?) {
            }

        })
        mMap?.setOnMapLoadedListener {
            updateInfo()
        }
        binding.fab.setOnClickListener {
//            loc()
            search()
        }
    }

    private fun search(){
        val search = DistrictSearch(this)
        val query = DistrictSearchQuery()
        query.keywords = "黄埔" //传入关键字

        query.isShowBoundary = false //是否返回边界值

        search.query = query
        search.setOnDistrictSearchListener{ r->
            Log.d(TAG, r.toString())
            r.district.forEachIndexed { index, districtItem ->
                Log.d(TAG, "$index > ${districtItem.toString()}")
                districtItem.subDistrict.forEachIndexed { subIndex, subDistrictItem ->
                    Log.d(TAG, "$subIndex > ${subDistrictItem.toString()}")
                }
            }
        } //绑定监听器

        search.searchDistrictAsyn() //开始搜索


    }
    private fun loc() {
        //声明AMapLocationClient类对象
        val mLocationClient = AMapLocationClient(applicationContext)
        //声明定位回调监听器
        val mLocationListener = AMapLocationListener { r ->
           // 【1代表完整描述，2代表精简描述，3代表极简描述】
            Log.d(TAG, r.toStr(2))
        }

        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener)


        //初始化AMapLocationClientOption对象
        val option = AMapLocationClientOption()
        /**
         * 设置定位场景，目前支持三种场景（签到、出行、运动，默认无场景）
         */
        option.locationPurpose = AMapLocationClientOption.AMapLocationPurpose.Transport
        option.locationMode = AMapLocationClientOption.AMapLocationMode.Battery_Saving
        option.isOnceLocation = true
        option.isNeedAddress=false
        mLocationClient.setLocationOption(option);
        //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
        mLocationClient.stopLocation()

        mLocationClient.startLocation()

    }


    @SuppressLint("SetTextI18n")
    private fun updateInfo() {
        binding.txtTitle.text = """
            cityCode > $mCityCode
            cityName > $mCityName
            zoomLevel > $mZoomLevel
        """.trimIndent()
    }


    override fun onResume() {
        super.onResume()
        checkLocPermission()
        binding.map.onResume()

        DataManager.with(this)
        Log.d("MapActivity", DataManager.keys().toString())
        whereIsMe()
    }

    override fun onPause() {
        super.onPause()
        binding.map.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.map.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.map.onSaveInstanceState(outState)
    }

    private fun searchBusLineByCityCode(code: String) {
        val busStationSearch: BusStationSearch = BusStationSearch(this, BusStationQuery("护林路", mCityCode))
        busStationSearch.setOnBusStationSearchListener { result, code ->
            Log.d(TAG, "setOnBusStationSearchListener code>$code result >${result}")
            Log.d(TAG, "bus result >${result.busStations}")
        }
        busStationSearch.searchBusStationAsyn()

    }

    private fun initGeoCoder(point: LatLonPoint) {
        val geocodeSearch = GeocodeSearch(this)
        geocodeSearch.setOnGeocodeSearchListener(object : GeocodeSearch.OnGeocodeSearchListener {
            override fun onRegeocodeSearched(result: RegeocodeResult?, code: Int) {
                Log.d(TAG, "onRegeocodeSearched code>$code result >${result?.regeocodeAddress}")
                mCityCode = result?.regeocodeAddress?.cityCode
                mCityName = result?.regeocodeAddress?.city
//                searchBusLineByCityCode("")
            }

            override fun onGeocodeSearched(result: GeocodeResult?, code: Int) {
                Log.d(TAG, "onGeocodeSearched code>$code result >$result")
            }

        })
        geocodeSearch.getFromLocationAsyn(RegeocodeQuery(point, 300f, GeocodeSearch.GPS))
    }

    private fun whereIsMe() {
        val myLocationStyle = MyLocationStyle()
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE)
        myLocationStyle.showMyLocation(true)
        mMap?.myLocationStyle = myLocationStyle
        mMap?.isMyLocationEnabled = true
        mMap?.uiSettings?.isMyLocationButtonEnabled = true


    }

    private fun checkLocPermission() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                "wtf"
            } else {
                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), ACCESS_FINE_LOCATION_REQUEST_CODE)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            ACCESS_FINE_LOCATION_REQUEST_CODE -> {
                if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                    whereIsMe()
                }
            }
        }
    }

    private val ACCESS_FINE_LOCATION_REQUEST_CODE = 1

}
