package com.bluboy.android.presentation.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.parseAsHtml
import com.bluboy.android.R
import com.bluboy.android.databinding.ActivityHowToPlayTextOnlyBinding
import com.bluboy.android.presentation.utility.*

class HowToPlayTextOnlyActivity : AppCompatActivity() {

    private var textHowToPlay: String? = ""
    private var gameName: String? = ""
    private lateinit var binding: ActivityHowToPlayTextOnlyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHowToPlayTextOnlyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        textHowToPlay = intent.getStringExtra(AppConstant.TEXT_HOW_TO_PLAY)
        gameName = intent?.getStringExtra(AppConstant.GAME_NAME)

        changeStatusBarColor(R.color.colorTransparent)
        setLightStatusBar(false)
        statusBarGone()
        binding.tool.txtHeader.text =
            String.format("%s - %s", getString(R.string.label_how_to_play), gameName)

        binding.tool.imageViewBack.setOnClickListener {
            onBackPressed()
        }
        binding.longText.text = textHowToPlay.toString().parseAsHtml()
    }

}