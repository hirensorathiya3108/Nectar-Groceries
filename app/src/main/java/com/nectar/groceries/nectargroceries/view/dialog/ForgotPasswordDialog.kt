package com.nectar.groceries.nectargroceries.view.dialog

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.nectar.groceries.nectargroceries.R
import com.nectar.groceries.nectargroceries.databinding.DialogForgotPasswordBinding
import com.nectar.groceries.nectargroceries.utils.Utils

class ForgotPasswordDialog {
    private lateinit var mActivity: Activity
    private lateinit var binding: DialogForgotPasswordBinding
    private lateinit var mDialog: BottomSheetDialog
    fun showDialog(activity: Activity) {
        mActivity = activity
        mDialog = BottomSheetDialog(activity, R.style.SheetDialog)
        binding = DataBindingUtil.inflate(
            activity.layoutInflater,
            R.layout.dialog_forgot_password,
            null,
            false
        )
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mDialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED)
        mDialog.setContentView(binding.root)
        binding.continueBtn.setOnClickListener {
            val email = binding.edtEmail.text.toString()
            if (email.isNotEmpty()){
                forgotPassword(email)
                mDialog.dismiss()
            } else Utils().showToast(mActivity,"Please provide email")

        }
        mDialog.show()
        mDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        mDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        mDialog.window?.setGravity(Gravity.BOTTOM)
    }

    private fun forgotPassword(email: String) {
        val auth = FirebaseAuth.getInstance()
        auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Utils().showToast(mActivity, "Check Your email")
            } else Utils().showToast(mActivity, "Error")
        }
    }
}