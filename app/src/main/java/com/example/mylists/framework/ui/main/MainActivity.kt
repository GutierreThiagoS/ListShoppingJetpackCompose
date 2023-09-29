package com.example.mylists.framework.ui.main

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.mylists.framework.composable.ToolbarAppBar
import com.example.mylists.framework.ui.theme.MyListsTheme
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val viewModel by inject<MainViewModel>()

    private fun readBarCode() {
        val scanOptions = ScanOptions()
        scanOptions.captureActivity = CaptureCodeBar::class.java
        scanOptions.setOrientationLocked(false)
        scanOptions.setBeepEnabled(true)
        scanOptions.setBarcodeImageEnabled(false)
        scanOptions.setCameraId(0)
        scanOptions.setDesiredBarcodeFormats(ScanOptions.ALL_CODE_TYPES)
        scanOptions.setPrompt("Código de Barra")
        barcodeLauncher.launch(scanOptions)
    }

    private val  barcodeLauncher = this.registerForActivityResult( ScanContract() ) { result: ScanIntentResult ->
        if (result.contents != null) {
            val barcode = result.contents.toString()
            Log.e("CodeBAr", "$barcode ")
            if (barcode.isNotBlank()) {
                viewModel.setBarCode(barcode)
            }
        } else Toast.makeText(this, "Cancelado", Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyListsTheme {
                val barCodeState by viewModel.barCodeState.collectAsState()
                ToolbarAppBar(
                    barCodeState
                ) {
                    viewModel.setBarCode(null)
                    if (it) readBarCode()
                }
            }
        }
    }
}