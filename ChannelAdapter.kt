package com.dbsnetwork.iptv

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load

class ChannelAdapter(private val onItemClick: (Channel) -> Unit) :
    ListAdapter<Channel, ChannelAdapter.ChannelViewHolder>(ChannelDiffCallback()) {

    class ChannelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val channelLogoImageView: ImageView = itemView.findViewById(R.id.channelLogoImageView)
        val channelNameTextView: TextView = itemView.findViewById(R.id.channelNameTextView)

        fun bind(channel: Channel, onItemClick: (Channel) -> Unit) {
            channelNameTextView.text = channel.name
            // Load channel logo using Coil
            channelLogoImageView.load(channel.logo) {
                placeholder(R.drawable.ic_launcher_background) // Replace with a default placeholder
                error(R.drawable.ic_launcher_background) // Replace with a default error image
                crossfade(true)
            }

            itemView.setOnClickListener {
                onItemClick(channel)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChannelViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_channel, parent, false)
        return ChannelViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChannelViewHolder, position: Int) {
        val channel = getItem(position)
        holder.bind(channel, onItemClick)
    }

    class ChannelDiffCallback : DiffUtil.ItemCallback<Channel>() {
        override fun areItemsTheSame(oldItem: Channel, newItem: Channel): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Channel, newItem: Channel): Boolean {
            return oldItem == newItem
        }
    }
}

