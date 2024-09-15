package com.scrapwala.screens.referearn

import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.moengage.core.internal.utils.MoEUtils.getSystemService
import com.scrapwala.R
import com.scrapwala.databinding.FragmentReferEarnBinding

class ReferEarnFragment : Fragment() {

private lateinit var binding: FragmentReferEarnBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReferEarnBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        binding.toolbar.tvHeading.text = getString(R.string.refer_and_earn)
        binding.toolbar.imgBack.setOnClickListener{
            requireActivity().onBackPressed()
        }



        binding.btnWhatsapp.setOnClickListener {
            val whatsappIntent = Intent(Intent.ACTION_SEND)
            whatsappIntent.type = "text/plain"
            whatsappIntent.putExtra(Intent.EXTRA_TEXT, "Use code" +" "+binding.couponCode.text.toString()+ " to get discounts!")
            whatsappIntent.setPackage("com.whatsapp")

            try {
                startActivity(whatsappIntent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(requireContext(), "WhatsApp not installed.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnFacebook.setOnClickListener {
            val facebookIntent = Intent(Intent.ACTION_SEND)
            facebookIntent.type = "text/plain"
            facebookIntent.putExtra(Intent.EXTRA_TEXT, "Use code" +" "+binding.couponCode.text.toString()+ " to get discounts!")
            facebookIntent.setPackage("com.facebook.katana")

            try {
                startActivity(facebookIntent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(requireContext(), "Facebook app not installed.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnInstagram.setOnClickListener {
            val instagramIntent = Intent(Intent.ACTION_SEND)
            instagramIntent.type = "text/plain"
            instagramIntent.putExtra(Intent.EXTRA_TEXT, "Use code" +" "+binding.couponCode.text.toString()+ " to get discounts!")
            instagramIntent.setPackage("com.instagram.android")

            try {
                startActivity(instagramIntent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(requireContext(), "Instagram app not installed.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnLinkedIn.setOnClickListener {
            val linkedinIntent = Intent(Intent.ACTION_SEND)
            linkedinIntent.type = "text/plain"
            linkedinIntent.putExtra(Intent.EXTRA_TEXT, "Use code" +" "+binding.couponCode.text.toString()+ " to get discounts!")
            linkedinIntent.setPackage("com.linkedin.android")

            try {
                startActivity(linkedinIntent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(requireContext(), "LinkedIn app not installed.", Toast.LENGTH_SHORT).show()
            }
        }


        binding.copyText.setOnClickListener {
            val textToCopy = binding.couponCode.text.toString()
            val clipboard = requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Copied Text", textToCopy)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(requireContext(), "Text copied to clipboard", Toast.LENGTH_SHORT).show()
        }

    }

}