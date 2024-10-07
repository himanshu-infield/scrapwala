package com.scrapwala.screens.profile

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.scrapwala.MainActivity
import com.scrapwala.R
import com.scrapwala.databinding.FragmentProfileBinding
import com.scrapwala.databinding.FragmentReferEarnBinding
import com.scrapwala.redirectionhandler.navigateToEditProfileActivity
import com.scrapwala.redirectionhandler.navigateToLoginActivity
import com.scrapwala.screens.login.model.LoginViewModel
import com.scrapwala.screens.login.model.SendOtpRequest
import com.scrapwala.screens.pickups.model.SuccessResponse
import com.scrapwala.screens.profile.model.ProfileViewModel
import com.scrapwala.utils.ErrorResponse
import com.scrapwala.utils.Preferences
import com.scrapwala.utils.access_media.ChooseMediaActivity
import com.scrapwala.utils.extensionclass.hideSpinner
import com.scrapwala.utils.extensionclass.showCustomToast
import com.scrapwala.utils.extensionclass.showSpinner
import java.io.File

class ProfileFragment : Fragment() {


    private lateinit var binding: FragmentProfileBinding

    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

        observeLogoutResponse()
    }

    private fun initView() {
        binding.toolbar.tvHeading.text = getString(R.string.your_profile)
        binding.toolbar.imgBack.setOnClickListener{
            requireActivity().onBackPressed()
        }

        binding.rlYourPickUp.setOnClickListener{
            (activity as MainActivity).bindingBase.navView.menu.getItem(2).isChecked = true
            (activity as MainActivity).bindingBase.bottomFragframeLayout.setCurrentItem(2, false)
        }
        binding.rlReferFriend.setOnClickListener{
            (activity as MainActivity).bindingBase.navView.menu.getItem(1).isChecked = true
            (activity as MainActivity).bindingBase.bottomFragframeLayout.setCurrentItem(1, false)
        }


        binding.relLogout.setOnClickListener {



            var userDataObj= Preferences.getUserDataObj(requireContext())


            if(userDataObj!=null){
                var request=SendOtpRequest()
                request.mobile=userDataObj.mobile

                showSpinner(context)
                viewModel.logoutRequest(request)
            }

        }

        binding.llEditProfile.setOnClickListener {
            navigateToEditProfileActivity(requireActivity(),null)
        }


        binding.cameraIcon.setOnClickListener {
            val intent = Intent(requireActivity(), ChooseMediaActivity::class.java)
            intent.putExtra("mediaType", ChooseMediaActivity.MediaEnum.PHOTO)
            intent.putExtra("maxImageSelection", 1)
            takePictureLauncher?.launch(intent)
        }
    }





    private fun observeLogoutResponse() {
        viewModel.logOutResponse.observe(this as AppCompatActivity, Observer {
            when (it) {
                is SuccessResponse -> {

                    navigateToLoginActivity(this,null)

                    hideSpinner()


                }

                is ErrorResponse -> {
                    if (it.message.isNullOrEmpty().not()) {
                        showCustomToast(findViewById(android.R.id.content),this,it.message)
                    }
                    hideSpinner()
                }

                is String -> {
                    showCustomToast(findViewById(android.R.id.content),this,it)
                    hideSpinner()
                }
            }

        })
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
            val circularProgressDrawable = CircularProgressDrawable(requireActivity()).apply {
                // Set the color of the progress bar to white
                setColorSchemeColors(R.color.white)
                setColorFilter(
                    ContextCompat.getColor(requireContext(), R.color.white),
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
                                requireContext(),
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