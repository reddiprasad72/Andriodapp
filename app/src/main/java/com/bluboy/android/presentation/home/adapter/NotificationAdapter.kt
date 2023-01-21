package com.bluboy.android.presentation.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bluboy.android.R
import com.bluboy.android.data.models.Notification
import com.bluboy.android.databinding.ItemNotificationsBinding
import com.bluboy.android.presentation.utility.DateTimeHelper
import com.bluboy.android.presentation.utility.loadImage

open class NotificationAdapter(
    val contexts: Context,
    val homeArrayList: ArrayList<Notification>,
    val callback: (temp: String) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var rowBinding: ItemNotificationsBinding

    override fun onCreateViewHolder(container: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        rowBinding =
            ItemNotificationsBinding.inflate(
                LayoutInflater.from(container.context),
                container,
                false
            )
        return ViewHolder(rowBinding)
    }

    override fun getItemCount(): Int {
        return homeArrayList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.setIsRecyclable(false)

        if (position % 2 == 0) {
            loadImage(rowBinding.imageViewNotification, R.drawable.gift_icon)
        } else {
            loadImage(rowBinding.imageViewNotification, R.drawable.notification_cup)
        }

        rowBinding.textViewNotificationContent.text =
            homeArrayList[position].notificationMessage

        val dateTime = DateTimeHelper.convertLongToTime(homeArrayList[position].createdAt.toLong())
        var time = DateTimeHelper.dateTimeConvertServertToLocal(
            dateTime,
            input = "yyyy-MM-dd'T'hh:mm:ss",
            output = "EEEE, hh:mm a"
        )
        rowBinding.textViewTime.text = time.uppercase()
    }

    inner class ViewHolder(binding: ItemNotificationsBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

}