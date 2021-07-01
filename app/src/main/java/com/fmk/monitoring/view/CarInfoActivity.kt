package com.fmk.monitoring.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.fmk.monitoring.common.CommonUtil
import com.fmk.monitoring.databinding.ActivityCarInfoBinding


class CarInfoActivity : AppCompatActivity() {
    lateinit var carInfoBinding: ActivityCarInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        carInfoBinding = ActivityCarInfoBinding.inflate(layoutInflater)
        val view = carInfoBinding.root
        setContentView(view)

        // 헤더 'x' 버튼
        carInfoBinding.headerCar.btnClose.visibility = View.VISIBLE
        carInfoBinding.headerCar.btnClose.setOnClickListener {
            finish()
        }
        carInfoBinding.headerCar.tvHeaderTitle.text = "차량등록"

        initView()
   }

    private fun initView() {
        carInfoBinding.btnFirstSubmit.setOnClickListener {
            carInfoBinding.viewLine.visibility = View.VISIBLE
            carInfoBinding.endLayout.visibility = View.VISIBLE
            // 키보드 숨기기
            CommonUtil.hideKeyboard(this, carInfoBinding.editCar)
        }

        carInfoBinding.btnEndSubmit.setOnClickListener {
            // 키보드 숨기기
            CommonUtil.hideKeyboard(this, carInfoBinding.editEndKm)
        }
    }
}