package com.scrapwala.screens.profile

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.scrapwala.R
import com.scrapwala.databinding.ActivityEditProfileBinding
import com.scrapwala.utils.access_media.ChooseMediaActivity
import java.io.File

class EditProfileActivity : AppCompatActivity() {

    lateinit var binding:ActivityEditProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        binding.toolbar.tvHeading.text = getString(R.string.profile_update)
        binding.toolbar.imgBack.setOnClickListener{
            finish()
        }

        binding.rlEditProfile.setOnClickListener {
            val intent = Intent(this, ChooseMediaActivity::class.java)
            intent.putExtra("mediaType", ChooseMediaActivity.MediaEnum.PHOTO)
            intent.putExtra("maxImageSelection", 1)
            takePictureLauncher?.launch(intent)
        }
    }

    private val takePictureLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            when (result.resultCode) {
                ChooseMediaActivity.RESULT_PHOTO -> {
                    val data: Intent? = result.data
                    val arr = data?.getStringArrayListExtra("paths")
                    arr?.forEach {
                        val file = File(it)
                        println("filepath $file")
                        // uploadProfilePic(File(it))
                        renderProfilePic(it)
                    }
                }
            }
        }


    private fun renderProfilePic(path: String) {
        binding.imgProfile.visibility = View.VISIBLE
        binding.txtImgProfile.visibility = View.GONE

        if (!TextUtils.isEmpty(path)) {
            val circularProgressDrawable = CircularProgressDrawable(this).apply {
                // Set the color of the progress bar to white
                setColorSchemeColors(R.color.white)
                setColorFilter(
                    ContextCompat.getColor(this@EditProfileActivity, R.color.white),
                    PorterDuff.Mode.SRC_IN
                );
                strokeWidth = 5f // Set the stroke width as needed
                centerRadius = 30f // Set the radius as needed
                start()
            }


            Glide.with(this)
                .asBitmap()
                .load(path)
                .centerCrop()
                .placeholder(circularProgressDrawable)
                .listener(object : RequestListener<Bitmap?> {

                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Bitmap?>,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.imgProfile.setImageResource(R.drawable.menu_user)
                        binding.imgProfile.setColorFilter(
                            ContextCompat.getColor(
                                this@EditProfileActivity,
                                R.color.white
                            ), PorterDuff.Mode.SRC_IN
                        )
                        return false
                    }

                    override fun onResourceReady(
                        resource: Bitmap,
                        model: Any,
                        target: Target<Bitmap?>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }
                })
                .error(R.drawable.menu_user)
                .into(binding.imgProfile)
        }
    }
}