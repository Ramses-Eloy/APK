package com.dbsnetwork.iptv

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChannelListActivity : AppCompatActivity() {

    private lateinit var channelRecyclerView: RecyclerView
    private lateinit var channelAdapter: ChannelAdapter
    private lateinit var loadingProgressBar: ProgressBar
    private lateinit var emptyStateTextView: TextView
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_channel_list)

        // Initialize UI elements
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "DBS Network IPTV"

        channelRecyclerView = findViewById(R.id.channelRecyclerView)
        channelRecyclerView.layoutManager = LinearLayoutManager(this)

        loadingProgressBar = findViewById(R.id.loadingProgressBar)
        emptyStateTextView = findViewById(R.id.emptyStateTextView)

        channelAdapter = ChannelAdapter { channel ->
            // Handle channel click
            val intent = Intent(this, VideoPlayerActivity::class.java)
            intent.putExtra("channelUrl", channel.url)
            startActivity(intent)
        }
        channelRecyclerView.adapter = channelAdapter

        // Load channels
        loadChannels()
    }

    private fun loadChannels() {
        loadingProgressBar.visibility = View.VISIBLE
        emptyStateTextView.visibility = View.GONE

        CoroutineScope(Dispatchers.IO).launch {
            try {
                //TODO: Replace with your actual API call to fetch channels
                val channelService = RetrofitHelper.getInstance().create(ChannelService::class.java)
                val response = channelService.getChannels()

                if (response.isSuccessful) {
                    val channels = response.body() ?: emptyList()
                    withContext(Dispatchers.Main) {
                        loadingProgressBar.visibility = View.GONE
                        if (channels.isEmpty()) {
                            emptyStateTextView.visibility = View.VISIBLE
                        } else {
                            channelAdapter.submitList(channels)
                        }
                    }
                } else {
                    // Handle error
                    withContext(Dispatchers.Main) {
                        loadingProgressBar.visibility = View.GONE
                        emptyStateTextView.visibility = View.VISIBLE
                        Toast.makeText(this@ChannelListActivity, "Failed to load channels", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                // Handle network errors
                withContext(Dispatchers.Main) {
                    loadingProgressBar.visibility = View.GONE
                    emptyStateTextView.visibility = View.VISIBLE
                    Toast.makeText(this@ChannelListActivity, "Network error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

