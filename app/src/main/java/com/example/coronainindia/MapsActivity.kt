package com.example.coronainindia

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.json.JSONObject
import java.util.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var mQueue: RequestQueue
    lateinit var  data: ArrayList<Data>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        mQueue = Volley.newRequestQueue(this)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        data = ArrayList<Data>()

        val request = getData()

        mQueue.add(request)

        addOnInfoWindowClickListener()


    }

    private fun addOnInfoWindowClickListener() {
        mMap.setOnInfoWindowClickListener { marker ->
            for(i in data){
                if(marker.position == i.latLong){
                        createDialog(i)
                }
            }
        }
    }

    private fun createDialog(data: Data) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.info_window)

        val country:TextView = dialog.findViewById(R.id.country)
        val cases:TextView = dialog.findViewById(R.id.cases)
        val active:TextView = dialog.findViewById(R.id.active)
        val todayCases:TextView = dialog.findViewById(R.id.todayCases)
        val critical:TextView = dialog.findViewById(R.id.critical)
        val deaths:TextView = dialog.findViewById(R.id.deaths)
        val todayDeaths:TextView = dialog.findViewById(R.id.todayDeaths)
        val recovered:TextView = dialog.findViewById(R.id.recovered)
        val casesPerOneMillion:TextView = dialog.findViewById(R.id.casesPerOneMillion)
        val tests:TextView = dialog.findViewById(R.id.tests)
        val testsPerOneMillion:TextView = dialog.findViewById(R.id.testPerOneMillion)
        val deathPerOneMillion:TextView = dialog.findViewById(R.id.deathPerOneMillion)

        country.text = data.country
        cases.text = "Cases: " + data.cases
        active.text = "Active: "+data.active
        todayCases.text = "Today's Cases: "+data.todayCases
        critical.text = "Critical: "+data.critical
        deaths.text = "Deaths: "+data.deaths
        todayDeaths.text = "Today's Deaths: "+data.todayDeath
        recovered.text = "Recovered: "+data.recovered
        casesPerOneMillion.text ="Cases Per One Million: " + data.casePerMillion
        tests.text = "Tests: "+ data.tests
        testsPerOneMillion.text = "TestsPerOneMillion: "+ data.testsPerOneMillion
        deathPerOneMillion.text = "DeathPerOneMillion: "+ data.deathsPerOneMillion



        dialog.show()

    }

    private fun getData(): JsonArrayRequest {

        val uri = "https://corona.lmao.ninja/countries"

        return JsonArrayRequest(
            Request.Method.GET, uri, null,
            Response.Listener { response ->

                for (i in 0 until response.length()) {

                    val item = response.getJSONObject(i)
                    val info = item.getJSONObject("countryInfo")

                    val countryName = item.getString("country")
                    val cases = item.getInt("cases")

                    val lat = info.getInt("lat").toDouble()
                    val long = info.getInt("long").toDouble()
                    val itemLatLong = LatLng(lat, long)
                    mMap.addMarker(
                        MarkerOptions().position(itemLatLong).title(countryName).snippet(
                            "Total Cases: $cases"
                        )
                    )

                    data.add(
                        Data(
                            country = item?.getString("country"),
                            cases = item?.getString("cases").toString(),
                            active = item?.getString("active").toString(),
                            todayCases = item?.getString("todayCases").toString(),
                            critical = item?.getString("critical").toString(),
                            todayDeath = item?.getString("todayDeaths").toString(),
                            recovered = item?.getString("recovered").toString(),
                            casePerMillion = item?.getString("casesPerOneMillion").toString(),
                            deaths = item?.getString("deaths").toString(),
                            tests = item?.getString("tests").toString(),
                            deathsPerOneMillion= item?.getString("deathsPerOneMillion").toString(),
                            testsPerOneMillion= item?.getString("testsPerOneMillion").toString(),
                            latLong = itemLatLong
                        )
                    )

//                        Log.i("ct" , item?.getString("country"))

                }
            },
            Response.ErrorListener {
                it.printStackTrace()
            })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.map_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.normal_map -> {
                mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
                true
            }
            R.id.hybrid_map -> {
                mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
                true
            }
            R.id.satellite_map -> {
                mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
                true
            }
            R.id.terrain_map -> {
                mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
                true
            }
            R.id.overView -> {
                statusDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun statusDialog() {

        val uri = "https://corona.lmao.ninja/all"
        var cases = ""
        var deaths = ""
        var recovered = ""

        val request = JsonObjectRequest(Request.Method.GET, uri, null,
            Response.Listener { response ->
                cases = response.getInt("cases").toString()
                deaths = response.getInt("deaths").toString()
                recovered = response.getInt("recovered").toString()

                val dialog = Dialog(this)
                dialog.setContentView(R.layout.global)

                val cases_text:TextView = dialog.findViewById(R.id.cases_global)
                val recovered_text:TextView = dialog.findViewById(R.id.recovered_global)
                val death_text:TextView = dialog.findViewById(R.id.deaths_global)
//                Log.i("inTheLinesAfter", "$cases $deaths $recovered")

                cases_text.text = "Cases: $cases"
                recovered_text.text = "Recovered: $recovered"
                death_text.text = "Deaths: $deaths"

                dialog.show()

            },
            Response.ErrorListener {
                it.printStackTrace()
            })

        mQueue.add(request)


    }


}

