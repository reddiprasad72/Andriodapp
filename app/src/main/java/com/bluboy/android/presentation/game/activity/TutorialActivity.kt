package com.bluboy.android.presentation.game.activity

import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.bluboy.android.R
import com.bluboy.android.databinding.ActivityTutorialBinding
import com.bluboy.android.presentation.core.BaseActivity
import com.bluboy.android.presentation.game.adapters.TutorialAdapter
import com.bluboy.android.presentation.loginsignup.LoginSignupViewModel
import com.bluboy.android.presentation.utility.AppConstant
import com.bluboy.android.presentation.utility.changeStatusBarColor
import com.bluboy.android.presentation.utility.loadImage
import com.bluboy.android.presentation.utility.setLightStatusBar
import org.koin.android.viewmodel.ext.android.viewModel

class TutorialActivity : BaseActivity() {

    private val loginSignupViewModel: LoginSignupViewModel by viewModel()

    override fun getBaseViewModel() = loginSignupViewModel

    private lateinit var binding: ActivityTutorialBinding
    private var gameKey = ""

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTutorialBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(binding.root)

        changeStatusBarColor(R.color.colorTransparent)
        setLightStatusBar(false)

        window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            statusBarColor =
                ContextCompat.getColor(context, R.color.colorTransparent) //Color.TRANSPARENT
        }

        gameKey = intent?.getStringExtra(AppConstant.GAME_NAME).toString()


        /*binding = ActivityTutorialBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)
        gameName = intent?.getStringExtra(AppConstant.GAME_NAME).toString()
        */

        if (gameKey == "candy_crush") {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
//            loadImage(binding.imageViewBackground, R.drawable.bg_cc)
            loadImage(binding.imageViewBackground, R.drawable.back_app)
        } else if (gameKey == "bluboy_runner") {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
            loadImage(binding.imageViewBackground, R.drawable.bg_runner)
        } else {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
            loadImage(binding.imageViewBackground, R.drawable.bg_fc)
        }

        if (gameKey == "candy_crush"  || gameKey == "bluboy_runner"
        ) {
            val param = binding.buttonNext.layoutParams as ViewGroup.MarginLayoutParams
            param.setMargins(0, 0, 0, dpToPx(12))
            binding.buttonNext.layoutParams = param
        } else {
            val param = binding.buttonNext.layoutParams as ViewGroup.MarginLayoutParams
            param.setMargins(0, 0, dpToPx(53), 0)
            binding.buttonNext.layoutParams = param
        }

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        val adapter = TutorialAdapter(
            this,
            if (gameKey == "candy_crush") listOf(
                R.drawable.cc_one,
                R.drawable.cc_two,
                R.drawable.cc_three,
            )
            else if (gameKey == "fruit_ninja") listOf(
                R.drawable.fc_one,
                R.drawable.fc_two,
                R.drawable.fc_three,
                R.drawable.fc_four

            )
            else // "Runner"
                listOf(
                    R.drawable.runner_one,
                    R.drawable.runner_two,
                    R.drawable.runner_three,
                    R.drawable.runner_four,
                    R.drawable.runner_five
                )
        )

        binding.viewPagerTutorials.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                Log.i("PAGE", "PAGE SCROLL" + position)
            }

            override fun onPageSelected(position: Int) {
                Log.i("PAGE", "PAGE SELECTED" + position)
            }
        })

        binding.buttonNext.setOnClickListener {
            if (binding.viewPagerTutorials.currentItem == 2) {
                if (gameKey == "candy_crush") {
                    finish()
                } else {
                    binding.viewPagerTutorials.currentItem =
                        binding.viewPagerTutorials.currentItem + 1
                }
            } else if (binding.viewPagerTutorials.currentItem == 3) {
                if (gameKey == "fruit_ninja") {
                    finish()
                } else {
                    binding.viewPagerTutorials.currentItem =
                        binding.viewPagerTutorials.currentItem + 1
                }
            } else if (binding.viewPagerTutorials.currentItem == 4) {
                if (gameKey == "bluboy_runner") {
                    finish()
                }
            } else {
                binding.viewPagerTutorials.currentItem = binding.viewPagerTutorials.currentItem + 1
            }
        }
        binding.viewPagerTutorials.adapter = adapter
    }

    //endregion
    private fun dpToPx(dp: Int): Int {
        return (dp * resources.displayMetrics.density + 0.5f).toInt()
    }
}