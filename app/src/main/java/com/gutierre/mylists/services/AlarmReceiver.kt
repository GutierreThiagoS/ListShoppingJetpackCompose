package com.gutierre.mylists.services

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.gutierre.mylists.R
import com.gutierre.mylists.framework.presentation.to_do.ToDoViewModel
import com.gutierre.mylists.framework.ui.main.MainActivity
import com.gutierre.mylists.framework.utils.logE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent
import java.text.SimpleDateFormat
import java.util.Locale

class AlarmReceiver: BroadcastReceiver() {

    private val viewModel: ToDoViewModel by KoinJavaComponent.inject(ToDoViewModel::class.java)

    private var count = 0
    override fun onReceive(context: Context?, intent: Intent?) {

        logE("AlarmReceiver ${intent?.extras.toString()}")

        // Obter os dados extras do intent
        val bundle: Bundle? = intent?.extras

        // Exemplo: obter uma string do bundle
        val id = bundle?.getInt("id")
        val title = bundle?.getString("title")
        val message = bundle?.getString("mensagem")

        createNotification(context = context, id = id, title = title, message = message)
    }

    private fun createNotification(context: Context?, id: Int?, title: String?, message: String?) {

        viewModel.getToDoItemId(id, null) {

            if (it?.concluded != true && it?.deleted != true) {

                Toast.makeText(context, "Alarme ativado!", Toast.LENGTH_SHORT).show()
                
                val channelId = "default_channel_id ${id ?: 123}"
                val notificationId = id ?: 123

                val notificationManager =
                    context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

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
                bundle.putInt("id", it?.id ?: 0)
                bundle.putString("title", it?.title ?: title)
                bundle.putString("message", it?.description ?: message)
                contentIntent.putExtras(bundle)

                val pendingIntent = PendingIntent.getActivity(
                    context,
                    count,
                    contentIntent,
                    PendingIntent.FLAG_IMMUTABLE
                )

                count ++

                // Construir a notificação
                val notification = NotificationCompat.Builder(context, channelId)
                    .setContentTitle(title ?: "Alarme ativado")
                    .setContentText(message ?: "Clique para abrir o aplicativo.")
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setVibrate(longArrayOf(1000, 1000, 1000))
                    .setBadgeIconType(R.drawable.baseline_notifications_active)
                    .setSmallIcon(R.drawable.baseline_notifications_active)
                    .setColor(ContextCompat.getColor(context, R.color.teal_200))
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .build()

                // Exibir a notificação
                notificationManager.notify(notificationId, notification)

                if (id != null) viewModel.extendToDoItemId(id, null) { newToDoItem ->
                    CoroutineScope(Dispatchers.Main).launch {
                        val alarmManager =
                            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

                        val dateHourFormat =
                            SimpleDateFormat("dd/MM/yyyyHH:mm", Locale.getDefault())

                        // Crie um intent para o BroadcastReceiver
                        val intent = Intent(context, AlarmReceiver::class.java)

                        val bundleExtend = Bundle()
                        bundleExtend.putInt("id", newToDoItem.id)
                        bundleExtend.putString("title", newToDoItem.title)
                        bundleExtend.putString("mensagem", newToDoItem.description)
                        intent.putExtras(bundleExtend)
                        val pendingIntentExtend = PendingIntent.getBroadcast(
                            context,
                            newToDoItem.id,
                            intent,
                            PendingIntent.FLAG_MUTABLE
                        )

                        alarmManager.set(
                            AlarmManager.RTC_WAKEUP,
                            dateHourFormat.parse(newToDoItem.dateFinal + newToDoItem.hourAlert)?.time
                                ?: SystemClock.elapsedRealtime(),
                            pendingIntentExtend
                        )
                    }
                }
            }
        }
    }
}