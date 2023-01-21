package com.bluboy.android.presentation.home

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bluboy.android.R
import com.bluboy.android.data.models.Notification
import com.bluboy.android.data.models.NotificationRs
import com.bluboy.android.databinding.ActivityNotificationBinding
import com.bluboy.android.presentation.core.BaseActivity
import com.bluboy.android.presentation.home.adapter.NotificationAdapter
import com.bluboy.android.presentation.utility.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class NotificationActivity : BaseActivity() {

    private val homeViewModel: HomeViewModel by viewModel()
    private lateinit var binding: ActivityNotificationBinding

    override fun getBaseViewModel() = homeViewModel

    private lateinit var notificationAdapter: NotificationAdapter
    private var arrayNotification = ArrayList<Notification>()

    private var todayDate = ""
    private var yesterdayDate = ""
    var isloading: Boolean = false


    private var page = 1
    private var isInProgress = false
    private var notificatoinRs: NotificationRs? = null

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setLightStatusBar(true)
        statusBarGone()

        binding.tool.txtHeader.text = getString(R.string.label_notifications)

        binding.tool.imageViewBack.setOnClickListener {
            onBackPressed()
        }

        init()
    }

    override fun onResume() {
        super.onResume()
        if (page == 1) {
//            showProgress()
            binding.mShimmerViewNotification.visible()
            binding.llEmpty.gone()
            binding.recyclerViewNotification.gone()
            homeViewModel.getNotificationList(1)
        }
    }

    @SuppressLint("FragmentLiveDataObserve")
    private fun attachObserver() {
        homeViewModel.NotificationObserver.observe(this, Observer {
            isInProgress = false
//            hideProgress()
            binding.mShimmerViewNotification.gone()
            binding.progressBottom.gone()
            if (it.status == 1) {
                binding.llEmpty.gone()
                binding.recyclerViewNotification.visible()
                notificatoinRs = it
                if (page == 1) {
                    isloading = false
                    arrayNotification.clear()
                    it.data?.notifications?.let { it1 -> arrayNotification.addAll(it1) }
                } else {
                    it.data?.notifications?.let { it1 -> arrayNotification.addAll(it1) }
                }
                if (arrayNotification.size==0){
                    binding.recyclerViewNotification.gone()
                    binding.llEmpty.visible()
                }

                notificationAdapter?.notifyDataSetChanged()
            } else {
                binding.recyclerViewNotification.gone()
                binding.llEmpty.visible()
            }
        })
    }

    private fun init() {
        attachObserver()
        var c = Calendar.getInstance()
        val df = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        todayDate = df.format(c.timeInMillis)
        c.add(Calendar.DAY_OF_WEEK, -1)
        yesterdayDate = df.format(c.timeInMillis)

        var linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerViewNotification.layoutManager = linearLayoutManager
        notificationAdapter = NotificationAdapter(this, arrayNotification, {
//            startActivityCustom(IntentHelper.getOrderDetailIntent(activity!!))
        })
        binding.recyclerViewNotification.adapter = notificationAdapter


        val sectionItemDecoration = RecyclerSectionItemDecoderSticky(
            resources.getDimension(R.dimen._35sdp).toInt(),
            false, getSectionCallback(arrayNotification)
        )
        binding.recyclerViewNotification.addItemDecoration(sectionItemDecoration)

        binding.recyclerViewNotification.addOnScrollListener(object :
            EndlessPaginationScrollListener() {
            override fun requestNewPage() {
                super.requestNewPage()
                if (!isloading && notificatoinRs?.data?.isHaveMoreRecords.equals("Yes")) {
                    page++
                    binding.progressBottom.visible()
                    getNotifications()
                }
            }
        })

    }

    private fun getNotifications() {
        homeViewModel.getNotificationList(page)
        isloading = true

    }

    private fun getSectionCallback(activitList: List<Notification>): RecyclerSectionItemDecoderSticky.SectionCallback {
        return object : RecyclerSectionItemDecoderSticky.SectionCallback {
            override fun isSection(position: Int): Boolean {
                var dateTimeFirst =
                    DateTimeHelper.convertLongToTime(activitList[position].updatedAt.toLong())
                var first = DateTimeHelper.convertDate(
                    dateTimeFirst,
                    input = "yyyy-MM-dd'T'hh:mm:ss",
                    output = "dd-MM-yyyy"
                )
                var second = ""
                if (position != 0) {
                    var dateTimeSecond =
                        DateTimeHelper.convertLongToTime(activitList[position - 1].updatedAt.toLong())
                    second = DateTimeHelper.convertDate(
                        dateTimeSecond,
                        input = "yyyy-MM-dd'T'hh:mm:ss",
                        output = "dd-MM-yyyy"
                    )
                }
                return position == 0 || first[0] != second[0] || first[1] != second[1]
            }

            override fun getSectionHeader(position: Int): CharSequence {
                val dateTime =
                    DateTimeHelper.convertLongToTime(activitList[position].createdAt.toLong())
                var stickyDate = DateTimeHelper.convertDate(
                    dateTime, input = "yyyy-MM-dd'T'hh:mm:ss", output = "dd-MM-yyyy"
                )

                return when (stickyDate) {
                    todayDate -> "Today"
                    yesterdayDate -> "Yesterday"
                    else -> stickyDate
                }
            }
        }
    }

}