package com.dbsnetwork.iptv

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.content.Intent

class HomeFragment : Fragment() {

    private lateinit var featuredChannelsRecyclerView: RecyclerView
    private lateinit var recentlyWatchedRecyclerView: RecyclerView
    private lateinit var popularMoviesRecyclerView: RecyclerView

    private lateinit var featuredChannelsAdapter: ChannelAdapter
    private lateinit var recentlyWatchedAdapter: ChannelAdapter
    private lateinit var popularMoviesAdapter: ChannelAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Initialize RecyclerViews and Adapters
        featuredChannelsRecyclerView = view.findViewById(R.id.featuredChannelsRecyclerView)
        recentlyWatchedRecyclerView = view.findViewById(R.id.recentlyWatchedRecyclerView)
        popularMoviesRecyclerView = view.findViewById(R.id.popularMoviesRecyclerView)

        featuredChannelsRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recentlyWatchedRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        popularMoviesRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        featuredChannelsAdapter = ChannelAdapter { channel ->
            // Handle channel click - open video player
            openVideoPlayer(channel.url)
        }
        recentlyWatchedAdapter = ChannelAdapter { channel ->
            // Handle channel click - open video player
            openVideoPlayer(channel.url)
        }
        popularMoviesAdapter = ChannelAdapter { channel ->
            // Handle movie click - open video player
            openVideoPlayer(channel.url)
        }

        featuredChannelsRecyclerView.adapter = featuredChannelsAdapter
        recentlyWatchedRecyclerView.adapter = recentlyWatchedAdapter
        popularMoviesRecyclerView.adapter = popularMoviesAdapter

        // Load data
        loadFeaturedChannels()
        loadRecentlyWatched()
        loadPopularMovies()

        return view
    }

    private fun openVideoPlayer(channelUrl: String) {
        val intent = Intent(context, VideoPlayerActivity::class.java)
        intent.putExtra("channelUrl", channelUrl)
        startActivity(intent)
    }

    private fun loadFeaturedChannels() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val channelService = RetrofitHelper.getInstance().create(ChannelService::class.java)
                val response = channelService.getFeaturedChannels() // Using new API call

                if (response.isSuccessful) {
                    val channels = response.body() ?: emptyList()
                    withContext(Dispatchers.Main) {
                        featuredChannelsAdapter.submitList(channels)
                    }
                } else {
                    // Handle error
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Failed to load featured channels", Toast.LENGTH_SHORT).show()
                        Log.e("HomeFragment", "Error loading featured channels: ${response.message()}")
                    }
                }
            } catch (e: Exception) {
                // Handle network errors
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Network error: ${e.message}", Toast.LENGTH_SHORT).show()
                    Log.e("HomeFragment", "Network error loading featured channels: ${e.message}")
                }
            }
        }
    }

    private fun loadRecentlyWatched() {
        //TODO: Load data from local data source (Room database, SharedPreferences, etc.)
        //For now, load some dummy data
        val dummyData = listOf(
            Channel("Recent Channel 1", "http://example.com/channel1", "https://example.com/channel1.png"),
            Channel("Recent Channel 2", "http://example.com/channel2", "https://example.com/channel2.png")
        )

        recentlyWatchedAdapter.submitList(dummyData)
    }

    private fun loadPopularMovies() {
        //TODO: Load data from your API
        //For now, load some dummy data
        val dummyData = listOf(
            Channel("Movie 1", "http://example.com/movie1", "https://example.com/movie1.png"),
            Channel("Movie 2", "http://example.com/movie2", "https://example.com/movie2.png")
        )

        popularMoviesAdapter.submitList(dummyData)
    }
}

