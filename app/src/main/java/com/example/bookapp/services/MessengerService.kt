package com.example.bookapp.services

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.bookapp.R
import com.example.bookapp.models.MessageDTO
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.launchdarkly.eventsource.EventHandler
import com.launchdarkly.eventsource.EventSource
import com.launchdarkly.eventsource.MessageEvent
import org.json.JSONObject
import java.net.URI
import java.time.Duration

class MessengerService : Service() {
    // Binder given to clients


    private val binder = LocalBinder()
    private var eventSource: EventSource? = null
    var callback: ((newMessage: MessageDTO) -> Unit)? = null
    var pendingIntent: PendingIntent? = null

    fun startChatEvent(chatLink: String) {
        configureServeSideEvents(chatLink)
    }


    inner class LocalBinder : Binder() {
        // Return this instance of LocalService so clients can call public methods
        fun getService(): MessengerService = this@MessengerService
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    private fun configureServeSideEvents(url: String) {
        val eventHandler: EventHandler = object : EventHandler {
            override fun onOpen() {}
            override fun onComment(comment: String?) {}
            override fun onClosed() {}
            override fun onError(t: Throwable?) {}


            override fun onMessage(event: String?, messageEvent: MessageEvent) {
                val gson: Gson = GsonBuilder().setPrettyPrinting().create()
                val data = messageEvent.data

                if (data != null) {
                    val jsonObject = JSONObject(data)

                    when (jsonObject.get("type")) {
                        "message" -> {
                            val message = gson.fromJson(jsonObject.get("message").toString(), MessageDTO::class.java)

                            val temp = callback
                            if (temp != null) {
                                temp(message)
                            } else {
                                val builder = NotificationCompat.Builder(this@MessengerService, getString(R.string.message_channel_id))
                                        .setSmallIcon(R.drawable.ic_notification)
                                        .setContentTitle("New message")
                                        .setContentText(message.content.take(10))
                                        .setAutoCancel(true)
                                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                                pendingIntent?.let {
                                    builder.setContentIntent(it)
                                }

                                with(NotificationManagerCompat.from(this@MessengerService)) {
                                    notify(message.id, builder.build())
                                }
                            }

                        }
                    }
                }

            }


        }

        val event: EventSource.Builder = EventSource.Builder(
                eventHandler,
                URI.create(url)
        )
                .reconnectTime(Duration.ofMillis(10));
        val temp = event.build()
        eventSource = temp
        temp.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        eventSource?.close()
    }
}