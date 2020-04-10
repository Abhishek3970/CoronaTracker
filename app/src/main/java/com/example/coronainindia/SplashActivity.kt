package com.example.coronainindia

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    private val SPLASH_DELAY = 1500

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({ start() }, SPLASH_DELAY.toLong())
    }

    private fun start() {
        startActivity(Intent(this,MapsActivity::class.java))
        finish()
    }
}
