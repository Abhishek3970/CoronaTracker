package com.example.coronainindia

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker

class MarkerAdapter(
    val context: Context,
    val country: String,
    val cases: String,
    val active: String,
    val todayCase: String,
    val critical: String,
    val todayDeath: String,
    val recovered: String,
    val casePerMillion: String
) : GoogleMap.InfoWindowAdapter{

    var mWindow: View = LayoutInflater.from(context).inflate(R.layout.info_window,null)

    fun draw(marker: Marker , view: View){
        val title = view.findViewById<TextView>(R.id.country)
        title.text = country
    }

    override fun getInfoContents(p0: Marker?): View {
        draw(p0!!, mWindow)
        return mWindow
    }

    override fun getInfoWindow(p0: Marker?): View {
        draw(p0!!, mWindow)
        return mWindow
    }

}