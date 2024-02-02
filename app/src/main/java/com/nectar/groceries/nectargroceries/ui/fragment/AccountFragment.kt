package com.nectar.groceries.nectargroceries.ui.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import com.google.firebase.auth.FirebaseAuth
import com.nectar.groceries.nectargroceries.R
import com.nectar.groceries.nectargroceries.data.model.user.ProfileData
import com.nectar.groceries.nectargroceries.data.preference.AppPersistence
import com.nectar.groceries.nectargroceries.data.preference.AppPreference
import com.nectar.groceries.nectargroceries.databinding.FragmentAccountBinding
import com.nectar.groceries.nectargroceries.extensions.beGone
import com.nectar.groceries.nectargroceries.extensions.beVisible
import com.nectar.groceries.nectargroceries.ui.activity.EditProfileActivity
import com.nectar.groceries.nectargroceries.ui.activity.LoginActivity


class AccountFragment : ParentFragment(),View.OnClickListener {
    private lateinit var binding :FragmentAccountBinding
    private lateinit var activity: Activity
    private lateinit var appPreference:AppPreference
    private lateinit var profileData:ProfileData
    companion object{
        fun newInstance(title: String): AccountFragment {
            val fragment = AccountFragment()
            val args = Bundle()
            args.putString("title", title)
            fragment.arguments = args
            return fragment
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        activity = requireActivity()
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_account, container, false)
        initViews()
        return binding.root
    }

    private fun initViews() {
        appPreference = AppPreference(activity)
        displayData()
        binding.btnLoginIn.setOnClickListener(this)
        binding.btnLogOut.setOnClickListener(this)
        binding.btnEdiProfile.setOnClickListener(this)
    }

    private fun displayData() {
        val isLogin = appPreference.getPreference(AppPersistence.keys.IS_LOGIN) as Boolean
        if (isLogin){
            profileData = appPreference.getProfileDetails()!!
            binding.tvUserName.text = profileData.userName
            binding.tvEmail.text = profileData.email
        }
    }

    override fun onResume() {
        super.onResume()
        val isLogin = appPreference.getPreference(AppPersistence.keys.IS_LOGIN) as Boolean
        if (isLogin){
            binding.llUserLogin.beGone()
            binding.clUserContent.beVisible()
        } else{
            binding.clUserContent.beGone()
            binding.llUserLogin.beVisible()
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnLoginIn -> {
                startActivity(Intent(activity, LoginActivity::class.java))
            }
            R.id.btnEdiProfile -> startActivity(Intent(activity, EditProfileActivity::class.java))
            R.id.btnLogOut -> {
                val auth = FirebaseAuth.getInstance()
                auth.signOut()
                appPreference.setPreference(AppPersistence.keys.IS_LOGIN, false)
                appPreference.setPreference(AppPersistence.keys.IS_FILE_ADDRESS_INFO, false)
                binding.clUserContent.beGone()
                binding.llUserLogin.beVisible()
            }
        }
    }
}