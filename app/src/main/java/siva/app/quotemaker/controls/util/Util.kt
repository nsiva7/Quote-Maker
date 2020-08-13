package siva.app.quotemaker.controls.util

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.FileProvider
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.io.IOException
import java.io.InputStream


/*
* Created by Siva Nimmala on 12/8/20.
* */
class Util {

    companion object {
        fun getBitmapFromAsset(context: Context, strName: String): Bitmap? {
            val assetManager = context.assets
            var inputStream: InputStream?
            return try {
                inputStream = assetManager.open(strName)
                BitmapFactory.decodeStream(inputStream)
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
        }

        @Throws(IOException::class)
        fun getRotateImage(photoPath: String?, bitmap: Bitmap?): Bitmap? {
            val ei = ExifInterface(photoPath!!)
            val orientation = ei.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED
            )
            var rotatedBitmap: Bitmap?
            rotatedBitmap = when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(bitmap!!, 90)
                ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(bitmap!!, 180)
                ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(bitmap!!, 270)
                ExifInterface.ORIENTATION_NORMAL -> bitmap
                else -> bitmap
            }
            return rotatedBitmap
        }

        fun rotateImage(source: Bitmap, angle: Int): Bitmap? {
            val matrix = Matrix()
            matrix.postRotate(angle.toFloat())
            return Bitmap.createBitmap(
                source, 0, 0, source.width, source.height,
                matrix, true
            )
        }

        fun buildFileProviderUri(context: Context, uri: Uri): Uri? {
            return FileProvider.getUriForFile(
                context,
                "siva.app.quotemaker.fileprovider",
                File(uri.path!!)
            )
        }

        fun buildFileProviderUri(context: Context, file: File): Uri? {
            return FileProvider.getUriForFile(
                context,
                "siva.app.quotemaker.fileprovider",
                file
            )
        }

        fun showSnackbar(activity: Activity, message: String) {
            val view = activity.findViewById<View>(android.R.id.content)
            if (view != null) {
                Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
            } else {
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
            }
        }

        fun setFullScreen(activity: Activity) {
            activity.requestWindowFeature(Window.FEATURE_NO_TITLE)
            activity.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }

        fun hideKeyboard(activity: Activity, view: View) {
            val inputMethodManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }

        fun showKeyboard(activity: Activity, editText: EditText) {
            editText.requestFocus()
            val inputMethodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            inputMethodManager!!.toggleSoftInputFromWindow(editText.applicationWindowToken, InputMethodManager.SHOW_FORCED, 0)
        }
    }
}