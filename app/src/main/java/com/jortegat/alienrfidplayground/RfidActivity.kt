package com.jortegat.alienrfidplayground

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jortegat.alienrfidplayground.ui.main.RfidFragment

class RfidActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.rfid_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, RfidFragment.newInstance())
                .commitNow()
        }
    }
}