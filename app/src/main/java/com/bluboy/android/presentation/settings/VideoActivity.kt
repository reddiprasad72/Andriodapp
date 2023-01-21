package com.bluboy.android.presentation.settings

import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.bluboy.android.databinding.ActivityVideoBinding
import com.bluboy.android.presentation.utility.AppConstant

class VideoActivity : AppCompatActivity() {

    private var url: String? = null
    private var mode = ""
    private var mediaUri: Uri? = null
    lateinit var player: SimpleExoPlayer
    private lateinit var binding: ActivityVideoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mode = intent.getStringExtra(AppConstant.GAME_MODE).toString()
        url = intent.getStringExtra(AppConstant.VIDEO_URL)

        if (mode.equals(AppConstant.GAME_ORIENTATION_LANDSCAPE)) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE;

        } else if (mode.equals(AppConstant.GAME_ORIENTATION_PORTRAIT)) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT;
        }

        player = SimpleExoPlayer.Builder(this).build()
        binding.exoplayerView.player = player
        mediaUri = Uri.parse(url)

        val mediaItem: MediaItem = MediaItem.fromUri(mediaUri!!)
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()
    }

    override fun onPause() {
        super.onPause()
        player.playWhenReady = false
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }
}