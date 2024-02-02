package com.nectar.groceries.nectargroceries.view.dialog

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.nectar.groceries.nectargroceries.R
import com.nectar.groceries.nectargroceries.databinding.DialogForgotPasswordBinding
import com.nectar.groceries.nectargroceries.databinding.DialogOrderConfirmBinding

class OrderConfirmDialog {
    private lateinit var mActivity: Activity
    private lateinit var binding: DialogOrderConfirmBinding
    private lateinit var mDialog: BottomSheetDialog
    fun showDialog(activity: Activity) {
        mActivity = activity
        mDialog = BottomSheetDialog(activity, R.style.SheetDialog)
        binding = DataBindingUtil.inflate(
            activity.layoutInflater,
            R.layout.dialog_order_confirm,
            null,
            false
        )
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mDialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED)
        mDialog.setContentView(binding.root)
        binding.continueBtn.setOnClickListener {
            mDialog.dismiss()
        }
        mDialog.show()
        mDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        mDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        mDialog.window?.setGravity(Gravity.BOTTOM)
    }
}