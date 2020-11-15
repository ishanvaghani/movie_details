package com.example.reviewz.UI

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.SparseArray
import android.view.View
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.navArgs
import at.huber.youtubeExtractor.VideoMeta
import at.huber.youtubeExtractor.YouTubeExtractor
import at.huber.youtubeExtractor.YtFile
import com.example.reviewz.API.MovieApi
import com.example.reviewz.R
import com.example.reviewz.ViewModel.MovieViewModel
import com.example.reviewz.ViewModel.TvShowViewModel
import com.example.reviewz.databinding.ActivityVideoBinding
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.exoplayer_custom_layout.*

@AndroidEntryPoint
class VideoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVideoBinding

    private var player: SimpleExoPlayer? = null

    private val movieViewModel: MovieViewModel by viewModels()
    private val tvShowViewModel: TvShowViewModel by viewModels()
    private val args: VideoActivityArgs by navArgs()

    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bindUI()

        if (savedInstanceState != null) {
            currentWindow = savedInstanceState.getInt("currentWindow")
            playWhenReady = savedInstanceState.getBoolean("playWhenReady")
            playbackPosition = savedInstanceState.getLong("playbackPosition")
        }
    }

    private fun bindUI() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        back_customLayout.setOnClickListener {
            finish()
        }
    }

    private fun getVideo() {
        var key: String
        if (args.type == getString(R.string.movie)) {
            movieViewModel.apply {
                readyMovieVideos(args.id)
                getMovieVideos().observe(this@VideoActivity, {
                    key = it[it.size - 1].key
                    getDownloadUrl(key)
                })
            }
        } else {
            tvShowViewModel.apply {
                readyTvShowVideos(args.id)
                getTvShowVideos().observe(this@VideoActivity, {
                    key = it[it.size - 1].key
                    getDownloadUrl(key)
                })
            }
        }
    }

    private fun getDownloadUrl(key: String) {
        val videoUrl = MovieApi.VIDEO_BASE_URL + key
        Log.d("VideoFragment", videoUrl)

        object : YouTubeExtractor(this) {
            override fun onExtractionComplete(ytFiles: SparseArray<YtFile>, vMeta: VideoMeta) {
                val iTag = 22
                val downloadUrl = ytFiles[iTag].url
                Log.d("VideoFragment", downloadUrl)
                initializePlayer(downloadUrl)
            }
        }.extract(videoUrl, true, true)
    }

    private fun initializePlayer(downloadUrl: String) {

        player = SimpleExoPlayer.Builder(this).build()
        binding.playerView.player = player
        binding.playerView.keepScreenOn = true

        val uri = Uri.parse(downloadUrl)
        val mediaSource: MediaSource = buildMediaSource(uri)

        player!!.playWhenReady = playWhenReady
        player!!.seekTo(currentWindow, playbackPosition)
        player!!.prepare(mediaSource, false, false)

        player!!.addListener(object : Player.EventListener {
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                if (playbackState == Player.STATE_BUFFERING) {
                    binding.progressBar.visibility = View.VISIBLE
                } else if (playbackState == Player.STATE_READY) {
                    binding.progressBar.visibility = View.GONE
                }
            }
        })
    }

    private fun buildMediaSource(uri: Uri): MediaSource {
        val dataSourceFactory: DataSource.Factory = DefaultDataSourceFactory(
            this,
            "exoplayer-codelab"
        )
        return ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(uri)
    }

    private fun releasePlayer() {
        if (player != null) {
            playbackPosition = player!!.currentPosition
            currentWindow = player!!.currentWindowIndex
            playWhenReady = player!!.playWhenReady
            player!!.release()
            player = null
        }
    }

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT > 23) {
            getVideo()
        }
    }

    override fun onResume() {
        super.onResume()
        if (Util.SDK_INT <= 23) {
            getVideo()
        }
    }

    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT <= 23) {
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT > 23) {
            releasePlayer()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        releasePlayer()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt("currentWindow", currentWindow)
        outState.putBoolean("playWhenReady", playWhenReady)
        outState.putLong("playbackPosition", playbackPosition)
    }
}