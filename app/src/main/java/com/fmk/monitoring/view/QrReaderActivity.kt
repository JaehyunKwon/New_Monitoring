package com.fmk.monitoring.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.fmk.monitoring.R
import com.fmk.monitoring.databinding.ActivityQrReaderBinding
import com.fmk.monitoring.databinding.ActivityQrSearchBinding
import com.google.zxing.client.android.Intents
import com.journeyapps.barcodescanner.CaptureManager
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.journeyapps.barcodescanner.DecoratedBarcodeView.TorchListener

class QrReaderActivity : AppCompatActivity(), TorchListener {
    private var m_captureManager: CaptureManager? = null
    lateinit var qrReaderBinding: ActivityQrReaderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        qrReaderBinding = ActivityQrReaderBinding.inflate(layoutInflater)
        val view = qrReaderBinding.root
        setContentView(view)

        // 헤더 'x' 버튼
        qrReaderBinding.btnClose.visibility = View.VISIBLE
        qrReaderBinding.btnClose.setOnClickListener {
            cancel()
            finish()
        }
        // QR 스킵 버튼
        qrReaderBinding.btnSkipQr.setOnClickListener {
            val intent = Intent()
            intent.putExtra(Intents.Scan.RESULT, "http://fmkr.kr/event/?cd=T00001")
            setResult(RESULT_OK, intent)
            finish()
        }

        // QR 스캔 view
        val decoratedBarcodeView = qrReaderBinding.qrReaderView
        decoratedBarcodeView.setTorchListener(this)
        m_captureManager = CaptureManager(this, decoratedBarcodeView)
        m_captureManager!!.initializeFromIntent(intent, savedInstanceState)
        m_captureManager!!.decode()
    }

    override fun onTorchOn() {}
    override fun onTorchOff() {}
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        m_captureManager!!.onSaveInstanceState(outState)
    }

    override fun onPause() {
        super.onPause()
        m_captureManager!!.onPause()
    }

    override fun onResume() {
        super.onResume()
        m_captureManager!!.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        m_captureManager!!.onDestroy()
    }

    override fun onBackPressed() {
        cancel()
        super.onBackPressed()
    }

    private fun cancel() {
        val intent = Intent()
        intent.putExtra(Intents.Scan.RESULT, "cancel")
        setResult(RESULT_OK, intent)
    }
}