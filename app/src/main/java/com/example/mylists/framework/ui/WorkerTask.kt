package com.example.mylists.framework.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.mylists.R
import com.example.mylists.framework.utils.logE
import kotlinx.coroutines.delay

class WorkerTask(context: Context, params: WorkerParameters): CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {

        // Simular uma tarefa em segundo plano
        delay(3000)

        // Executar a lógica da tarefa em segundo plano

        logE("Testando doWork")
        showNotification()
        return Result.success()
    }

    private fun showNotification() {
        // Lógica para mostrar uma notificação
        val notificationBuilder = NotificationCompat.Builder(applicationContext, "channel_id")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Tarefa Concluída")
            .setContentText("A tarefa em segundo plano foi concluída.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(applicationContext)) {
            if (ActivityCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                notify(1, notificationBuilder.build())
            }
        }
    }


}