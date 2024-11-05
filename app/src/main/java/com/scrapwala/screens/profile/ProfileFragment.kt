package com.scrapwala.screens.profile

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.google.gson.Gson
import com.scrapwala.MainActivity
import com.scrapwala.R
import com.scrapwala.databinding.FragmentProfileBinding
import com.scrapwala.redirectionhandler.navigateToEditProfileActivity
import com.scrapwala.redirectionhandler.navigateToLoginActivity
import com.scrapwala.redirectionhandler.navigateToSelectAddress
import com.scrapwala.screens.login.model.SendOtpRequest
import com.scrapwala.screens.login.model.VerifyOtpResponse
import com.scrapwala.screens.pickups.model.SuccessResponse
import com.scrapwala.screens.profile.model.UpdateProfileResponse
import com.scrapwala.screens.profile.viewmodel.ProfileViewModel
import com.scrapwala.utils.Constant
import com.scrapwala.utils.ErrorResponse
import com.scrapwala.utils.Preferences
import com.scrapwala.utils.access_media.ChooseMediaActivity
import com.scrapwala.utils.extensionclass.hideSpinner
import com.scrapwala.utils.extensionclass.showCustomToast
import com.scrapwala.utils.extensionclass.showSpinner
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.util.Locale

@AndroidEntryPoint
class ProfileFragment : Fragment() {


    private var userDataObj: VerifyOtpResponse.Data? = null
    private lateinit var binding: FragmentProfileBinding

