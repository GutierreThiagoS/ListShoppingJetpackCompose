package com.example.mylists.framework.ui.main

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.app.ActivityCompat
import androidx.work.Constraints
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.mylists.domain.model.ToDoItem
import com.example.mylists.framework.composable.ToolbarAppBar
import com.example.mylists.framework.ui.AlarmReceiver
import com.example.mylists.framework.ui.WorkerTask
import com.example.mylists.framework.ui.theme.MyListsTheme
import com.example.mylists.framework.utils.isValidDateHour
import com.example.mylists.framework.utils.isValidDateToday
import com.example.mylists.framework.utils.logE
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private val viewModel by inject<MainViewModel>()

    companion object {
        const val REQUEST_CODE_NOTIFICATION_PERMISSION = 12
    }

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

    private val barcodeLauncher = this.registerForActivityResult( ScanContract() ) { result: ScanIntentResult ->
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

        // Verificar se a Activity foi iniciada a partir de uma notificação
        val extras = intent?.extras
        if (
            extras != null &&
            extras.containsKey("id") &&
            extras.containsKey("title") &&
            extras.containsKey("message")
        ) {
            val id = extras.getInt("id")
            val title = extras.getString("title")
            val message = extras.getString("message")

            logE("id $id, title $title, message $message")
            viewModel.read(id)
        }

        setContent {
            MyListsTheme {

                val toDoNotifications by viewModel.toDoNotifications.collectAsState(initial = listOf())

                val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager

                toDoNotifications.forEach { item ->
                    getAlertManager(item, alarmManager)
                }

                val barCodeState by viewModel.barCodeState.collectAsState()

                ToolbarAppBar(
                    barCodeState
                ) {
                    viewModel.setBarCode(null)
                    if (it) readBarCode()
                }
            }
        }

        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            worker()
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    REQUEST_CODE_NOTIFICATION_PERMISSION
                )
            } else worker()
        }
    }

    private fun getAlertManager(item: ToDoItem, alarmManager: AlarmManager) {
        val dateHourFormat = SimpleDateFormat("dd/MM/yyyyHH:mm", Locale.getDefault())
        // Crie um intent para o BroadcastReceiver
        val intent = Intent(this, AlarmReceiver::class.java)

        val bundle = Bundle()
        bundle.putInt("id", item.id)
        bundle.putString("title", item.title)
        bundle.putString("mensagem", item.description)
        intent.putExtras(bundle)
        val pendingIntent = PendingIntent.getBroadcast(this, item.id, intent, PendingIntent.FLAG_MUTABLE)

        if (isValidDateToday(item.dateFinal)) {
            if (isValidDateHour(item.dateFinal + item.hourAlert)) {
                logE("id isValidDateHour")
                alarmManager.set(AlarmManager.RTC_WAKEUP,
                    dateHourFormat.parse(item.dateFinal + item.hourAlert)?.time
                        ?: SystemClock.elapsedRealtime(),
                    pendingIntent
                )
            } else {
                logE("id isValidDate")
                alarmManager.set(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime() + 3000,
                    pendingIntent
                )
            }
        }
    }

    private fun worker() {
        // Criar restrições para a tarefa
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(androidx.work.NetworkType.UNMETERED) // Exemplo: apenas em Wi-Fi
            .setRequiredNetworkType(androidx.work.NetworkType.CONNECTED)
            .setRequiresCharging(true) // Exemplo: apenas quando carregando
            .build()

        // Criar uma solicitação única
        val workRequest = OneTimeWorkRequestBuilder<WorkerTask>()
            .setConstraints(constraints)
            .setInitialDelay(10, TimeUnit.MINUTES) // Atraso inicial, se necessário
            .build()

        // Agendar a tarefa
        WorkManager.getInstance(this).enqueue(workRequest)

        // Observar o status da tarefa, se necessário
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(workRequest.id)
            .observe(this) { workInfo ->
                // Lidar com as mudanças no status da tarefa, se necessário
            }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE_NOTIFICATION_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    worker()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}