package com.nectar.groceries.nectargroceries.ui.activity

import android.app.Activity
import android.media.MediaPlayer.OnCompletionListener
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.nectar.groceries.nectargroceries.R
import com.nectar.groceries.nectargroceries.data.preference.AppPersistence
import com.nectar.groceries.nectargroceries.data.preference.AppPreference
import com.nectar.groceries.nectargroceries.databinding.ActivityPaymentInfoBinding
import com.nectar.groceries.nectargroceries.extensions.beGone
import com.nectar.groceries.nectargroceries.extensions.beVisible
import com.nectar.groceries.nectargroceries.utils.Utils
import java.util.regex.Pattern

class PaymentInfoActivity : ParentActivity(),View.OnClickListener {
    private lateinit var binding: ActivityPaymentInfoBinding
    private lateinit var activity: Activity
    private lateinit var appPreference:AppPreference
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
        activity = this@PaymentInfoActivity
        hideNavigate(activity,true)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_payment_info)
        initViews()
    }

    private fun initViews() {
        appPreference = AppPreference(activity)
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

        val username =appPreference.getPreference(AppPersistence.keys.USERNAME) as String
        val email =appPreference.getPreference(AppPersistence.keys.EMAIL) as String
        val password =appPreference.getPreference(AppPersistence.keys.PASSWORD) as String
        val confirmPassword =appPreference.getPreference(AppPersistence.keys.CONFIRM_PASSWORD) as String

        createAccountInFirebase(
                username,
                email,
                password,
                confirmPassword)

    }

    private fun createAccountInFirebase(
        username: String,
        email: String,
        password: String,
        confirmPassword: String
    ) {
        changeInProgress(true)

        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(object :
            OnCompleteListener<AuthResult> {
            override fun onComplete(task: Task<AuthResult>) {
               if (task.isSuccessful){
                   Utils().showToast(activity,"Successfully create account")
                   Log.e("onComplete: ","firebase => ${firebaseAuth.currentUser?.email}" )
               } else {
                   Utils().showToast(activity, task.exception!!.localizedMessage!!)
               }
            }

        })
    }

    private fun changeInProgress(isShow:Boolean){
        if (isShow){
            binding.clProgress.beVisible()
            binding.btnSignUp.beGone()
        } else {
            binding.clProgress.beVisible()
            binding.btnSignUp.beGone()
        }

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