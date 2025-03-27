
package com.dbsnetwork.iptv

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dbsnetwork.iptv.Channel
import com.dbsnetwork.iptv.ChannelAdapter
import com.dbsnetwork.iptv.ChannelService
import com.dbsnetwork.iptv.R
import com.dbsnetwork.iptv.RetrofitHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.widget.Toast
import android.util.Log
import android.content.Intent

class ChannelsFragment : Fragment() {

    private lateinit var channelRecyclerView: RecyclerView
    private lateinit var channelAdapter: ChannelAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_channels, container, false)

        channelRecyclerView = view.findViewById(R.id.channelRecyclerView)
        channelRecyclerView.layoutManager = LinearLayoutManager(context)

        channelAdapter = ChannelAdapter { channel ->
            // Handle channel click - open video player
            val intent = Intent(context, com.dbsnetwork.iptv.VideoPlayerActivity::class.java)
            intent.putExtra("channelUrl", channel.url)
            startActivity(intent)
        }
        channelRecyclerView.adapter = channelAdapter

        // Load channels
        loadChannels()

        return view
    }

    private fun loadChannels() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val channelService = RetrofitHelper.getInstance().create(ChannelService::class.java)
                val response = channelService.getChannels()

                if (response.isSuccessful) {
                    val channels = response.body() ?: emptyList()
                    withContext(Dispatchers.Main) {
                        channelAdapter.submitList(channels)
                    }
                } else {
                    // Handle error
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Failed to load channels", Toast.LENGTH_SHORT).show()
                        Log.e("ChannelsFragment", "Error loading channels: ${response.message()}")
                    }
                }
            } catch (e: Exception) {
                // Handle network errors
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Network error: ${e.message}", Toast.LENGTH_SHORT).show()
                    Log.e("ChannelsFragment", "Network error loading channels: ${e.message}")
                }
            }
        }
    }
}