    private val viewModel: ProfileViewModel by viewModels()
    private var pref: VerifyOtpResponse.Data? = null
    private var token: String = ""
    private var compressedFile: String? = null


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
        observeSaveUserResponse()
    }

    override fun onResume() {
        super.onResume()
        pref = Preferences.getUserDataObj(requireContext())
        token = Preferences.getUserToken(requireContext())
        setUserData()
    }

    private fun observeSaveUserResponse() {
        viewModel.saveUserResponse.observe(requireActivity(), Observer {
            when (it) {
                is VerifyOtpResponse -> {
                    hideSpinner()
                    if(it.data!=null){
                        Preferences.setUserData(requireContext(), Gson().toJson(it.data))
                    }
                        renderProfilePic("" + compressedFile)


                }

                is ErrorResponse -> {
                    hideSpinner()
                    if (it.message.isNullOrEmpty().not()) {
                        showCustomToast(binding.root,requireActivity(),it.message)
                    }

                }

                is String -> {
                    hideSpinner()
                    showCustomToast(binding.root,requireActivity(),it)
                }
            }

        })
    }

    private fun initView() {

        userDataObj= Preferences.getUserDataObj(requireContext())



        binding.toolbar.tvHeading.text = getString(R.string.your_profile)
        binding.toolbar.imgBack.setOnClickListener{
            requireActivity().onBackPressed()
        }


        binding.relSavedAddress.setOnClickListener {

            navigateToSelectAddress(requireActivity(),null)
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

            if(userDataObj!=null){
                var request=SendOtpRequest()
                request.mobile=userDataObj?.mobile

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

    private fun setUserData() {
        if(userDataObj!=null){

            if (userDataObj?.image.toString().isNullOrEmpty().not()) {
                val requestOptions = RequestOptions()
                val listenerImage: RequestListener<Drawable?> = object :
                    RequestListener<Drawable?> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable?>,
                        isFirstResource: Boolean
                    ): Boolean {
                        if (userDataObj?.name.toString().isNullOrEmpty().not()) {
                            binding.imgProfile.visibility = View.GONE
                            binding.txtImgProfile.visibility = View.VISIBLE
                            val firstLetter = userDataObj?.name?.firstOrNull()?.toUpperCase() ?: ""
                            binding.txtImgProfile.text = firstLetter.toString()
                        }
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable?>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        // onResourceReady implementation
                        return false
                    }
                }
                binding.imgProfile.visibility = View.VISIBLE
                binding.txtImgProfile.visibility = View.GONE
                Glide.with(this).load("https://treestructure.onrender.com/image/"+userDataObj?.image).listener(listenerImage).apply(requestOptions)
                    .fitCenter().into(binding.imgProfile)
            } else {
                if (userDataObj?.name.toString().isNullOrEmpty().not()) {
                    binding.imgProfile.visibility = View.GONE
                    binding.txtImgProfile.visibility = View.VISIBLE
                    val firstLetter = userDataObj?.name?.firstOrNull()?.toUpperCase() ?: ""
                    binding.txtImgProfile.text = firstLetter.toString()
                }
            }



            if(userDataObj?.name.isNullOrEmpty().not()){
                binding.txtUserName.setText(userDataObj?.name)
            }
            else{
                binding.txtUserName.setText("")

            }


            if(userDataObj?.rewardPoint.isNullOrEmpty().not()){
                binding.txtRewardPoint.setText(userDataObj?.rewardPoint)
            }
            else{
                binding.txtRewardPoint.setText("")

            }
        }
    }


    private fun observeLogoutResponse() {
        viewModel.logOutResponse.observe(requireActivity(), Observer {
            when (it) {
                is SuccessResponse -> {

                    val settingsSqyrdPref = context?.getSharedPreferences(Constant.PREFERENCE_NAME, 0)
                    //val settingsFilterPref = context.getSharedPreferences(Constant.FILTERS_NAME, 0)
                    var editor = settingsSqyrdPref?.edit()
                    editor?.clear()
                    editor?.apply()


                    navigateToLoginActivity(requireActivity(),null)

                    hideSpinner()


                }

                is ErrorResponse -> {
                    if (it.message.isNullOrEmpty().not()) {
                        showCustomToast(binding.root,requireActivity(),it.message)
                    }
                    hideSpinner()
                }

                is String -> {
                    showCustomToast(binding.root,requireActivity(),it)
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
                        compressedFile = (it)
                        uploadProfilePic(File(it))
                      //  renderProfilePic(it)
                    }
                }
            }
        }



    private fun uploadProfilePic(compressedFile: File?) {
        print("file path: $compressedFile")
        var imageUri = Uri.fromFile(compressedFile)
        var imageMimeType = MimeTypeMap.getSingleton()
            .getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(imageUri.toString()))!!
        if (imageMimeType.isEmpty()) {
            imageMimeType = "image/jpeg"
        }

        val id: RequestBody = pref?.id!!.toString().toRequestBody("text/plain".toMediaType())
        val name: RequestBody = pref?.name!!.toRequestBody("text/plain".toMediaType())
        val gender: RequestBody = pref?.gender!!.toRequestBody("text/plain".toMediaType())
        val email: RequestBody = pref?.email!!.toRequestBody("text/plain".toMediaType())
        val mobile: RequestBody = pref?.mobile!!.toRequestBody("text/plain".toMediaType())
        val city: RequestBody = pref?.city!!.toRequestBody("text/plain".toMediaType())

        val coverPicBody = compressedFile?.asRequestBody(imageMimeType.toMediaType())!!



//        val imageFile = File(this.compressedFile)
//        val requestFile = RequestBody.create("image/jpeg".toMediaTypeOrNull(), imageFile)
//        val imagePart = MultipartBody.Part.createFormData("image", imageFile.name, requestFile)



        var hashMap: HashMap<String, RequestBody> = hashMapOf()
        hashMap.put("id", id)
        hashMap.put("name", name)
        hashMap.put("gender", gender)
        hashMap.put("email", email)
        hashMap.put("mobile", mobile)
        hashMap.put("city", city)
        hashMap.put(
            String.format(Locale.getDefault(), "image\"; filename=\"%s", compressedFile?.name),
            coverPicBody
        )

        showSpinner(context)
        viewModel.saveUserRequest("Bearer $token", hashMap)

//        showSpinner(context)
//        viewModel.saveUserRequest("Bearer $token", id, name, gender, email, mobile, city, imagePart)
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