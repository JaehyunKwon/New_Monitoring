package com.fmk.monitoring.view

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.location.*
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.text.TextPaint
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.fmk.monitoring.R
import com.fmk.monitoring.common.CommonPopup
import com.fmk.monitoring.common.Permission
import com.fmk.monitoring.data.ResultGetSearchNews
import com.fmk.monitoring.databinding.ActivityMainBinding
import com.fmk.monitoring.network.API
import com.fmk.monitoring.util.DebugUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


open class MainActivity : AppCompatActivity() {

    private val TAG: String = MainActivity::class.java.simpleName

    private val REQUEST_MANDATORY_PERMISSION = 51
    private val RESULT_PERMISSION = 60

    private val REQUEST_IMAGE_CAPTURE = 1

    private lateinit var binding: ActivityMainBinding

    //========== [동적 팝업창 전역 변수] ==========
    lateinit var alertDialog: AlertDialog
    lateinit var builder: AlertDialog.Builder

    private var currentLatLng: Location? = null
    private var locatioNManager: LocationManager? = null

    lateinit var currentPhotoPath: String

    override fun onCreate(savedInstance: Bundle?) {
        super.onCreate(savedInstance)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // 메인 시작하고 바로 권한요청한다.
        requestPermission();
    }

    override fun onPause() {
        super.onPause()
        locatioNManager?.removeUpdates(locationListener)
    }

    private fun initView() {
        var title = ""
        var etTitle = ""

        binding.btnAddr.setOnClickListener {
            locatioNManager = (getSystemService(Context.LOCATION_SERVICE) as LocationManager?)!!
            getEnabled(locatioNManager!!)
        }

        binding.btnApi.setOnClickListener {
            val api = API.NaverAPI.create()

            api.getSearchNews("테스트").enqueue(object : Callback<ResultGetSearchNews> {
                override fun onResponse(
                    call: Call<ResultGetSearchNews>,
                    response: Response<ResultGetSearchNews>
                ) {
                    // 성공
                    DebugUtil.printWarning(TAG, "aaa ==>" + (response.body()?.items))
                }

                override fun onFailure(call: Call<ResultGetSearchNews>, t: Throwable) {
                    // 실패
                    DebugUtil.printError(TAG, "==== 실패!!! ====")
                }
            })

            /*api.transferPapago("한국어", "영어", "바보").enqueue(object : Callback<ResultTransferPapago> {
                override fun onResponse(
                    call: Call<ResultTransferPapago>,
                    response: Response<ResultTransferPapago>
                ) {
                    // 성공
                    DebugUtil.printWarning(TAG, "aaa ==>" + (response.body()))
                }

                override fun onFailure(call: Call<ResultTransferPapago>, t: Throwable) {
                    // 실패
                    DebugUtil.printError(TAG, "==== 실패!!! ====")
                }
            })*/
        }

        binding.btnPicture.setOnClickListener {
            //startCapture()
            val nextIntent = Intent(this, MonitoringActivity::class.java)
            startActivity(nextIntent)
        }
    }

