package com.bluboy.android.presentation.settings

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bluboy.android.R
import com.bluboy.android.data.models.HowToPlayContent
import com.bluboy.android.data.models.HowToPlayRs
import com.bluboy.android.databinding.ActivityHowToPlayBinding
import com.bluboy.android.presentation.core.BaseActivity
import com.bluboy.android.presentation.home.HomeViewModel
import com.bluboy.android.presentation.settings.adapter.HowToPlayAdapter
import com.bluboy.android.presentation.utility.*
import org.koin.android.viewmodel.ext.android.viewModel

class HowToPlayActivity : BaseActivity() {

    private val homeViewModel: HomeViewModel by viewModel()

    override fun getBaseViewModel() = homeViewModel
    private lateinit var howToPlayAdapter: HowToPlayAdapter
    private var arrayListHowToPlay = ArrayList<HowToPlayContent>()
    private var page = 1
    private var isInProgress = false
    private var howToPlayRs: HowToPlayRs? = null

    private lateinit var binding: ActivityHowToPlayBinding

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHowToPlayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        changeStatusBarColor(R.color.colorTransparent)
        setLightStatusBar(false)
        statusBarGone()
        setToolBar()
        init()
    }

    fun setToolBar() {
        binding.tool.txtHeader.text = getString(R.string.label_how_to_play)
        binding.tool.imageViewBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun init() {
        attachObserver()

        homeViewModel.getHowToPlay(page)

        var linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerViewHowToPlay.layoutManager = linearLayoutManager
        howToPlayAdapter = HowToPlayAdapter(arrayListHowToPlay) {

            when (it.content_type) {
                "VIDEO" -> {
                    var i = Intent(this@HowToPlayActivity, VideoActivity::class.java)
                    i.putExtra(AppConstant.VIDEO_URL, it.content_video_url)
                    i.putExtra(AppConstant.GAME_MODE, it.game_orientation)
                    startActivity(i)
                }
                "IMAGE" -> {
                    var i = Intent(this@HowToPlayActivity, PhotoZoomActivity::class.java)
                    i.putExtra(AppConstant.URL_LINK, it.content_image_url)
                    startActivity(i)
                }
                else -> {
                    var i = Intent(this@HowToPlayActivity, HowToPlayTextOnlyActivity::class.java)
                    i.putExtra(AppConstant.TEXT_HOW_TO_PLAY, it.content_description)
                    i.putExtra(AppConstant.GAME_NAME, it.game_name)
                    startActivity(i)
                }
            }
        }

        binding.recyclerViewHowToPlay.adapter = howToPlayAdapter

        binding.recyclerViewHowToPlay.addOnScrollListener(object :
            EndlessPaginationScrollListener() {
            override fun requestNewPage() {
                super.requestNewPage()
                if (howToPlayRs?.data?.isHaveMoreRecords.equals("Yes") && !isInProgress) {
                    page++
                    homeViewModel.getHowToPlay(page)
                }
            }
        })
    }

    private fun attachObserver() {
        homeViewModel.howtoPlayObserver.observe(this, Observer {
            isInProgress = false
            if (it.status == 1) {
                howToPlayRs = it
                binding.recyclerViewHowToPlay.visible()
                binding.llEmpty.root.gone()
                arrayListHowToPlay.clear()
                arrayListHowToPlay.addAll(it.data?.content as ArrayList<HowToPlayContent>)
                if (arrayListHowToPlay.size==0){
                    binding.recyclerViewHowToPlay.gone()
                    binding.llEmpty.root.visible()
                }
                howToPlayAdapter.notifyDataSetChanged()

            } else {
                binding.recyclerViewHowToPlay.gone()
                binding.llEmpty.root.visible()
            }
        })
    }
}
