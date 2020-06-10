package com.developer.allef.boilerplateapp.extensions

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat


/**
 * @author allef.santos on 09/06/20
 */


fun Context.openOtherAppActivity() {
    val i = Intent()
    i.component = ComponentName("com.google.mlkit.md", "com.google.mlkit.md.MainActivity")
    this.startActivity(i)
}

