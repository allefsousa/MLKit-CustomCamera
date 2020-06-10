package com.developer.allef.boilerplateapp

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.developer.allef.boilerplateapp.extensions.REQUEST_CODE_PHOTO_LIBRARY
import com.developer.allef.boilerplateapp.extensions.loadImage
import com.developer.allef.boilerplateapp.extensions.openImagePicker
import com.developer.allef.boilerplateapp.extensions.openOtherAppActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private var inputBitmap: Bitmap? = null
    private  val MAX_IMAGE_DIMENSION = 1024


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_new_activity.setOnClickListener {
            openOtherAppActivity()
        }

        btn_image_picker.setOnClickListener {
            openImagePicker(this)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_PHOTO_LIBRARY &&
            resultCode == Activity.RESULT_OK &&
            data != null
        ) {
            Log.d("Imagem","Uri ${data}")
            data.data?.let(::setImageToImageView)
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun setImageToImageView(imageUri:Uri){
        try {
            inputBitmap = loadImage(
                this, imageUri,
                MAX_IMAGE_DIMENSION
            )
            image_view?.setImageBitmap(inputBitmap)
        } catch (e: IOException) {
            Log.e(MainActivity::class.java.simpleName, "Failed to load file: $imageUri", e)
            return
        }
    }
}
