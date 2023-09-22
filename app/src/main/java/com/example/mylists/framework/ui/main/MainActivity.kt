package com.example.mylists.framework.ui.main

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.mylists.framework.composable.ToolbarAppBar
import com.example.mylists.framework.ui.theme.MyListsTheme
import com.journeyapps.barcodescanner.CaptureActivity
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val viewModel by inject<MainViewModel>()

    private fun readBarCode() {
        val scanOptions = ScanOptions()
        scanOptions.captureActivity = CaptureActivity::class.java
        scanOptions.setOrientationLocked(true)
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
                ToolbarAppBar {
                    readBarCode()
                }
            }
        }
    }
}