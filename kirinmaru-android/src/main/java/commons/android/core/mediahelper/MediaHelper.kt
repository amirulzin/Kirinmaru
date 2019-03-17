package commons.android.core.mediahelper

import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v4.content.FileProvider
import android.util.Log
import commons.android.BuildConfigAlias
import commons.android.ProjectConstants.DEBUG_TAG
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

const val REQ_EXT_IMAGE_CAPTURE = 501
const val REQ_EXT_IMAGE_PICK = 502
const val REQ_EXT_FILE_PICK = 503

data class PendingFile(val uri: Uri?, val file: File?)

fun Fragment.pickImage() {
  val out = Intent(Intent.ACTION_GET_CONTENT).apply {
    type = "image/*"
    putExtra(Intent.EXTRA_LOCAL_ONLY, true)
    addCategory(Intent.CATEGORY_OPENABLE)
  }
  startActivityForResult(Intent.createChooser(out, "Select Image"), REQ_EXT_IMAGE_PICK)
}

inline fun Fragment.takePicture(crossinline beforeLaunch: (PendingFile) -> Unit) {
  context?.let { ctx ->
    val imageDateFormatter = SimpleDateFormat("yyyy-MM-dd_HHmm_a", Locale.getDefault())
    val timeStamp = imageDateFormatter.format(Date())
    val file = File.createTempFile(timeStamp + "_", ".jpg", ctx.cacheDir)
    val photoURI = FileProvider.getUriForFile(ctx, BuildConfigAlias.APPLICATION_ID.plus(".provider"), file)
    Log.d(DEBUG_TAG, "OutUri: ${file.absolutePath}")
    val out = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
      putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
    }
    beforeLaunch.invoke(PendingFile(photoURI, file))
    startActivityForResult(Intent.createChooser(out, "Select camera app"), REQ_EXT_IMAGE_CAPTURE)
  }
}

fun Fragment.showFilePicker() {
  val out = Intent(Intent.ACTION_GET_CONTENT).apply {
    putExtra(Intent.EXTRA_LOCAL_ONLY, true)
    type = "*/*"
    addCategory(Intent.CATEGORY_OPENABLE)
  }
  startActivityForResult(Intent.createChooser(out, "Select File"), REQ_EXT_FILE_PICK)
}