
package com.dbsnetwork.iptv

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.dbsnetwork.iptv.R

class TVShowsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_tv_shows, container, false)

        val textView = view.findViewById<TextView>(R.id.text_tv_shows)

        // Set the text of the TextView
        textView.text = "TV Shows"

        return view
    }
}

