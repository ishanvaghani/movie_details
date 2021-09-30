package com.example.reviewz.UI

import android.os.Bundle
import android.widget.Toast
import androidx.navigation.navArgs
import com.example.reviewz.databinding.ActivityVideoBinding
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubeInitializationResult

class VideoActivity : YouTubeBaseActivity() {

    private lateinit var binding: ActivityVideoBinding

    private val args: VideoActivityArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getDownloadUrl(args.key)
    }

    private fun getDownloadUrl(key: String) {

        val listener = object : YouTubePlayer.OnInitializedListener {
                override fun onInitializationSuccess(
                    provider: YouTubePlayer.Provider,
                    youTubePlayer: YouTubePlayer, b: Boolean
                ) {

                    youTubePlayer.loadVideo(key)
                    youTubePlayer.play()
                }

                override fun onInitializationFailure(
                    provider: YouTubePlayer.Provider,
                    youTubeInitializationResult: YouTubeInitializationResult
                ) {
                    Toast.makeText(this@VideoActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
            }

        binding.youtubePlayer.initialize("AIzaSyDvu4IjB_2uxBajnUsUokJda3NRP4tOnD4", listener)
    }
}