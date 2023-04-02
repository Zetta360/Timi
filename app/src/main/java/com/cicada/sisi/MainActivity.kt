package com.cicada.sisi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cicada.sisi.fragments.GMap
import com.cicada.sisi.manager.open

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        open(GMap(), supportFragmentManager)
    }
}