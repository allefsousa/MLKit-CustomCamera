package com.developer.allef.boilerplateapp.customview

import android.content.Context
import android.util.AttributeSet
import android.view.SurfaceView
import android.widget.FrameLayout
import com.google.android.gms.common.images.Size
import com.google.android.gms.vision.CameraSource

/**
 * @author allef.santos on 09/06/20
 */
//Custom camera
class CameraSourcePreview(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

//    private val surfaceView: SurfaceView = SurfaceView(context).apply {
//        holder.addCallback(SurfaceCallback())
//        addView(this)
//    }

    private var graphicOverlay: GraphicOverlay? = null
    private var startRequested = false
    private var surfaceAvailable = false
    private var cameraSource: CameraSource? = null
    private var cameraPreviewSize: Size? = null

}