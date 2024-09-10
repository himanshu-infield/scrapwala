package com.scrapwala.utils.access_media

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import com.anilokcun.uwmediapicker.UwMediaPicker
import com.bumptech.glide.Glide
import com.scrapwala.R
import com.scrapwala.databinding.ActivityChooseMediaBinding
import com.scrapwala.utils.PermissionUtils
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream


class ChooseMediaActivity : AppCompatActivity(), VideoCompressor.Callback {
    // This enum used when user choose that which media option (i.e. PHOTO or VIDEO) is accessing and from where it is accessing i.e. either from gallery or from camera.
    internal enum class MediaEnum {
        PHOTO, VIDEO
    }

    private lateinit var binding: ActivityChooseMediaBinding
    private var progress: ProgressDialog? = null

    // This parameter tells about what media type is need to open whether it is PHOTO or VIDEO
    private var mediaType: MediaEnum? = null

    // This parameter tells about if mediaType = PHOTO/VIDEO then what option we are choosing 'From Camera' or 'From Gallery'
    //private var selectedMediaOption: MediaEnum? = null

    // This parameter is about that, do we need to visible CAMERA option for PHOTO or VIDEO or just let user to pick from gallery only (currently not using)
    //private var accessFromCamera: Boolean = false

    // Below parameters are the default value of maximum nos. of media files we can select for image/video
    private var maxImageSelection: Int = 5 // default value is 5
    private var maxVideoSelection: Int = 1 // default value is 1

    // Below variable collects the paths of selected media
    private var paths = arrayListOf<String>()

    // This parameter is used to save the captured image/video path.
    private lateinit var currentMediaPath: String

    // These parameters will use if we want edit the photo
    private var isEditPhoto = false
    private var position = -1

    // Below parameter will tell that photo/video is either captured from camera or from gallery, default is assuming from gallery.
    private var isFromCamera = false

    companion object {
        //====================================
        const val MAX_VIDEO_TIME: Int = 120 // in seconds
        const val FILE_SIZE_OF_UNCOMPRESSED_VIDEO_IN_MB = 30
        const val FILE_SIZE_OF_UNCOMPRESSED_IMAGE_IN_MB = 4
        const val IMAGE_SIZE: Int = 4 * 1024 * 1024 // 4 MB

        //====================================
        const val RESULT_VIDEO: Int = 100
        const val RESULT_PHOTO: Int = 101
        const val RESULT_CANCELLED: Int = 102
        const val RESULT_URI_NOT_FOUND: Int = 103
        const val REQUEST_IMAGE_CAPTURE = 1
        const val REQUEST_VIDEO_CAPTURE = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_choose_media)
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
        getExtras()
        initComponents()

