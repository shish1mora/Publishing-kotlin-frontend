package ru.kafpin.lr3

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Person
import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.util.Date


class MainActivity : AppCompatActivity() {
    companion object {
        const val NOTIFICATION_ID = 101
        const val CHANNEL_ID = "channelID"
    }

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel.
            val name = CHANNEL_ID
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
            // Register the channel with the system. You can't change the importance
            // or other notification behaviors after this.
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }

//        val mNotificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE)
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        val button: Button = findViewById(R.id.button)
        button.setOnClickListener {
//            val mBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
//                .setSmallIcon(R.drawable.hungrycat24)
//                .setContentTitle("Напоминание")
//                .setContentText("Пора покормить кота")
//                .setAutoCancel(true)
//                .setChannelId(CHANNEL_ID)
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//            val notificationManager = NotificationManagerCompat.from(this)
//            notificationManager.notify(NOTIFICATION_ID, mBuilder.build())
//            val notificationIntent = Intent(
//                this@MainActivity,
//                SecondActivity::class.java
//            )
//            val pendingIntent = PendingIntent.getActivity(
//                this@MainActivity,
//                0, notificationIntent,
//                PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
//            )
//
//            val bigText = ("Это я, почтальон Печкин. Принёс для вас посылку. "
//                    + "Только я вам её не отдам. Потому что у вас документов нету. ")
//
//            val builder: NotificationCompat.Builder =
//                NotificationCompat.Builder(this@MainActivity, CHANNEL_ID)
//                    .setSmallIcon(R.drawable.hungrycat24)
//                    .setContentTitle("Посылка")
//                    .setContentText("Это я, почтальон Печкин. Принес для вас посылку")
//                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                    .setContentIntent(pendingIntent)
//                    .setLargeIcon(
//                        BitmapFactory.decodeResource(
//                            resources,
//                            R.drawable.hungrycat
//                        )
//                    ) // большая картинка
//                    .addAction(
//                        R.drawable.hungrycat24, "Запустить активность",
//                        pendingIntent
//                    )
//

//                    .setStyle(
//                        NotificationCompat.BigPictureStyle()
//                            .bigPicture(BitmapFactory.decodeResource(resources,R.drawable.hungrycat))
//                            .bigLargeIcon(BitmapFactory.decodeResource(resources,R.drawable.ic_pets_black_24dp))
//                            .setBigContentTitle("Beautiful Cat")
//                            .setSummaryText("Голодный кот")
//                    )
//                    .setStyle(NotificationCompat.BigTextStyle().bigText(bigText))
//
//                    .setChannelId(id)
//                    .setStyle(
//                        NotificationCompat.InboxStyle()
//                            .addLine("This is first line")
//                            .addLine("This is second line")
//                            .addLine("This is third line")
//                            .addLine("This is fourth line")
//                            .addLine("This is fifth line")
//                            .setBigContentTitle("This is Content Title.")
//                            .setSummaryText("This is summary text.")
//                    )
//
//                    .setAutoCancel(true) // автоматически закрыть уведомление после нажатия

            val sender = Person.Builder()
                .setName("Барсик")
                //.setIcon(...) // можно добавить значок
                .build()
            val murzik = Person.Builder()
                .setName("Мурзик")
                //.setIcon(...) // можно добавить значок
                .build()
            val vaska = Person.Builder()
                .setName("Васька")
                //.setIcon(...) // можно добавить значок
                .build()
//
            val messagingStyle = Notification.MessagingStyle(sender)
                .addMessage("Хозяин, когда кормить будут?", Date().time, sender)
                .addMessage("Привет котаны!", System.currentTimeMillis(), murzik)
                .addMessage("А вы знали, что chat по-французски кошка?", System
                    .currentTimeMillis(),
                    murzik)
                .addMessage("Круто!", System.currentTimeMillis(),
                    vaska)
                .addMessage("Ми-ми-ми", System.currentTimeMillis(), vaska)
                .addMessage("Мурзик, откуда ты знаешь французский?", System.currentTimeMillis(),
                    vaska)
                .addMessage("Шерше ля фам, т.е. ищите кошечку!", System.currentTimeMillis(),
                    murzik);
//
//
            val builder = Notification.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_pets_black_24dp)
                .setStyle(messagingStyle)
                .setChannelId(CHANNEL_ID)
//
            val channel = NotificationChannel(
                CHANNEL_ID, "channel", NotificationManager
                .IMPORTANCE_DEFAULT).apply {
                description = "Feed cat"

                val notificationManager =
                    NotificationManagerCompat.from(this@MainActivity)
                notificationManager.notify(NOTIFICATION_ID, builder.build())

            }
        }
    }
}
