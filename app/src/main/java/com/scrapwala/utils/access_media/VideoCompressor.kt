package com.scrapwala.utils.access_media

import android.content.Context
import android.net.Uri
import android.os.Environment
import com.abedelazizshe.lightcompressorlibrary.CompressionListener
import com.abedelazizshe.lightcompressorlibrary.VideoQuality
import java.io.File

class VideoCompressor {

    interface Callback {
        fun onVideoCompression(isCompressionCompleted: Boolean, info: String, percent: Float, file: File?, path: String?)
    }

    companion object {
        private var mInstance: VideoCompressor? = null
        val instance: VideoCompressor?
            get() {
                if (mInstance == null) {
                    mInstance = VideoCompressor()
                }
                return mInstance
            }
    }

    fun compressVideo(uri: Uri?, context: Context, callback: Callback, quality: VideoQuality) {
        val dir = context.getExternalFilesDir(Environment.DIRECTORY_MOVIES)
        if (!dir!!.exists()) {
            try {
                dir.mkdirs()
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }

        // Remove previous files
        /*if (dir.isDirectory) {
            val children = dir.list()
            for (i in children.indices) {
                File(dir, children[i]).delete()
            }
        }*/

        //val fileName = "Video_" + System.currentTimeMillis() + ".mp4"
        val fileName = "Video_Compressed.mp4"
        val file = File(dir, fileName)

        uri?.let {
            com.abedelazizshe.lightcompressorlibrary.VideoCompressor.start(
                uri.path!!, // => This is required if srcUri is provided. If not, it can be ignored or null.
                file.path,
                object : CompressionListener {
                    override fun onProgress(percent: Float) {
                        // Update UI with progress value
                        callback.onVideoCompression(false, "Compressing... ${percent.toLong()}%", percent, null, null)
                    }

                    override fun onStart() {
                        // Compression start
                        callback.onVideoCompression(false, "Compression Starts...", 0.0f, null, null)
                    }

                    override fun onSuccess() {
                        // On Compression success
                        //videoUri = Uri.fromFile(file)
                        callback.onVideoCompression(true, "Compression Done!", 100.0f, file, file.path)
                    }

                    override fun onFailure(failureMessage: String) {
                        // On Failure
                        callback.onVideoCompression(false, "Failure $failureMessage", 0.0f, null, null)
                    }

                    override fun onCancelled() {
                        // On Cancelled
                        callback.onVideoCompression(false, "Cancelled", 0.0f, null, null)
                    }

                },
                quality,
                false, // isMinBitrateCheckEnabled
                keepOriginalResolution = false /* videoBitrate -> Int, ignore, or null*/
            )
        }
    }

    fun getVideoQuality(fileSizeInMb:Int): VideoQuality {
        return if(fileSizeInMb == 0) {
            // If fileSizeInMb is 0, it means either there is an exception to finding out the file size so in that case we set Low quality
            return VideoQuality.LOW
        } else if(fileSizeInMb < 30) {
            VideoQuality.VERY_HIGH
        } else if(fileSizeInMb < 50) {
            VideoQuality.MEDIUM
        } else {
            VideoQuality.LOW
        }
    }
}