        // Ask for permissions
        PermissionUtils.hasPermissionGranted(
            this,
            *PermissionUtils.getReadWritePermissionAccordingToSdk()
        )
    }

    /**
     * Get extra parameter
     */
    private fun getExtras() {
        val intent = this.intent ?: return
        val bundle = intent.extras ?: return
        mediaType = getIntent().getSerializableExtra("mediaType") as MediaEnum?
        //accessFromCamera = intent.getBooleanExtra("cameraAccess", true)
        maxImageSelection = intent.getIntExtra("maxImageSelection", 5)
        maxVideoSelection = intent.getIntExtra("maxVideoSelection", 1)

        // For Photo Edit
        isEditPhoto = intent.getBooleanExtra("isEditPhoto", false)
        position = intent.getIntExtra("position", -1)
    }

    /**
     * Initialize view components
     */
    private fun initComponents() {
        handleMediaOptions()

        binding.imgTransparent.setOnClickListener {
            finish()
        }
    }

    /**
     * This method handles which media types will have to show and handles its listeners for getting media from gallery or by camera.
     */
    private fun handleMediaOptions() {
        if (mediaType == MediaEnum.PHOTO) {
            binding.title.text = "Upload Image"
            Glide.with(this).load(R.drawable.icon_camera).into(binding.uploadImageCamera)
            Glide.with(this).load(R.drawable.icon_image).into(binding.uploadImageGallery)
            binding.uploadImageCameraText.text = "Picture from Camera"
            binding.uploadImageGalleryText.text = "Picture from Gallery"

            binding.selectFromCamera.setOnClickListener {
                isFromCamera = true
                captureMediaIntent()
            }

            binding.selectFromGallery.setOnClickListener {
                isFromCamera = false
                chooseImageFromGallery()
            }
        } else if (mediaType == MediaEnum.VIDEO) {
            binding.selectFromCamera.setOnClickListener {
                isFromCamera = true
                captureMediaIntent()
            }

            binding.selectFromGallery.setOnClickListener {
                isFromCamera = false
                chooseVideoFromGallery()
            }
        }
    }

    /**
     * Choose image from gallery
     */
    private fun chooseImageFromGallery() {
        try {
            UwMediaPicker
                .with(this)                        // Activity or Fragment
                .setGalleryMode(UwMediaPicker.GalleryMode.ImageGallery) // GalleryMode: ImageGallery/VideoGallery/ImageAndVideoGallery, default is ImageGallery
                .setGridColumnCount(3)                                  // Grid column count, default is 3
                .setMaxSelectableMediaCount(maxImageSelection)          // Maximum selectable media count, default is null which means infinite
                .setLightStatusBar(true)                                // Is light status bar enable, default is true
                .enableImageCompression(true)       // Is image compression enable, default is false
                .setCompressionMaxWidth(1280F)                // Compressed image's max width px, default is 1280
                .setCompressionMaxHeight(720F)                // Compressed image's max height px, default is 720
                .setCompressFormat(Bitmap.CompressFormat.JPEG)        // Compressed image's format, default is JPEG
                .setCompressionQuality(85)                // Image compression quality, default is 85
                //.setCompressedFileDestinationPath(destinationPath)	// Compressed image file's destination path, default is "${application.getExternalFilesDir(null).path}/Pictures"
                .setCancelCallback { }                    // Will be called when user cancels media selection
                .launch { selectedMediaList ->
                    selectedMediaList?.forEach {
                        paths.add(it.mediaPath)
                    }

                    if (paths.size == 0) {
                        handleResult(RESULT_URI_NOT_FOUND, true)
                    } else {
                        handleResult(RESULT_PHOTO, false)
                    }
                }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun chooseVideoFromGallery() {
        UwMediaPicker
            .with(this)                        // Activity or Fragment
            .setGalleryMode(UwMediaPicker.GalleryMode.VideoGallery) // GalleryMode: ImageGallery/VideoGallery/ImageAndVideoGallery, default is ImageGallery
            .setGridColumnCount(3)                                  // Grid column count, default is 3
            .setMaxSelectableMediaCount(maxVideoSelection)          // Maximum selectable media count, default is null which means infinite
            .setLightStatusBar(true)                                // Is llight status bar enable, default is true
            .enableImageCompression(false)                // Is image compression enable, default is false
            //.setCompressionMaxWidth(1280F)				// Compressed image's max width px, default is 1280
            //.setCompressionMaxHeight(720F)				// Compressed image's max height px, default is 720
            //.setCompressFormat(Bitmap.CompressFormat.JPEG)		// Compressed image's format, default is JPEG
            //.setCompressionQuality(85)                // Image compression quality, default is 85
            //.setCompressedFileDestinationPath(destinationPath)	// Compressed image file's destination path, default is "${application.getExternalFilesDir(null).path}/Pictures"
            .setCancelCallback {                                    // Will be called when user cancels media selection
                finish()
            }
            .launch { selectedMediaList ->
                /*selectedMediaList?.forEach {
                    paths.add(it.mediaPath)
                }*/

                if (selectedMediaList.isNullOrEmpty()) {
                    handleResult(RESULT_URI_NOT_FOUND, true)
                } else {
                    videoSizeAndCompressionHandling(selectedMediaList[0].mediaPath)
                }
            }
    }


    /**
     * This method is used to capture IMAGE/VIDEO (from camera) based on selectedMediaOption.
     */
    private fun captureMediaIntent() {

        if (mediaType == MediaEnum.PHOTO) {

            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            // Create the File where the photo should go
            try {
                val file = createMediaFile(
                    Environment.DIRECTORY_PICTURES,
                    "JPEG_TempFile",
                    ".jpg"
                ) //createImageFile()
                currentMediaPath = file.absolutePath

                // Continue only if the File was successfully created
                if (file != null) {
                    val uri = FileProvider.getUriForFile(
                        this,
                        "$packageName.provider",
                        file!!
                    )
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
                    startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
                }
            } catch (ex: Exception) {
                // Error occurred while creating the File
                //displayMessage(baseContext, ex.message.toString())
            }
        } else {

            val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
            // Create the File where the photo should go
            try {
                val file = createMediaFile(
                    Environment.DIRECTORY_MOVIES,
                    "MP4_TempFile",
                    ".mp4"
                ) //createImageFile()
                currentMediaPath = file.absolutePath

                // Continue only if the File was successfully created
                if (file != null) {
                    val uri = FileProvider.getUriForFile(
                        this,
                        "$packageName.provider",
                        file!!
                    )
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
                    intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, MAX_VIDEO_TIME)
                    startActivityForResult(intent, REQUEST_VIDEO_CAPTURE)
                }
            } catch (ex: Exception) {
                // Error occurred while creating the File
                //displayMessage(baseContext, ex.message.toString())
            }
        }
    }

    /**
     * This method returns the media file containing the path where it saves in local folder.
     */
    private fun createMediaFile(
        directory: String,
        mediaFileName: String,
        mediaFileExt: String
    ): File {
        // Create an image file name
        //val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        //val imageFileName = "JPEG_" + timeStamp + "_"
        //val imageFileName = "JPEG_TempFile_"
        val storageDir = getExternalFilesDir(directory)

        return File(storageDir, mediaFileName + mediaFileExt)

        /*return File.createTempFile(
            mediaFileName, *//* prefix *//*
            mediaFileExt, *//* suffix *//*
            storageDir      *//* directory *//*
        )*/
    }

    /**
     *  Activity results for getting photo/video from camera.
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            // Use the saved photo path to get the URI
            val imageUri: Uri? = Uri.fromFile(File(currentMediaPath))

            if (imageUri != null) {
                val path = getFilePathFromUri(this, imageUri)
                if (path != null) {
                    val fileSize = getFileSizeInMB(applicationContext, path)

                    if (fileSize > FILE_SIZE_OF_UNCOMPRESSED_IMAGE_IN_MB) {
                        compressImage(path, path)
                    }

                    paths.add(path)
                }

                handleResult(RESULT_PHOTO, false)
            } else {
                handleResult(RESULT_URI_NOT_FOUND, true)
            }
        } else if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            val videoUri: Uri? = Uri.fromFile(File(currentMediaPath))

            if (videoUri != null) {
                val path = getFilePathFromUri(this, videoUri)

                videoSizeAndCompressionHandling(path)
                /*if (path != null) {
                    paths.add(path)
                }
                handleResult(RESULT_VIDEO, false)*/
            } else {
                handleResult(RESULT_URI_NOT_FOUND, true)
            }
        }
    }

    /**
     * If file size is more than FILE_SIZE_OF_UNCOMPRESSED_VIDEO_IN_MB then only compress the file otherwise send as it is.
     */
    private fun videoSizeAndCompressionHandling(path: String?) {
        if (path != null) {
            if (!videoExceedsMaxDuration(path)) {
                val fileSize = getFileSizeInMB(applicationContext, path)

                if (fileSize > FILE_SIZE_OF_UNCOMPRESSED_VIDEO_IN_MB) {
                    val quality = VideoCompressor.instance?.getVideoQuality(fileSize)
                    VideoCompressor.instance?.compressVideo(
                        path.toUri(),
                        applicationContext,
                        this,
                        quality!!
                    )
                } else {
                    paths.add(path)
                    handleResult(RESULT_VIDEO, false)
                }
            }
        } else {
            handleResult(RESULT_URI_NOT_FOUND, true)
        }
    }

    /**
     * Set result back to activity. If no error then send the paths arraylist back to the activity.
     */
    private fun handleResult(resultCode: Int, isError: Boolean) {
        if (isError) {
            setResult(resultCode, null)
        } else {
            val intent = Intent()
            intent.putStringArrayListExtra("paths", paths)
            intent.putExtra("isEditPhoto", isEditPhoto)
            intent.putExtra("position", position)
            intent.putExtra("isFromCamera", isFromCamera)
            setResult(resultCode, intent)
        }

        finish()
    }

    /**
     * Compress the captured image.
     */
    private fun compressImage(inputImagePath: String, outputImagePath: String) {
        try {
            val file = File(inputImagePath)
            if (!file.exists()) {
                // Handle the case where the input image file does not exist
                return
            }

            // Load the original image into a Bitmap
            val originalBitmap = BitmapFactory.decodeFile(inputImagePath)

            // Create a quality variable to control compression quality
            var quality = 100

            val outputStream = ByteArrayOutputStream()
            originalBitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)

            // Iterate to find the best quality to achieve the desired file size
            while (outputStream.size() > IMAGE_SIZE && quality > 0) {
                outputStream.reset() // Clear the output stream

                // Decrease the quality by 10% and try again
                quality -= 10
                originalBitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
            }

            // Save the compressed image to the output file
            FileOutputStream(outputImagePath).use { it.write(outputStream.toByteArray()) }
        } catch (e: Exception){
            e.printStackTrace()
        }
    }

    // ==============================================
    // VIDEO OPERATIONS STARTED
    // ==============================================

    private fun getVideoDuration(context: Context, path: String): Long {
        val retriever = MediaMetadataRetriever()
        //use one of overloaded setDataSource() functions to set your data source
        retriever.setDataSource(context, path.toUri())
        val time =
            retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        val timeInMillis = time?.toLongOrNull() ?: 0
        retriever.release()

        return timeInMillis
    }

    /**
     * Get size of file in MBs
     */
    private fun getFileSizeInMB(context: Context, videoPath: String?): Int {
        var fileSizeInMB = 0
        try {
            videoPath?.let {
                val file = File(it)
                // Get length of file in bytes
                val fileSizeInBytes: Long = file.length()
                // Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
                val fileSizeInKB = fileSizeInBytes / 1024
                // Convert the KB to MegaBytes (1 MB = 1024 KBytes)
                fileSizeInMB = (fileSizeInKB / 1024).toInt()
            }
        } catch (e: Exception) {
        }
        return fileSizeInMB // Return 0 if file size could not be determined
    }

    private fun videoExceedsMaxDuration(path: String): Boolean {
        val durationInMillis = getVideoDuration(applicationContext, path)

        if (durationInMillis > (MAX_VIDEO_TIME * 1000)) {
            Toast.makeText(
                this,
                "Video can\'t be longer than 2 minutes.",
                Toast.LENGTH_LONG
            ).show()

            return true
        }
        return false
    }

    override fun onVideoCompression(
        isCompressionCompleted: Boolean,
        info: String,
        percent: Float,
        file: File?,
        path: String?
    ) {
        runOnUiThread {
            handleMessageText(info, resources.getColor(R.color.white), false)
        }

        if (isCompressionCompleted) {
            if (path != null) {
                handleMessageText(info, resources.getColor(R.color.green), true)
                paths.add(path)

                // Return after 2 seconds
                Handler().postDelayed(Runnable {
                    handleResult(RESULT_VIDEO, false)
                }, 2000)
            } else {
                handleResult(RESULT_URI_NOT_FOUND, true)
            }
        }
    }

    // ==============================================
    // VIDEO OPERATIONS ENDED
    // ==============================================

    private fun handleMessageText(
        info: String,
        color: Int,
        isCompressionCompleted: Boolean
    ) {
        //txt_message.visibility = visibleState
        binding.txtCompressionMessage.text = info
        binding.txtCompressionMessage.setTextColor(color)

        if (!isCompressionCompleted && !binding.txtCompressionMessage.isShown) {
            binding.txtCompressionMessage.visibility = View.VISIBLE
        }

        /*if (isCompressionCompleted) {
            // After 2 seconds, text view will be blank
            Handler().postDelayed(Runnable {
                binding.txtCompressionMessage.text = ""
            }, 2000)
        }*/
    }

    private fun getFilePathFromUri(context: Context, contentURI: Uri): String? {
        val result: String?
        val cursor = contentResolver.query(contentURI, null, null, null, null)
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.path
        } else {
            cursor.moveToFirst()
            val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            result = cursor.getString(idx)
            cursor.close()
        }
        return result
    }

    private fun showProgressBar() {
        progress = ProgressDialog(this)
        //progress!!.setTitle("Please Wait...")
        progress!!.setMessage("Please Wait...")
        progress!!.setCancelable(false)
        progress!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        //progress!!.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        progress?.dismiss()
    }
}