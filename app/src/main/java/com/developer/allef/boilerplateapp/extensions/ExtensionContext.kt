package com.developer.allef.boilerplateapp.extensions

import android.app.Activity
import android.content.ComponentName
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.exifinterface.media.ExifInterface
import java.io.IOException
import java.io.InputStream


/**
 * @author allef.santos on 09/06/20
 */


internal const val REQUEST_CODE_PHOTO_LIBRARY = 1

fun Context.openOtherAppActivity() {
    val i = Intent()
    i.component = ComponentName("com.google.mlkit.md", "com.google.mlkit.md.MainActivity")
    this.startActivity(i)
}

internal fun openImagePicker(activity: Activity) {
    val intent = Intent(Intent.ACTION_GET_CONTENT)
    intent.addCategory(Intent.CATEGORY_OPENABLE)
    intent.type = "image/*"
    activity.startActivityForResult(intent, REQUEST_CODE_PHOTO_LIBRARY)
}

@Throws(IOException::class)
internal fun loadImage(context: Context, imageUri: Uri, maxImageDimension: Int): Bitmap? {
    var inputStreamForSize: InputStream? = null
    var inputStreamForImage: InputStream? = null
    try {
        inputStreamForSize = context.contentResolver.openInputStream(imageUri)
        var opts = BitmapFactory.Options()
        opts.inJustDecodeBounds = true
        BitmapFactory.decodeStream(inputStreamForSize, null, opts)/* outPadding= */
        val inSampleSize = Math.max(opts.outWidth / maxImageDimension, opts.outHeight / maxImageDimension)

        opts = BitmapFactory.Options()
        opts.inSampleSize = inSampleSize
        inputStreamForImage = context.contentResolver.openInputStream(imageUri)
        val decodedBitmap = BitmapFactory.decodeStream(inputStreamForImage, null, opts)/* outPadding= */
        return maybeTransformBitmap(
            context.contentResolver,
            imageUri,
            decodedBitmap
        )
    } finally {
        inputStreamForSize?.close()
        inputStreamForImage?.close()
    }
}
private fun maybeTransformBitmap(resolver: ContentResolver, uri: Uri, bitmap: Bitmap?): Bitmap? {
    val matrix: Matrix? = when (getExifOrientationTag(resolver, uri)) {
        ExifInterface.ORIENTATION_UNDEFINED, ExifInterface.ORIENTATION_NORMAL ->
            // Set the matrix to be null to skip the image transform.
            null
        ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> Matrix().apply { postScale(-1.0f, 1.0f) }

        ExifInterface.ORIENTATION_ROTATE_90 -> Matrix().apply { postRotate(90f) }
        ExifInterface.ORIENTATION_TRANSPOSE -> Matrix().apply { postScale(-1.0f, 1.0f) }
        ExifInterface.ORIENTATION_ROTATE_180 -> Matrix().apply { postRotate(180.0f) }
        ExifInterface.ORIENTATION_FLIP_VERTICAL -> Matrix().apply { postScale(1.0f, -1.0f) }
        ExifInterface.ORIENTATION_ROTATE_270 -> Matrix().apply { postRotate(-90.0f) }
        ExifInterface.ORIENTATION_TRANSVERSE -> Matrix().apply {
            postRotate(-90.0f)
            postScale(-1.0f, 1.0f)
        }
        else ->
            // Set the matrix to be null to skip the image transform.
            null
    }

    return if (matrix != null) {
        Bitmap.createBitmap(bitmap!!, 0, 0, bitmap.width, bitmap.height, matrix, true)
    } else {
        bitmap
    }
}

private fun getExifOrientationTag(resolver: ContentResolver, imageUri: Uri): Int {
    if (ContentResolver.SCHEME_CONTENT != imageUri.scheme && ContentResolver.SCHEME_FILE != imageUri.scheme) {
        return 0
    }

    var exif: ExifInterface? = null
    try {
        resolver.openInputStream(imageUri)?.use { inputStream -> exif = ExifInterface(inputStream) }
    } catch (e: IOException) {
        Log.e("Teste", "Failed to open file to read rotation meta data: $imageUri", e)
    }

    return if (exif != null) {
        exif!!.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
    } else {
        ExifInterface.ORIENTATION_UNDEFINED
    }
}

fun isPortraitMode(context: Context): Boolean =
    context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT

