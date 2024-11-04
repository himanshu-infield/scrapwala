package com.scrapwala.screens.profile

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.scrapwala.R
import com.scrapwala.databinding.ActivityEditProfileBinding
import com.scrapwala.databinding.DialogGenericsearchBinding
import com.scrapwala.screens.login.model.VerifyOtpResponse
import com.scrapwala.screens.pickups.adapter.CityListAdapter
import com.scrapwala.screens.pickups.adapter.ClickedCityItemCallback
import com.scrapwala.screens.pickups.model.CityListResponse
import com.scrapwala.screens.pickups.viewmodel.PickupViewModel
import com.scrapwala.utils.ErrorResponse
import com.scrapwala.utils.Preferences
import com.scrapwala.utils.access_media.ChooseMediaActivity
import com.scrapwala.utils.extensionclass.hideKeyboard
import com.scrapwala.utils.extensionclass.setupFullHeight
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class EditProfileActivity : AppCompatActivity() {

    lateinit var binding: ActivityEditProfileBinding
    private var pref: VerifyOtpResponse.Data? = null
    private var token: String = ""
    private val viewModelCity: PickupViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        pref = Preferences.getUserDataObj(this)
        token = Preferences.getUserToken(this)
        initView()
        setUserData()
        setCityPhone()
        observeCityListResponse()
        callCityApi()

        disableAllInputField()
    }


    private fun setCityPhone() {
        binding.edtCity.setOnTouchListener(View.OnTouchListener { view, motionEvent ->
            when (motionEvent.action) {

                MotionEvent.ACTION_DOWN -> {
                    showCityDialog()
                    hideKeyboard(this@EditProfileActivity)
                }

            }
            return@OnTouchListener true
        })
    }

    private fun showCityDialog() {

        if (viewModelCity.cityList.isNullOrEmpty().not()) {
            viewModelCity.cityList

            val mAlertDialog = BottomSheetDialog(this)

            val dialog_Binding =
                DataBindingUtil.inflate<DialogGenericsearchBinding>(
                    LayoutInflater.from(this), R.layout.dialog_genericsearch, null, false
                )

            dialog_Binding.toolbar.toolbarTitleGeneric.setText("City List")
            dialog_Binding.searchView.queryHint = "Enter City Name"

            // Clear focus from EditText
            dialog_Binding.searchView.clearFocus()
            dialog_Binding.searchView.requestFocus()
            dialog_Binding.searchView.hideKeyboard()


            mAlertDialog.setContentView(dialog_Binding.root)
            mAlertDialog.setCanceledOnTouchOutside(false)
            setupFullHeight(this, mAlertDialog)


            dialog_Binding.toolbar.dialogBack.setOnClickListener {

                mAlertDialog.dismiss()

            }

            var adapter = CityListAdapter(this, viewModelCity.cityList,
                object : ClickedCityItemCallback {
                    override fun clickedItem(
                        position: Int,
                        item: Any
                    ) {

                        viewModelCity.selectedCityItem = (item as CityListResponse.Data)
                        binding.edtCity.setText((item as CityListResponse.Data).name)

                        mAlertDialog.dismiss()


                    }
                })

            var layoutManager = LinearLayoutManager(this)



            dialog_Binding.recyclerView?.layoutManager = layoutManager
            dialog_Binding.recyclerView?.adapter = adapter
            dialog_Binding.searchView.setOnQueryTextListener(object :
                SearchView.OnQueryTextListener,
                androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false;
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    adapter.filterList(
                        filterCity(
                            newText ?: "",
                            viewModelCity?.cityList!!
                        ) as ArrayList<CityListResponse.Data>
                    )
                    return false;
                }

            })


            // Show the AlertDialog
            mAlertDialog.show()
        }

    }

    private fun filterCity(
        searchText: String,
        cities: ArrayList<CityListResponse.Data>
    ): List<CityListResponse.Data> {

        return cities.filter {
            it.name!!.lowercase().contains(searchText.lowercase())
        }

    }


    private fun observeCityListResponse() {
        viewModelCity.cityListResponse.observe(this) {

            when (it) {
                is CityListResponse -> {
                    if (it.success == 1 && it.data.isNullOrEmpty().not()) {
                        viewModelCity.cityList = it.data as ArrayList<CityListResponse.Data>
                    }
                }

                is ErrorResponse -> {
                    Toast.makeText(this, "Error ${it}", Toast.LENGTH_LONG).show()
                }
            }

        }
    }

    private fun callCityApi() {
        viewModelCity.getCityList()
    }

    private fun setUserData() {
        if (pref?.image.toString().isNullOrEmpty().not()) {
            val requestOptions = RequestOptions()
            val listenerImage: RequestListener<Drawable?> = object : RequestListener<Drawable?> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable?>,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.imgProfile.visibility = View.GONE
                    binding.txtImgProfile.visibility = View.VISIBLE
                    if (pref?.name.toString().isNullOrEmpty().not()) {
                        val firstLetter = pref?.name?.firstOrNull()?.toUpperCase() ?: ""
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
            Glide.with(this).load(pref?.image).listener(listenerImage).apply(requestOptions)
                .fitCenter().into(binding.imgProfile)
        } else {
            binding.imgProfile.visibility = View.GONE
            binding.txtImgProfile.visibility = View.VISIBLE
            if (pref?.name.toString().isNullOrEmpty().not()) {
                val firstLetter = pref?.name?.firstOrNull()?.toUpperCase() ?: ""
                binding.txtImgProfile.text = firstLetter.toString()
            }
        }

        if (pref?.name.isNullOrEmpty().not()) {
            binding.edtName.setText(pref?.name ?: "")
        }

        if (pref?.mobile.isNullOrEmpty().not()) {
            binding.edtMobile.setText(pref?.mobile ?: "")
        }
        if (pref?.email.isNullOrEmpty().not()) {
            binding.edtEmail.setText(pref?.email ?: "")
        }
        if (pref?.city.isNullOrEmpty().not()) {
            binding.edtCity.setText(pref?.city ?: "")
        }

    }

    private fun initView() {
        binding.toolbar.tvHeading.text = getString(R.string.profile_update)
        binding.toolbar.imgBack.setOnClickListener {
            finish()
        }

        binding.rlEditProfile.setOnClickListener {
            val intent = Intent(this, ChooseMediaActivity::class.java)
            intent.putExtra("mediaType", ChooseMediaActivity.MediaEnum.PHOTO)
            intent.putExtra("maxImageSelection", 1)
            takePictureLauncher?.launch(intent)
        }



        binding.changeName.setOnClickListener {
            binding.edtName.isClickable = true
            binding.edtName.isFocusable = true
            binding.edtName.requestFocus()
            binding.edtName.isFocusableInTouchMode = true
            binding.edtName.requestFocus()
            binding.edtName.setSelection(binding.edtName.text.toString().length)

        }

        binding.changeMob.setOnClickListener {
            binding.edtMobile.isClickable = true
            binding.edtMobile.isFocusable = true
            binding.edtMobile.requestFocus()
            binding.edtMobile.isFocusableInTouchMode = true
            binding.edtMobile.requestFocus()
            binding.edtMobile.setSelection(binding.edtMobile.text.toString().length)
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


    private fun disableAllInputField() {
        binding.edtName.isClickable = false
        binding.edtName.isFocusable = false
        binding.edtName.isFocusableInTouchMode = false


        binding.edtMobile.isClickable = false
        binding.edtMobile.isFocusable = false
        binding.edtMobile.isFocusableInTouchMode = false

        binding.edtEmail.isClickable = false
        binding.edtEmail.isFocusable = false
        binding.edtEmail.isFocusableInTouchMode = false

        binding.edtCity.isClickable = false
        binding.edtCity.isFocusable = false
        binding.edtCity.isEnabled = false
        binding.edtCity.isFocusableInTouchMode = false
    }
}