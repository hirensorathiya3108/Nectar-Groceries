package com.nectar.groceries.nectargroceries.view.dialog

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.Gravity
import android.view.View.OnClickListener
import android.view.Window
import android.view.WindowManager
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.nectar.groceries.nectargroceries.R
import com.nectar.groceries.nectargroceries.data.model.products.CartProductsData
import com.nectar.groceries.nectargroceries.data.model.user.AddressData
import com.nectar.groceries.nectargroceries.data.model.user.ProfileData
import com.nectar.groceries.nectargroceries.data.preference.AppPersistence
import com.nectar.groceries.nectargroceries.data.preference.AppPreference
import com.nectar.groceries.nectargroceries.database.FirebaseDB
import com.nectar.groceries.nectargroceries.databinding.DialogAddressInfoBinding
import com.nectar.groceries.nectargroceries.extensions.beGone
import com.nectar.groceries.nectargroceries.extensions.beInvisible
import com.nectar.groceries.nectargroceries.extensions.beVisible
import com.nectar.groceries.nectargroceries.ui.activity.MainActivity
import com.nectar.groceries.nectargroceries.utils.Utils

class AddressInformationDialog {
    private lateinit var mActivity: Activity
    private lateinit var binding: DialogAddressInfoBinding
    private lateinit var mDialog: BottomSheetDialog
    private lateinit var appPreference: AppPreference
    private lateinit var mOnDialogClickListener:OnDialogClickListener
    interface OnDialogClickListener{
        fun onClicked()
    }
    fun showDialog(activity: Activity,onDialogClickListener:OnDialogClickListener) {
        if(onDialogClickListener!= null) mOnDialogClickListener = onDialogClickListener
        mActivity = activity
        appPreference = AppPreference(mActivity)
        mDialog = BottomSheetDialog(activity, R.style.SheetDialog)
        binding = DataBindingUtil.inflate(
            activity.layoutInflater,
            R.layout.dialog_address_info,
            null,
            false
        )
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mDialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED)
        mDialog.setContentView(binding.root)
        binding.continueBtn.setOnClickListener {
            sendAddressData()
        }
        mDialog.show()
        mDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        mDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        mDialog.window?.setGravity(Gravity.BOTTOM)
    }

    private fun sendAddressData() {
        val edtApartmentNumber = binding.edtApartmentNumber.text.toString()
        val edtApartmentName = binding.edtApartmentName.text.toString()
        val edtAreaName = binding.edtAreaName.text.toString()
        val edtCityName = binding.edtCityName.text.toString()
        val isValidated =
            validated(edtApartmentNumber, edtApartmentName, edtAreaName, edtCityName)
        if (!isValidated) return

        updateDataInFirebase(
            edtApartmentNumber,
            edtApartmentName,
            edtAreaName,
            edtCityName,
        )
    }

    private fun updateDataInFirebase(
        edtApartmentNumber: String,
        edtApartmentName: String,
        edtAreaName: String,
        edtCityName: String,
    ) {
        val addressData = AddressData(
            edtApartmentNumber,
            edtApartmentName,
            edtAreaName,
            edtCityName,
        )

        val documentReference = FirebaseDB().getCollectionReferenceForUser().document("data")
        // Create a map with the updated data
        val updatedData = mapOf(
            "address_info" to addressData,
            // Add other fields as needed
        )
        // Update the data in Firebase Realtime Database
        documentReference.update(updatedData)
            .addOnSuccessListener {
                getUserData()
                mOnDialogClickListener.onClicked()
                Utils().showToast(mActivity, mActivity.getString(R.string.address_information_added_successfully))
            }
            .addOnFailureListener { e ->
            }
    }

    private fun getUserData() {
        val documentReference =
            FirebaseDB().getCollectionReferenceForUser().document("data")
        documentReference.addSnapshotListener { value, error ->
            if (error != null) {
                Utils().showToast(mActivity,"Error fetching document: $error")
                return@addSnapshotListener
            }
            if (value != null) {
                val profileData = value.toObject<ProfileData>(ProfileData::class.java)!!
                if (profileData != null) {
                    val json = Gson().toJson(profileData)
                    AppPreference(mActivity).setPreference(
                        AppPersistence.keys.USER_INFO_DATA,
                        json
                    )
                    appPreference.setPreference(AppPersistence.keys.USER_INFO_DATA,json)
                    appPreference.setPreference(AppPersistence.keys.IS_LOGIN,true)
                    appPreference.setPreference(AppPersistence.keys.IS_FILE_ADDRESS_INFO,true)
                    changeInProgress(false)
                    mDialog.dismiss()
                    Utils().showToast(mActivity, "User info get successfully")
                } else {
                    changeInProgress(false)
                    appPreference.setPreference(AppPersistence.keys.IS_LOGIN,false)
                }
            }
        }
    }
    private fun validated(
        edtApartmentNumber: String,
        edtApartmentName: String,
        edtAreaName: String,
        edtCityName: String,
    ): Boolean {
        if (edtApartmentNumber.isEmpty() &&
            edtApartmentName.isEmpty() &&
            edtAreaName.isEmpty() &&
            edtCityName.isEmpty()
        ) {
            Utils().showToast(mActivity, mActivity.getString(R.string.address_info_null_message))
            return false
        }
        return true
    }

    private fun changeInProgress(isShow: Boolean) {
        if (isShow) {
            binding.clProgress.beVisible()
            binding.continueBtn.beInvisible()
        } else {
            binding.clProgress.beGone()
            binding.continueBtn.beVisible()
        }
    }
}