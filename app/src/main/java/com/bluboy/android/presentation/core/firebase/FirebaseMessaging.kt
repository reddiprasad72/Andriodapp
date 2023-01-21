package com.bluboy.android.presentation.core.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.annotation.Keep
import androidx.core.app.NotificationCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.bluboy.android.R
import com.bluboy.android.presentation.home.HomeActivity
import com.bluboy.android.presentation.utility.AppConstant
import com.bluboy.android.presentation.utility.Logger
import com.bluboy.android.presentation.utility.PrefKeys
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.android.parcel.Parcelize

open class FirebaseMessaging : FirebaseMessagingService() {

    private val TAG = "FirebaseMessaging"
    private var mNotificationId: Int = 0
    private var mContext: Context? = null

    companion object {

        const val FCM_KEY_TITLE = "title"
        const val FCM_KEY_ID = "id"
        const val FCM_KEY_TYPE = "type"
        const val FCM_KEY_BODY = "message"  //body earlier
        const val FCM_KEY_ADDITIONAL_DATA = "additional_data"
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Prefs.putString(PrefKeys.PushTokenKey, token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Logger.d("From : " + remoteMessage.from)
        Logger.d("getData() : " + remoteMessage.data)
        Logger.d("getNotification() : " + remoteMessage.notification)

        mContext = this
        if (PrefKeys.getAuthKey()
                .isNotEmpty() and PrefKeys.getUserCommon()?.pushNotificationStatus.equals("Y")
        )
            remoteMessage.apply {
                createNotification(this)
            }
    }

    private fun createNotification(remoteMessage: RemoteMessage) {
        val messageData = remoteMessage.data
        Logger.e(messageData.toString())

        var id: String? = ""
        var type: String? = ""
        var title: String? = ""
        var message: String? = ""
        var additionalData: String = ""
        var notificationData: NotificationData? = null

        var pendingIntent: PendingIntent? = null

        id = messageData[FCM_KEY_ID]
        type = messageData[FCM_KEY_TYPE]
        title = messageData[FCM_KEY_TITLE]
        message = messageData[FCM_KEY_BODY]
        additionalData = messageData[FCM_KEY_ADDITIONAL_DATA] ?: ""

        if (additionalData.isNotEmpty() && additionalData != "[]") {
            notificationData = Gson().fromJson(additionalData, NotificationData::class.java)
        }

        mNotificationId = System.currentTimeMillis().toInt()

        pendingIntent = getIntentForHome(type.orEmpty(), notificationData)

        if (notificationData?.image?.isNotEmpty() == true) {
            Glide.with(applicationContext)
                .asBitmap()
                .load(notificationData.image)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onLoadCleared(placeholder: Drawable?) {
                        popupNotification(
                            title.toString().ifBlank {
                                type.toString()
                            },
                            message.orEmpty(),
                            pendingIntent,
                            mNotificationId
                        )
                    }

                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        popupNotification(
                            title.toString().ifBlank {
                                type.toString()
                            },
                            message.orEmpty(),
                            pendingIntent,
                            mNotificationId,
                            resource
                        )
                    }

                    override fun onLoadFailed(errorDrawable: Drawable?) {
                        popupNotification(
                            title.toString().ifBlank {
                                type.toString()
                            },
                            message.orEmpty(),
                            pendingIntent,
                            mNotificationId
                        )
                    }
                })
        } else {
            popupNotification(
                title.toString().ifBlank {
                    type.toString()
                },
                message.orEmpty(),
                pendingIntent,
                mNotificationId
            )
        }
    }

    private fun popupNotification(
        type: String,
        content: String,
        pendingIntent: PendingIntent?,
        notificationId: Int,
        productImage: Bitmap? = null
    ) {
        val v = longArrayOf(500, 1000)

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, getString(R.string.app_name))
            .setSmallIcon(R.drawable.ic_notification_logo)
            .setContentTitle(type)
            .setContentText(content)
            .setStyle(NotificationCompat.BigTextStyle().bigText(content))
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setVibrate(v)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        if (productImage != null) {
            notificationBuilder.setStyle(
                NotificationCompat.BigPictureStyle().bigPicture(productImage)
            )
        }

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationBuilder.priority = NotificationManager.IMPORTANCE_HIGH

            val channel = NotificationChannel(
                getString(R.string.app_name),
                getString(R.string.app_name),
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                setShowBadge(true)
            }
            notificationManager.createNotificationChannel(channel)
        } else {
            notificationBuilder.priority = NotificationCompat.PRIORITY_MAX
        }

        notificationManager.notify(
            notificationId, notificationBuilder.build()
        )
    }

    private fun getIntentForHome(
        type: String,
        notificationData: NotificationData?
    ): PendingIntent? {
        val resultIntent = Intent(this, HomeActivity::class.java)
        resultIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val bundle = Bundle()

        bundle.putString(AppConstant.EXTRA_TYPE, type)
        bundle.putBoolean(AppConstant.OPEN_FROM_PUSH, true)
        if (notificationData != null) {
            bundle.putParcelable(NotificationData::class.java.simpleName, notificationData)
        }

        resultIntent.putExtras(bundle)

        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(
                this, 0, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        } else {
            PendingIntent.getActivity(
                this, 0, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
        return pendingIntent
    }

    @Keep
    @Parcelize
    data class NotificationData(
        @SerializedName("image")
        var image: String? = ""
    ) : Parcelable
}