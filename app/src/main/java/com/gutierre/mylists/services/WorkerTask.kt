package com.gutierre.mylists.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.gutierre.mylists.R
import com.gutierre.mylists.framework.presentation.to_do.ToDoViewModel
import com.gutierre.mylists.framework.ui.main.ActivityState
import com.gutierre.mylists.framework.ui.main.MainActivity
import com.gutierre.mylists.framework.utils.logE
import com.gutierre.mylists.framework.utils.valueValidDateHour
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent
import java.util.concurrent.TimeUnit

class WorkerTask(private val context: Context, params: WorkerParameters): CoroutineWorker(context, params) {

    private val viewModel: ToDoViewModel by KoinJavaComponent.inject(ToDoViewModel::class.java)

    private var count = 0

    override suspend fun doWork(): Result {
        logE("Background Testando doWork")

        // Simular uma tarefa em segundo plano
        delay(3000)

        // Executar a lógica da tarefa em segundo plano
        val id = inputData.getInt("id", 0)
        val level = inputData.getInt("level", 0)

        logE("Background doWork2 id $id, level $level")
        createNotification(id, level)
        return Result.success()
    }

    private fun createNotification(id: Int?, level: Int?) {

        viewModel.getToDoItemId(id, level) {
            logE("Background createNotification getToDoItemId $it")

            if (it?.concluded == false && !it.deleted) {

                val channelId = "default_channel_id ${id ?: 123}"
                val notificationId = id ?: 123

                val notificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

                // Verificar se o canal de notificação já foi criado
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val channel = NotificationChannel(
                        channelId,
                        "Default Channel",
                        NotificationManager.IMPORTANCE_HIGH
                    )
                    notificationManager.createNotificationChannel(channel)
                }

                // Criar um intent para abrir o aplicativo quando a notificação for clicada
                val contentIntent = Intent(context, MainActivity::class.java)

                val bundle = Bundle()
                bundle.putInt("id", it.id)
                contentIntent.putExtras(bundle)

                val pendingIntent = PendingIntent.getActivity(
                    context,
                    count,
                    contentIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )

                count ++

                // Construir a notificação
                val notification = NotificationCompat.Builder(context, channelId)
                    .setContentTitle(it.title)
                    .setContentText(it.description)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setVibrate(longArrayOf(1000, 1000, 1000))
                    .setBadgeIconType(R.drawable.baseline_message)
                    .setSmallIcon(R.drawable.baseline_notifications_active)
                    .setColor(ContextCompat.getColor(context, R.color.teal_200))
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
//                    .addAction(R.drawable.baseline_message, "Concluir", pendingIntent)
                    .build()

                // Exibir a notificação
                notificationManager.notify(notificationId, notification)
                val isActivityResumed = ActivityState.isResumed.value

                logE("Background isActivityResumed $isActivityResumed")

                if (id != null) viewModel.extendToDoItemId(id, level) { newToDoItem ->
                    CoroutineScope(Dispatchers.Main).launch {
                        if (isActivityResumed != true) {

                            logE("Background worker1212212 ActivityState ${ActivityState.getActivity()}")

                            val params = workDataOf(
                                "id" to newToDoItem.id,
                                "level" to newToDoItem.level
                            )

                            // Criar restrições para a tarefa
                            val constraints = Constraints.Builder()
                                .setRequiredNetworkType(androidx.work.NetworkType.CONNECTED)
                                .build()

                            val valueDateHour = valueValidDateHour(newToDoItem.dateFinal + newToDoItem.hourAlert)

                            // Criar uma solicitação única
                            val workRequest = OneTimeWorkRequestBuilder<WorkerTask>()
                                .setConstraints(constraints)
                                .setInputData(params)
                                .setInitialDelay(if (valueDateHour < 1) 1 else valueDateHour, TimeUnit.MINUTES) // Atraso inicial, se necessário
                                .build()

                            // Agendar a tarefa
                            WorkManager.getInstance(context).enqueue(workRequest)
                            WorkManager.getInstance(context).getWorkInfoByIdLiveData(workRequest.id).let {
                                ActivityState.getActivity()?.let { fragmentActivity ->
                                    it.observe(fragmentActivity) { workInfo ->
                                        logE("teste12121212 $workInfo")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}