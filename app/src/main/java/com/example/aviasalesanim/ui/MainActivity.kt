package com.example.aviasalesanim.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.aviasalesanim.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, SelectCitiesFragment())
                .commitNow()
        }
    }
}
