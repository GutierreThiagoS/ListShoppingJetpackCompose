package com.gutierre.mylists.framework.ui.main

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.content.ContextCompat
import androidx.work.Constraints
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.gutierre.mylists.R
import com.gutierre.mylists.domain.model.ToDoItem
import com.gutierre.mylists.framework.composable.ToolbarAppBar
import com.gutierre.mylists.services.WorkerTask
import com.gutierre.mylists.framework.ui.theme.MyListsTheme
import com.gutierre.mylists.framework.utils.isValidDateHour
import com.gutierre.mylists.framework.utils.isValidDateToday
import com.gutierre.mylists.framework.utils.logE
import com.gutierre.mylists.framework.utils.valueValidDateHour
import com.gutierre.mylists.services.FireBaseAnalytic
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import org.koin.android.ext.android.inject
import java.util.concurrent.TimeUnit

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

        ActivityState.setActivity(this)

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("MainActivity", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result
            // Log and toast
            val msg = getString(R.string.msg_token_fmt, token)
            logE("MainActivity FirebaseMessaging $msg")
        })

        val extras = intent?.extras

        if (
            extras != null &&
            extras.containsKey("id")
        ) {
            val id = extras.getInt("id")
            viewModel.read(id)
        }

        setContent {
            MyListsTheme {

                val toDoNotifications by viewModel.toDoNotifications.collectAsState(initial = listOf())

                val barCodeActive by viewModel.barCodeActive.collectAsState()

                toDoNotifications.forEach { item ->
                    getAlertManager(item)
                }

                if (barCodeActive != null && (barCodeActive ?: 0) > 0) {
                    viewModel.setBarCode(null)
                    readBarCode()
                    viewModel.removeBarCode()
                }

                ToolbarAppBar(
                    viewModel = viewModel
                )
            }
        }

    }

    private fun getAlertManager(item: ToDoItem) {
        if (isValidDateToday(item.dateFinal)) {
            if (isValidDateHour(item.dateFinal + item.hourAlert)) {
                requiredNotification(item)
            } else {
                requiredNotification(item)
                FireBaseAnalytic.sendNotificationEvent(item)
            }
        }
    }

    private fun requiredNotification(item: ToDoItem) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                worker(item)
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                AlertDialog.Builder(this).apply {
                    setTitle("Atenção")
                    setMessage("Você precisa da Permissão de Notificação!")
                    setNeutralButton("OK") { d, _ ->
                        d.dismiss()
                    }
                    show()
                }
//                Toast.makeText(this, "Solicite a permissão de Notificação", Toast.LENGTH_SHORT).show()
            } else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        } else worker(item)
    }

    private fun worker(item: ToDoItem) {
        val params = workDataOf(
            "id" to item.id,
            "level" to item.level
        )

        // Criar restrições para a tarefa
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(androidx.work.NetworkType.CONNECTED)
            .build()

        val valueDateHour = valueValidDateHour(item.dateFinal + item.hourAlert)

        // Criar uma solicitação única
        val workRequest = OneTimeWorkRequestBuilder<WorkerTask>()
            .setConstraints(constraints)
            .setInputData(params)
            .setInitialDelay(if (valueDateHour < 1) 1 else valueDateHour , TimeUnit.MINUTES) // Atraso inicial, se necessário
            .build()

        // Agendar a tarefa
        WorkManager.getInstance(this).enqueue(workRequest)

        // Observar o status da tarefa, se necessário
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(workRequest.id)
            .observe(this) { workInfo ->
                logE("MainActivity $workInfo")
                // Lidar com as mudanças no status da tarefa, se necessário
            }
    }

    override fun onResume() {
        super.onResume()
        ActivityState.activityResumed()
        ActivityState.setActivity(this)
    }

    override fun onPause() {
        super.onPause()
        ActivityState.activityPaused()
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityState.setActivity(this)
    }

    // Declare the launcher at the top of your Activity/Fragment:
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            viewModel.refreshNotification()
        } else {
            Toast.makeText(this, "Precisa da permissão de Notificação", Toast.LENGTH_SHORT).show()
        }
    }

}