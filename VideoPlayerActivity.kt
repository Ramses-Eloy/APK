package com.dbsnetwork.iptv

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerView

class VideoPlayerActivity : AppCompatActivity() {

    private var player: ExoPlayer? = null
    private lateinit var playerView: PlayerView
    private lateinit var loadingProgressBar: ProgressBar
    private lateinit var errorMessageTextView: TextView
    private lateinit var fullscreenButton: Button

    private var playbackPosition: Long = 0
    private var playWhenReady: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_player)

        // Initialize UI elements
        playerView = findViewById(R.id.playerView)
        loadingProgressBar = findViewById(R.id.loadingProgressBar)
        errorMessageTextView = findViewById(R.id.errorMessageTextView)
        fullscreenButton = findViewById(R.id.fullscreenButton)

        val channelUrl = intent.getStringExtra("channelUrl")
        if (channelUrl == null) {
            Toast.makeText(this, "Invalid channel URL", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        initializePlayer(channelUrl)

        fullscreenButton.setOnClickListener {
            // Implement fullscreen toggle logic here
            // For example, you can hide the status bar and navigation bar
        }
    }

    private fun initializePlayer(channelUrl: String) {
        loadingProgressBar.visibility = View.VISIBLE
        errorMessageTextView.visibility = View.GONE

        player = ExoPlayer.Builder(this).build()
        playerView.player = player

        val mediaItem = MediaItem.fromUri(Uri.parse(channelUrl))
        player?.setMediaItem(mediaItem)
        player?.playWhenReady = playWhenReady
        player?.seekTo(playbackPosition)
        player?.prepare()

        player?.addListener(object : Player.Listener {
            override fun onPlayerError(error: com.google.android.exoplayer2.PlaybackException) {
                super.onPlayerError(error)
                loadingProgressBar.visibility = View.GONE
                errorMessageTextView.visibility = View.VISIBLE
                errorMessageTextView.text = "Playback error: ${error.message}"
                Toast.makeText(this@VideoPlayerActivity, "Playback error: ${error.message}", Toast.LENGTH_SHORT).show()
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                if (isPlaying) {
                    loadingProgressBar.visibility = View.GONE
                }
            }
        })
    }

    private fun releasePlayer() {
        player?.let {
            playbackPosition = it.currentPosition
            playWhenReady = it.playWhenReady
            it.release()
            player = null
        }
    }

    override fun onStart() {
        super.onStart()
        if (player == null) {
            val channelUrl = intent.getStringExtra("channelUrl") ?: ""
            initializePlayer(channelUrl)
        }
    }

    override fun onResume() {
        super.onResume()
        if (player != null) {
            player?.playWhenReady = playWhenReady
        }
    }

    override fun onPause() {
        super.onPause()
        if (player != null) {
            player?.playWhenReady = false
        }
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }
}

