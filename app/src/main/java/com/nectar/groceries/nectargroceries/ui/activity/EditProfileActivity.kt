package com.nectar.groceries.nectargroceries.ui.activity

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import com.google.gson.Gson
import com.nectar.groceries.nectargroceries.R
import com.nectar.groceries.nectargroceries.data.model.user.AddressData
import com.nectar.groceries.nectargroceries.data.model.user.PaymentData
import com.nectar.groceries.nectargroceries.data.model.user.ProfileData
import com.nectar.groceries.nectargroceries.data.preference.AppPersistence
import com.nectar.groceries.nectargroceries.data.preference.AppPreference
import com.nectar.groceries.nectargroceries.database.FirebaseDB
import com.nectar.groceries.nectargroceries.databinding.ActivityEditProfileBinding
import com.nectar.groceries.nectargroceries.extensions.beGone
import com.nectar.groceries.nectargroceries.extensions.beVisible
import com.nectar.groceries.nectargroceries.utils.Utils

class EditProfileActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var activity: Activity
    private lateinit var appPreference: AppPreference
    private lateinit var profileData: ProfileData
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile)
        activity = this@EditProfileActivity
        initViews()
    }

    private fun initViews() {
        appPreference = AppPreference(activity)
        profileData = appPreference.getProfileDetails()!!
        setDataInField()
        binding.saveButtonHolder.setOnClickListener(this)
        binding.ivBack.setOnClickListener(this)
    }

    // TODO: set profile data
    private fun setDataInField() {
        binding.nameEditText.setText(profileData.userName)
        binding.edtEmail.setText(profileData.email)
        binding.edtCardName.setText(profileData.payment_info.card_name)
        binding.edtCardNumber.setText(profileData.payment_info.card_number.toString())
        binding.edtCardDate.setText(profileData.payment_info.card_expiry_date)
        binding.edtSecurityCode.setText(profileData.payment_info.card_security_code.toString())
        binding.edtApartmentNumber.setText(profileData.address_info.house_Number)
        binding.edtApartmentName.setText(profileData.address_info.aparment_name)
        binding.edtApartmentArea.setText(profileData.address_info.area)
        binding.edtApartmentCity.setText(profileData.address_info.city)
    }

    // TODO: clear profile data
    private fun clearDataInField() {
        binding.nameEditText.setText("")
        binding.edtEmail.setText("")
        binding.edtCardName.setText("")
        binding.edtCardNumber.setText("")
        binding.edtCardDate.setText("")
        binding.edtSecurityCode.setText("")
        binding.edtApartmentNumber.setText("")
        binding.edtApartmentName.setText("")
        binding.edtApartmentArea.setText("")
        binding.edtApartmentCity.setText("")
        binding.edtApartmentState.setText("")
    }

    private fun updateProfileData() {
        val edtUserName = binding.nameEditText.text.toString()
        val edtEmail = binding.edtEmail.text.toString()
        val edtCardName = binding.edtCardName.text.toString()
        val edtCardNumber = binding.edtCardNumber.text.toString()
        val edtCardDate = binding.edtCardDate.text.toString()
        val edtSecurityCode = binding.edtSecurityCode.text.toString()
        val edtApartmentNumber = binding.edtApartmentNumber.text.toString()
        val edtApartmentName = binding.edtApartmentName.text.toString()
        val edtApartmentArea = binding.edtApartmentArea.text.toString()
        val edtApartmentCity = binding.edtApartmentCity.text.toString()

        val changeInData = checkChangeForUpdate()
        if (!changeInData){
            Utils().showToast(activity,activity.getString(R.string.password_length_invalid))
            return
        }

        updateInFirebase(
            edtUserName,
            edtEmail,
            edtCardName,
            edtCardNumber,
            edtCardDate,
            edtSecurityCode,
            edtApartmentNumber,
            edtApartmentName,
            edtApartmentArea,
            edtApartmentCity,
        )

    }

    private fun updateInFirebase(
        edtUserName: String,
        edtEmail: String,
        edtCardName: String,
        edtCardNumber: String,
        edtCardDate: String,
        edtSecurityCode: String,
        edtApartmentNumber: String,
        edtApartmentName: String,
        edtApartmentArea: String,
        edtApartmentCity: String,
    ) {
        changeInProgress(true)
        val documentReference = FirebaseDB().getCollectionReferenceForUser().document("data")
        // Create a map with the updated data

        val updatePaymentData =
            PaymentData(edtCardName, edtCardNumber.toInt(), edtCardDate, edtSecurityCode.toInt())
        val updateAddressData = AddressData(
            edtApartmentNumber,
            edtApartmentName,
            edtApartmentArea,
            edtApartmentCity,
        )
        val updateProfileData = ProfileData(
            edtUserName,
            edtEmail,
            profileData.password,
            updatePaymentData,
            updateAddressData
        )

        val updateData = mapOf(
            "userName" to updateProfileData.userName,
            "email" to updateProfileData.email,
            "password" to updateProfileData.password,
            "payment_info" to updateProfileData.payment_info,
            "address_info" to updateProfileData.address_info,
            // add other fields as needed
        )

        // Update the data in Firebase Realtime Database
        documentReference.update(updateData)
            .addOnSuccessListener {
                Utils().showToast(
                    activity,
                    activity.getString(R.string.update_profile_message)
                )
                val newProfileData = Gson().toJson(updateProfileData)
                appPreference.setPreference(AppPersistence.keys.USER_INFO_DATA,newProfileData)
                profileData = appPreference.getProfileDetails()!!
                if(profileData.address_info.city.isNotEmpty()) appPreference.setPreference(AppPersistence.keys.IS_FILE_ADDRESS_INFO,true)
                clearDataInField()
                setDataInField()
                changeInProgress(false)
            }
            .addOnFailureListener { e ->
                changeInProgress(false)
                Utils().showToast(
                    activity,
                    e.localizedMessage!!
                )
            }
    }

    // TODO: Check Profile details is update
    private fun checkChangeForUpdate(): Boolean {
        val edtUserName = binding.nameEditText.text.toString()
        val edtEmail = binding.edtEmail.text.toString()
        val edtCardName = binding.edtCardName.text.toString()
        val edtCardNumber = binding.edtCardNumber.text.toString()
        val edtCardDate = binding.edtCardDate.text.toString()
        val edtSecurityCode = binding.edtSecurityCode.text.toString()
        val edtApartmentNumber = binding.edtApartmentNumber.text.toString()
        val edtApartmentName = binding.edtApartmentName.text.toString()
        val edtApartmentArea = binding.edtApartmentArea.text.toString()
        val edtApartmentCity = binding.edtApartmentCity.text.toString()
        val edtApartmentState = binding.edtApartmentState.text.toString()

        return edtUserName != profileData.userName ||
                edtEmail != profileData.email ||
                edtCardName != profileData.payment_info.card_name ||
                edtCardNumber != profileData.payment_info.card_number.toString() ||
                edtCardDate != profileData.payment_info.card_expiry_date ||
                edtSecurityCode != profileData.payment_info.card_security_code.toString() ||
                edtApartmentNumber != profileData.address_info.house_Number ||
                edtApartmentName != profileData.address_info.aparment_name ||
                edtApartmentArea != profileData.address_info.area ||
                edtApartmentCity != profileData.address_info.city
    }

    private fun changeInProgress(isShow:Boolean) {
        if (isShow){
            binding.isProgressLoading.beVisible()
            binding.saveButtonHolder.beGone()
        }else{
            binding.saveButtonHolder.beVisible()
            binding.isProgressLoading.beGone()
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.ivBack ->{
              onBackPressedDispatcher.onBackPressed()
            }

            R.id.saveButtonHolder ->{
                updateProfileData()
            }
        }
    }
}