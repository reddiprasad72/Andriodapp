package com.bluboy.android.presentation.settings

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bluboy.android.R
import com.bluboy.android.data.models.FAQDetail
import com.bluboy.android.databinding.ActivityFaqBinding
import com.bluboy.android.presentation.core.BaseActivity
import com.bluboy.android.presentation.home.HomeViewModel
import com.bluboy.android.presentation.settings.adapter.FaqAdapter
import com.bluboy.android.presentation.utility.*
import org.koin.android.viewmodel.ext.android.viewModel

class FaqActivity : BaseActivity() {

    private val homeViewModel: HomeViewModel by viewModel()

    override fun getBaseViewModel() = homeViewModel
    private lateinit var faqAdapter: FaqAdapter
    private var arrayListFaq = ArrayList<FAQDetail>()

    var page: Int = 1
    var isloading: Boolean = false
    var hasmore: String = ""

    private lateinit var binding: ActivityFaqBinding

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFaqBinding.inflate(layoutInflater)
        setContentView(binding.root)

        changeStatusBarColor(R.color.colorTransparent)
        setLightStatusBar(false)
        statusBarGone()
        setToolBar()
        init()
    }

    fun setToolBar() {
        binding.tool.txtHeader.text = getString(R.string.label_faq)
        binding.tool.imageViewBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun init() {
        attachObserver()

        getFAQ()

        var linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerViewFaq.layoutManager = linearLayoutManager
        faqAdapter = FaqAdapter(this, arrayListFaq) { faq, position, isExpand ->

            if (isExpand) {

                arrayListFaq.forEach {
                    it.isExpanded = false
                }
                arrayListFaq[position].isExpanded = true
                faqAdapter.notifyDataSetChanged()
            } else {
                arrayListFaq.forEach {
                    it.isExpanded = false
                }
                arrayListFaq[position].isExpanded = false
                faqAdapter.notifyDataSetChanged()
            }

        }

        binding.recyclerViewFaq.adapter = faqAdapter

        binding.recyclerViewFaq.addOnScrollListener(object : EndlessPaginationScrollListener() {
            override fun requestNewPage() {
                super.requestNewPage()
                if (!isloading && hasmore == AppConstant.Yes) {
                    page++
                    getFAQ()
                }
            }
        })

    }

    private fun getFAQ() {
        showProgress()
        homeViewModel.getFAQs(page)
        isloading = true
    }

    private fun attachObserver() {
        homeViewModel.faqDataListRSLiveData.observe(this, Observer {
            it?.apply {
                hideProgress()
                if (it.status == 1) {
                    isloading = false
                    it.faqData?.faqs?.let { it1 -> arrayListFaq.addAll(it1) }
                    faqAdapter.notifyDataSetChanged()
                }
            }
        })
    }
}