    /**
     * GPS 활성 여부
     * */
    private fun getEnabled(locatioNManager: LocationManager) {
        val gpsProvider = LocationManager.GPS_PROVIDER
        val networkProvider = LocationManager.NETWORK_PROVIDER
        // gps OFF 일 때
        if (!locatioNManager.isProviderEnabled(gpsProvider)) {
            alertDialog = CommonPopup.showConfirmCancelDialog(
                this,
                false,
                "",
                getString(R.string.gps_alert_message),
                getString(R.string.gps_alert_cancel),
                getString(R.string.gps_alert_go_setting),
                { dialogInterface: DialogInterface, i: Int ->
                    alertDialog.dismiss()
                }) { dialogInterface: DialogInterface, i: Int ->
                // 위치정보 설정 Intent
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            } as AlertDialog
        } else {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                locatioNManager.requestLocationUpdates(gpsProvider, 60000, 1.0f, locationListener)
                locatioNManager.requestLocationUpdates(
                    networkProvider,
                    60000,
                    1.0f,
                    locationListener
                )
            }
        }
    }

    /**
     * 현재 위치 정보
     * */
    private fun getLocation(location: Location) {
        var userLocation: Location? = location
        if (userLocation != null) {
            var latitude = userLocation.latitude
            var longitude = userLocation.longitude
            Log.d("CheckCurrentLocation", "현재 내 위치 값: $latitude, $longitude")

            var mGeoCoder = Geocoder(applicationContext, Locale.KOREAN)
            var mResultList: List<Address>? = null
            try {
                mResultList = mGeoCoder.getFromLocation(
                    latitude!!, longitude!!, 1
                )
            } catch (e: IOException) {
                e.printStackTrace()
            }
            if (mResultList != null) {
                DebugUtil.printError(
                    TAG,
                    "CheckCurrentLocation : ${mResultList[0].getAddressLine(0)}"
                )
                DebugUtil.printError(TAG, "CheckCurrentLocation : ${mResultList[0]}")
                binding.etTitle.text = mResultList[0].getAddressLine(0)
            }
        } else {
            binding.etTitle.text = ""
        }
    }

    private fun getLatLng(locatioNManager: LocationManager): Location? {
        val gpsProvider = LocationManager.GPS_PROVIDER
        val networkProvider = LocationManager.NETWORK_PROVIDER
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            currentLatLng = locatioNManager.getLastKnownLocation(gpsProvider)
        }

        if (currentLatLng == null) {
            currentLatLng = locatioNManager.getLastKnownLocation(networkProvider)
        }

        return currentLatLng
    }

    /**
     * LocationListener
     * */
    var locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            DebugUtil.printDebug(
                TAG, "GPS Location changed, Latitude: $location.latitude" +
                        ", Longitude: $location.longitude"
            )
            getLocation(location)
        }

        override fun onProviderDisabled(provider: String) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
    }

    fun startCapture() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    null
                }
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "com.fmk.monitoring.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val file = File(currentPhotoPath)
            if (Build.VERSION.SDK_INT < 28) {
                val bitmap = MediaStore.Images.Media
                    .getBitmap(contentResolver, Uri.fromFile(file))

                val canvas = Canvas(bitmap)
                val textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG or Paint.LINEAR_TEXT_FLAG)
                textPaint.style = Paint.Style.FILL
                textPaint.color = Color.WHITE
                textPaint.textSize = 30f
                canvas.drawText(binding.etTitle.text as String, 20f, 50f, textPaint)
                binding.imgPicture.setImageBitmap(bitmap)
            } else {
                val decode = ImageDecoder.createSource(
                    this.contentResolver,
                    Uri.fromFile(file)
                )
                val bitmap = ImageDecoder.decodeBitmap(decode).copy(Bitmap.Config.ARGB_8888, true)

                val canvas = Canvas(bitmap)
                val textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG or Paint.LINEAR_TEXT_FLAG)
                textPaint.style = Paint.Style.FILL
                textPaint.color = Color.WHITE
                textPaint.textSize = 130f
                textPaint.typeface = Typeface.DEFAULT_BOLD
                canvas.drawText(binding.etTitle.text as String, 50f, 200f, textPaint)
                binding.imgPicture.setImageBitmap(bitmap)
            }
        }
    }

    /**
     * 단말 permission 요청
     */
    private fun requestPermission() {
        var count = 0
        for (permission in Permission().PERMISSION_MANDATORY) {
            val accessCoarseLocation = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

            val accessFineLocation = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            DebugUtil.printDebug(TAG, "accessCoarseLocation ==> $accessCoarseLocation")
            DebugUtil.printWarning(TAG, "accessFineLocation ==> $accessFineLocation")

            // ACCESS_COARSE_LOCATION 권한이 true 이면, ACCESS_BACKGROUND_LOCATION 권한 PASS.
            if (ContextCompat.checkSelfPermission(
                    this@MainActivity,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (accessCoarseLocation) {
                    continue
                } else {
                    count++
                }
            }
        }
        if (count > 0) {
            var index = 0
            val strPermission = arrayOfNulls<String>(count)
            for (permission in Permission().PERMISSION_MANDATORY) {
                if (ContextCompat.checkSelfPermission(
                        this@MainActivity,
                        permission
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    strPermission[index] = permission
                    index++
                }
            }
            ActivityCompat.requestPermissions(
                this,
                strPermission,
                REQUEST_MANDATORY_PERMISSION
            )
        } else {
            initView()
        }
    }

    /**
     * 단말 permission 요청에 따른 응답 수신
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_MANDATORY_PERMISSION -> {
                if (Permission().hasMandatoryPermission(this)) {
                    initView()
                } else {
                    alertDialog = CommonPopup.showConfirmCancelDialog(
                        this,
                        false,
                        getString(R.string.alert_title),
                        getString(R.string.alert_message),
                        getString(R.string.alert_go_permission),
                        getString(R.string.alert_finish),
                        { dialogInterface: DialogInterface, i: Int ->
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            val uri = Uri.fromParts("package", packageName, null)
                            intent.data = uri
                            startActivityForResult(intent, RESULT_PERMISSION)
                        }) { dialogInterface: DialogInterface, i: Int ->
                        finish()
                    } as AlertDialog
                }
                return
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }
}