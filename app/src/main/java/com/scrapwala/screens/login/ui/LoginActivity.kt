package com.scrapwala.screens.login.ui

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.scrapwala.R
import com.scrapwala.baseactivity.BaseAppCompatActivity
import com.scrapwala.databinding.ActivityLoginBinding
import com.scrapwala.redirectionhandler.navigateToMainActivity
import com.scrapwala.screens.login.model.LoginViewModel
import com.scrapwala.screens.login.model.SendOtpRequest
import com.scrapwala.screens.login.model.VerifyOtpRequest
import com.scrapwala.screens.login.model.VerifyOtpResponse
import com.scrapwala.screens.pickups.category.model.CategoryResponse
import com.scrapwala.screens.pickups.model.SuccessResponse
import com.scrapwala.screens.pickups.viewmodel.PickupViewModel
import com.scrapwala.utils.ErrorResponse
import com.scrapwala.utils.Preferences
import com.scrapwala.utils.extensionclass.checkDigit
import com.scrapwala.utils.extensionclass.hideSpinner
import com.scrapwala.utils.extensionclass.removePadding
import com.scrapwala.utils.extensionclass.setErrorMessage
import com.scrapwala.utils.extensionclass.showCustomToast
import com.scrapwala.utils.extensionclass.showSpinner
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseAppCompatActivity() {


    private lateinit  var binding: ActivityLoginBinding

    private val viewModel: LoginViewModel by viewModels()


    var time: Long = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)






        binding.resendOtp.setOnClickListener {

            startTimer(60)
            sendOtpRequest()


        }

        binding.txtGetOtp.setOnClickListener {

            if(validate()){


           sendOtpRequest()



            }

        }



        binding.txtContinueButton.setOnClickListener {
            if(validateOtp()){

                var request=VerifyOtpRequest()
                request.mobile=binding.edtPhone.text.toString()

                request.otp=binding.otpView.text.toString()


                showSpinner(this)
                viewModel.verifyOtpRequest(request)

            }

        }


        observeSendOtpResponse()


        observeVerifyOtpResponse()

        binding.otpView.setOtpCompletionListener {
            binding.otpView.setLineColor(resources.getColor(R.color.lineselectorcolor))
            binding.inputOtpView.setErrorEnabled(false)
            binding.inputOtpView.setError(null)

        }





        binding.edtPhone.setOnFocusChangeListener { view, b ->
            if (b) {

                binding.txtSeparator.setBackgroundColor(resources.getColor(R.color.primary_dark))
                var param = binding.txtSeparator.layoutParams
                param.width = 5

                binding.txtSeparator.setLayoutParams(
                    param
                )

            }

            else{
                binding.txtSeparator.setBackgroundColor(resources.getColor(R.color._eaeaea))

                var param = binding.txtSeparator.layoutParams
                param.width = 1

                binding.txtSeparator.setLayoutParams(
                    param
                )

            }
        }
    }

    private fun observeVerifyOtpResponse() {
        viewModel.verifyOtpResponse.observe(this as AppCompatActivity, Observer {
            when (it) {
                is VerifyOtpResponse -> {
                    hideSpinner()


                    if(it.token.isNullOrEmpty().not()){
                        Preferences.setUserToken(this, it.token!!)
                    }


                    if(it.data!=null){
                        Preferences.setUserData(this, Gson().toJson(it.data))


                        navigateToMainActivity(this,null)
                    }




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

    private fun sendOtpRequest() {
        var request=SendOtpRequest()
        request.mobile=binding.edtPhone.text.toString()


        showSpinner(this)
        viewModel.sendOtpRequest(request)
    }

    private fun observeSendOtpResponse() {
        viewModel.sendOtpResponse.observe(this as AppCompatActivity, Observer {
                when (it) {
                    is SuccessResponse -> {

                        hideSpinner()
                        binding.txtVerify.setText("Enter verification code we sent to\n" +
                                "+91"+binding.edtPhone.text.toString())

                        binding.viewFlipper.displayedChild = 1
                        binding.resendOtp.isEnabled = false
                        startTimer( 60)

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



    private fun validateOtp(): Boolean {

        if(binding.otpView.text.toString().isNullOrEmpty()){
            setOtpViewError("Please enter OTP")
            return false
        }
        else{
            return true
        }

    }



    private fun setOtpViewError(message: String) {
        binding.inputOtpView.error = message
        binding.inputOtpView.removePadding()
        binding.otpView.setLineColor(resources.getColor(R.color.red))
    }
    private fun validate(): Boolean {


        if(binding.edtPhone.text.isNullOrEmpty()){
            binding.txtSeparator.setBackgroundColor(resources.getColor(R.color.red))

            binding.number.setErrorMessage("Please enter Mobile Number")

            return false
        }


        else if(binding.edtPhone.text.toString().length<10){
            binding.txtSeparator.setBackgroundColor(resources.getColor(R.color.red))

            binding.number.setErrorMessage("Please enter  valid Mobile Number")

            return false
        }

        return true

    }


    private fun startTimer(retryInterval: Long) {


        var timer = object : CountDownTimer(retryInterval * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.timer.visibility = View.VISIBLE

                binding.timer.setText("in 0:" + checkDigit(time))
                time--
            }

            override fun onFinish() {
                binding.timer.visibility = View.GONE
                binding.resendOtp.isEnabled = true
            }
        }


        time = retryInterval
        timer.start()


    }



}