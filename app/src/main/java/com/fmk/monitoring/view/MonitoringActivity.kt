package com.fmk.monitoring.view

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fmk.monitoring.databinding.ActivityMonitorBinding
import java.text.SimpleDateFormat
import java.util.*


class MonitoringActivity : AppCompatActivity() {
    private val TAG : String = MonitoringActivity::class.java.simpleName
    private lateinit var mainBinding : ActivityMonitorBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMonitorBinding.inflate(layoutInflater)
        val view = mainBinding.root
        setContentView(view)

        // 공통 헤더 영역 '날짜, 이름'
        val currentTime = Calendar.getInstance().time
        val monthFormat = SimpleDateFormat("MM", Locale.getDefault()).format(currentTime)
        val dayFormat = SimpleDateFormat("dd", Locale.getDefault()).format(currentTime)
        mainBinding.header.tvDay.text = "$monthFormat.$dayFormat"
        mainBinding.header.tvName.text = "권재현님"

        // 단지 검색 페이지로 이동
        mainBinding.btnQr.setOnClickListener {
            val qrIntent = Intent(this, QRSearchActivity::class.java)
            startActivity(qrIntent)
        }
        // 차량등록 페이지로 이동
        mainBinding.btnCar.setOnClickListener {
            val carIntent = Intent(this, CarInfoActivity::class.java)
            startActivity(carIntent)
        }
    }

    override fun onBackPressed() {
        //super.onBackPressed()
        val builder = AlertDialog.Builder(this)
        builder.setTitle("알림")
        builder.setMessage("앱을 종료하시겠습니까?")
        builder.setNegativeButton("취소", null)
        builder.setPositiveButton(
            "종료"
        ) { _, _ -> finish() }
        builder.show()
    }
}