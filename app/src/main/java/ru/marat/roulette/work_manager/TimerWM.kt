package ru.marat.roulette.work_manager

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import kotlinx.coroutines.delay
import ru.marat.roulette.R

const val TIME_IN_SECONDS = 60

class TimerWM(private val context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val timerPrefs = TimerPrefs(context)
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(NotificationChannel("testttttt", "testikkkkk", NotificationManager.IMPORTANCE_HIGH))

        val notificationBuilder = NotificationCompat.Builder(context, Notification())
            .setContentTitle("timer")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setChannelId("testttttt")
        val savedTime = timerPrefs.getTime()
        var timeLeft = TIME_IN_SECONDS.toLong()
        val finishTime = if (savedTime == 0L) System.currentTimeMillis() + (TIME_IN_SECONDS * 1000) else savedTime
        timerPrefs.saveTimeMillis(finishTime)
        delay(1000)
        while(timeLeft > 0) {
            val currentTime = System.currentTimeMillis()
            timeLeft = (finishTime - currentTime)/1000
            notificationBuilder.setContentText("$timeLeft")
            setForeground(ForegroundInfo(0,notificationBuilder.build()))
            delay(1000)
        }
        timerPrefs.saveTimeMillis(0)
        notificationBuilder.setContentText("success")
        manager.notify(1, notificationBuilder.build())
        return Result.success()
    }
}

private const val TIME_KEY = "kkkkkkkkk"

class TimerPrefs(context: Context) {

    val pref = context.getSharedPreferences("timerrrrrr", Context.MODE_PRIVATE)

    fun saveTimeMillis(timeMillis: Long) = pref.edit().putLong(TIME_KEY, timeMillis).apply()

    fun getTime(): Long = pref.getLong(TIME_KEY, 0)
}