package com.kotlin.program.nectargroceries.ui.activity

import android.app.Activity
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.kotlin.program.nectargroceries.R
import com.kotlin.program.nectargroceries.databinding.ActivityPaymentInfoBinding
import com.kotlin.program.nectargroceries.utils.Utils
import java.util.regex.Pattern

class PaymentInfoActivity : AppCompatActivity(),View.OnClickListener {
    private lateinit var binding: ActivityPaymentInfoBinding
    private lateinit var activity: Activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        try {
            if (Build.VERSION.SDK_INT >= 28) {
                val attributes = window.attributes
                attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                window.attributes = attributes
            } else {
                window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            }
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        binding = DataBindingUtil.setContentView(this,R.layout.activity_payment_info)
        activity = this@PaymentInfoActivity
        initViews()
    }

    private fun initViews() {
        binding.ivBack.setOnClickListener(this)
        binding.btnSignUp.setOnClickListener(this)
    }

    private fun createAccount() {
        val edtCardName = binding.edtCardName.text.toString()
        val edtCardNumber = binding.edtCardNumber.text.toString()
        val edtExpiryDate = binding.edtExpiryDate.text.toString()
        val edtSecurityCode = binding.edtSecurityCode.text.toString()

        val isValidated = validated(edtCardName, edtCardNumber, edtExpiryDate, edtSecurityCode)
        if (!isValidated) return

        createAccountInFirebase()

    }

    private fun createAccountInFirebase() {

    }

    private fun validated(
        edtCardName: String,
        edtCardNumber: String,
        edtExpiryDate: String,
        edtSecurityCode: String
    ):Boolean{
        if (edtCardName == ""){
            binding.edtCardName.error = activity.getString(R.string.please_enter_card_name)
            return false
        }

        if (edtCardNumber == ""){
            binding.edtCardNumber.error = activity.getString(R.string.please_enter_card_number)
            return false
        }

        if (edtExpiryDate == ""){
            binding.edtExpiryDate.error = activity.getString(R.string.please_enter_card_expiry_date)
            return false
        }

        if (edtSecurityCode == ""){
            binding.edtSecurityCode.error = activity.getString(R.string.please_enter_card_security_code)
            return false
        }

        if (isValidExpiryDate(edtExpiryDate)){
            binding.edtExpiryDate.error = activity.getString(R.string.expiry_date_format_invalid)
            return false
        }

        if (edtSecurityCode.length != 3){
            binding.edtSecurityCode.error = activity.getString(R.string.security_code_invalid)
            return false
        }

        return true
    }

    private fun isValidExpiryDate(expiryDate: String): Boolean {
        // Define the regex pattern for MM/YY format
        val pattern = Pattern.compile("^\\d{2}/\\d{2}\$")

        // Match the input expiry date against the pattern
        val matcher = pattern.matcher(expiryDate)

        // Return true if the pattern matches, otherwise false
        return matcher.matches()
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.ivBack -> onBackPressedDispatcher.onBackPressed()
            R.id.btnSignUp ->{
                createAccount()
            }
        }
    }
}