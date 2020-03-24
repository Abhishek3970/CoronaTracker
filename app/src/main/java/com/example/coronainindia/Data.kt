package com.example.coronainindia

import com.google.android.gms.maps.model.LatLng

data class Data (
    val country: String = "",
    val cases: String = "",
    val active: String = "",
    val todayCases: String = "",
    val critical: String = "",
    val todayDeath: String = "",
    val recovered: String = "",
    val casePerMillion: String = "",
    val deaths: String = "",
    val latLong: LatLng
)