package com.bbu.attendancetracking.ui.profile

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bbu.attendancetracking.MyApplication
import com.bbu.attendancetracking.databinding.FragmentProfileBinding
import com.bbu.attendancetracking.helpers.LocalStorageHelper
import com.bbu.attendancetracking.model.LoginResponse
import com.bbu.attendancetracking.ui.login.LoginActivity
import com.bumptech.glide.Glide

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    // Activity Result Launcher to pick an image
    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            // Load selected image into ShapeableImageView using Glide
            LocalStorageHelper.saveImageUri(it.toString())//save image to local storage
            Glide.with(this)
                .load(it)
                .into(binding.imageView)
        }
    }

    //load image from share preference
    private fun loadSavedImage() {
        val savedUri = LocalStorageHelper.getImageUri()
        savedUri?.let {
            Glide.with(this)
                .load(it)
                .into(binding.imageView)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        profileViewModel.text.observe(viewLifecycleOwner) {
//            binding.textProfile.text = it
//        }

        var loginDetails: LoginResponse? = LocalStorageHelper.getLoginResponse()

        binding.profileName.text = loginDetails?.user?.firstName + " " + loginDetails?.user?.lastName
        binding.profileEmail.text = loginDetails?.user?.email


        binding.profileId.text = (loginDetails?.user?.user_id ?: 123456).toString()



        binding.logoutButton.setOnClickListener{
            val sharedPreferences: SharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putBoolean("isLogin", false)
            editor.apply()
            // Navigate to LoginActivity
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish() // Close MainActivity}
        }
        //load image from local storage
        loadSavedImage()

        binding.imageView.setOnClickListener{
            pickImageLauncher.launch("image/*")
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
