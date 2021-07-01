package com.fmk.monitoring.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.webkit.WebSettings
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fmk.monitoring.adapter.AddressAdapter
import com.fmk.monitoring.common.CommonUtil
import com.fmk.monitoring.data.Address
import com.fmk.monitoring.databinding.ActivityQrSearchBinding
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import java.text.SimpleDateFormat
import java.util.*

class QRSearchActivity : AppCompatActivity() {
    private val TAG : String = MonitoringActivity::class.java.simpleName
    lateinit var qrSearchBinding: ActivityQrSearchBinding

    private val addrList: List<Address> = listOf(
        Address("금오주공 9단지", "경기도 의정부시"),
        Address("송산주공 1단지", "경기도 의정부시"),
        Address("현대 아이파크 2단지", "경기도 의정부시"),
        Address("현대 아이파크 3단지", "경기도 의정부시"),
        Address("현대 아이파크 4단지", "경기도 의정부시"),
        Address("용마 어울림 2단지", "경기도 의정부시"),
        Address("더샵파크에비뉴 4단지", "경기도 의정부시"),
        Address("롯데 캐슬 6단지", "경기도 의정부시"),
        Address("금오주공 2단지", "경기도 의정부시"),
        Address("드림밸리 2단지", "경기도 의정부시"),
        Address("드림밸리 3단지", "경기도 의정부시"),
        Address("용마 금호타운 4단지", "경기도 의정부시"),
        Address("금오주공 3단지", "경기도 의정부시"),
        Address("달빛마을 3단지", "경기도 의정부시"),
        Address("달빛마을 5단지", "경기도 의정부시"),
        Address("금빛마을 2단지", "경기도 의정부시")
    )

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        qrSearchBinding = ActivityQrSearchBinding.inflate(layoutInflater)
        val view = qrSearchBinding.root
        setContentView(view)

        // 공통 헤더 영역 '날짜, 이름'
        val currentTime = Calendar.getInstance().time
        val monthFormat = SimpleDateFormat("MM", Locale.getDefault()).format(currentTime)
        val dayFormat = SimpleDateFormat("dd", Locale.getDefault()).format(currentTime)
        qrSearchBinding.header.tvDay.text = "$monthFormat.$dayFormat"
        qrSearchBinding.header.tvName.text = "권재현님"

        initView()
    }

    private fun initView() {
        qrSearchBinding.wv.settings.apply {
            this.setSupportMultipleWindows(false) // 새창 띄우기 허용
            this.setSupportZoom(false) // 화면 확대 허용
            this.javaScriptEnabled = true
            //자바스크립트 허용
            this.javaScriptCanOpenWindowsAutomatically = false // 자바스크립트 새창 띄우기 허용
            this.loadWithOverviewMode = true // html의 컨텐츠가 웹뷰보다 클 경우 스크린 크기에 맞게 조정
            this.cacheMode = WebSettings.LOAD_NO_CACHE // 브라우저 캐쉬 허용
            this.domStorageEnabled = true // 로컬 저장 허용
            this.databaseEnabled = true
            /**
             * This request has been blocked; the content must be served over HTTPS
             * https 에서 이미지가 표시 안되는 오류를 해결하기 위한 처리
             * */
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            }
        }
        qrSearchBinding.wv.webViewClient = WebViewClient()

        qrSearchBinding.btnQr.setOnClickListener {
            val intentIntegrator = IntentIntegrator(this)
            intentIntegrator.setBeepEnabled(true)
            intentIntegrator.captureActivity = QrReaderActivity::class.java
            intentIntegrator.initiateScan()
        }

        qrSearchBinding.btnSearch.setOnClickListener{
            val adapter = AddressAdapter(this, addrList)
            qrSearchBinding.lvAddr.adapter = adapter
            CommonUtil.hideKeyboard(this, qrSearchBinding.editAddr)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val result: IntentResult =
            IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            // QR 코드 인식 후 결과
            IntentIntegrator.REQUEST_CODE ->
                if (resultCode == Activity.RESULT_OK) {
                    Log.w(TAG, "result :: " + result.contents)

                    if (result.contents.equals("cancel")) {
                        Toast.makeText(this, "Scanned Cancel!!", Toast.LENGTH_LONG).show()
                        return
                    }

                    if (qrSearchBinding.wv.visibility == View.GONE) {
                        qrSearchBinding.wv.visibility = View.VISIBLE
                    }
                    qrSearchBinding.wv.loadUrl(result.contents);
                } else {
                    Toast.makeText(this, "Scanned Fail!", Toast.LENGTH_LONG).show()
                }
        }
    }
}