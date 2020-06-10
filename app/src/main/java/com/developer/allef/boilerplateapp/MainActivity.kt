package com.developer.allef.boilerplateapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.developer.allef.boilerplateapp.extensions.openOtherAppActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_new_activity.setOnClickListener {
            openOtherAppActivity()
        }
    }
}
