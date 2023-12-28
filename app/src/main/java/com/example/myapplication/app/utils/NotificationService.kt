import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.app.constants.NOTIFICATION_CHANNEL_ID
import com.example.myapplication.app.constants.NOTIFICATION_ID
import com.example.myapplication.app.constants.REQUEST_CODE
class NotificationServices(

private  val context: Context
){

    private val notificationManager:NotificationManager=
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


    private  val myIntent=Intent(context,MainActivity::class.java)
    private  val pendingIntent=PendingIntent.getActivities(
     context,
     REQUEST_CODE,
        arrayOf(myIntent),
        PendingIntent.FLAG_IMMUTABLE

    )

    fun showNotification() {
        val notification: Notification =
            NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("STOP WATCH")
                .setContentText("YOUR TIME IS FINISHED")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .build()
        notificationManager.notify(NOTIFICATION_ID, notification)
    }


}