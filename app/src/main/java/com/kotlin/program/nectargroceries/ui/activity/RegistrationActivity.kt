package com.kotlin.program.nectargroceries.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Patterns
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.databinding.DataBindingUtil
import com.kotlin.program.nectargroceries.R
import com.kotlin.program.nectargroceries.data.preference.AppPersistence
import com.kotlin.program.nectargroceries.data.preference.AppPreference
import com.kotlin.program.nectargroceries.databinding.ActivityRegistrationBinding
import com.kotlin.program.nectargroceries.utils.Utils

class RegistrationActivity : AppCompatActivity(), View.OnClickListener{
    private lateinit var binding: ActivityRegistrationBinding
    private lateinit var activity: Activity
    private lateinit var appPreference: AppPreference
    private var isVisibility = false
    private var isConfirmVisibility = false

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
        binding = DataBindingUtil.setContentView(this,R.layout.activity_registration)
        activity = this@RegistrationActivity
        initViews()
    }

    private fun initViews() {
        appPreference = AppPreference(activity)
        // Set the initial input type to 'textPassword' to hide the password
        binding.edtPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        isVisibility = true
        binding.ivVisibility.setImageDrawable(ContextCompat.getDrawable(activity,R.drawable.ic_round_visibility))
        binding.edtConfirmPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        isConfirmVisibility = true
        binding.ivConfirmVisibility.setImageDrawable(ContextCompat.getDrawable(activity,R.drawable.ic_round_visibility))

        val string = activity.getString(R.string.term_condition, activity.getString(R.string.green_color_create, activity.getString(R.string.term_of_service)))
        binding.tvTermCondition.text = HtmlCompat.fromHtml(string, HtmlCompat.FROM_HTML_MODE_LEGACY)


        binding.btnNext.setOnClickListener(this)
        binding.ivVisibility.setOnClickListener(this)
        binding.ivConfirmVisibility.setOnClickListener(this)
        binding.tvLogin.setOnClickListener(this)
    }

    private fun validated(
        edtUsername: String,
        edtEmail: String,
        edtPassword: String,
        edtConfirmPassword: String
    ):Boolean{
        if (edtUsername == ""){
            binding.edtUsername.error = activity.getString(R.string.please_enter_username)
            return false
        }

        if (edtEmail == ""){
            binding.edtEmail.error = activity.getString(R.string.please_enter_email)
            return false
        }

        if (edtPassword == ""){
            binding.edtPassword.error = activity.getString(R.string.please_enter_password)
            return false
        }

        if (edtConfirmPassword == ""){
            binding.edtConfirmPassword.error = activity.getString(R.string.please_enter_username)
            return false
        }

        if (edtPassword != edtConfirmPassword){
            Utils().showToast(activity, activity.getString(R.string.confirm_password_is_not_match))
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(edtEmail).matches()){
            binding.edtEmail.error = activity.getString(R.string.email_is_invalid)
            return false
        }

        val passwordLength = 8
        if (edtPassword.length != passwordLength){
            binding.edtPassword.error = activity.getString(R.string.password_length_invalid)
            return false
        }

        if (edtConfirmPassword.length != passwordLength){
            binding.edtConfirmPassword.error = activity.getString(R.string.password_length_invalid)
            return false
        }
        return true
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnNext -> {
                val edtUsername = binding.edtUsername.text.toString()
                val edtEmail = binding.edtEmail.text.toString()
                val edtPassword = binding.edtPassword.text.toString()
                val edtConfirmPassword = binding.edtConfirmPassword.text.toString()

                val isValidate = validated(edtUsername, edtEmail, edtPassword, edtConfirmPassword)
                if (!isValidate) return

                appPreference.setPreference(AppPersistence.keys.USERNAME,edtUsername)
                appPreference.setPreference(AppPersistence.keys.EMAIL,edtEmail)
                appPreference.setPreference(AppPersistence.keys.PASSWORD,edtPassword)
                appPreference.setPreference(AppPersistence.keys.CONFIRM_PASSWORD,edtConfirmPassword)

                startActivity(Intent(activity,PaymentInfoActivity::class.java))
            }

            R.id.ivVisibility-> {
                isVisibility = if (isVisibility) {
                    // If checked, show the password
                    binding.edtPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    binding.ivVisibility.setImageDrawable(ContextCompat.getDrawable(activity,R.drawable.ic_round_visibility_off))
                    false
                } else {
                    // If unchecked, hide the password
                    binding.edtPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                    binding.ivVisibility.setImageDrawable(ContextCompat.getDrawable(activity,R.drawable.ic_round_visibility))
                    true
                }

                // Move the cursor to the end of the text to maintain the correct cursor position
                binding.edtPassword.setSelection(binding.edtPassword.text!!.length)
            }

            R.id.ivConfirmVisibility-> {
                isConfirmVisibility = if (isConfirmVisibility) {
                    // If checked, show the password
                    binding.edtConfirmPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    binding.ivConfirmVisibility.setImageDrawable(ContextCompat.getDrawable(activity,R.drawable.ic_round_visibility_off))
                    false
                } else {
                    // If unchecked, hide the password
                    binding.edtConfirmPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                    binding.ivConfirmVisibility.setImageDrawable(ContextCompat.getDrawable(activity,R.drawable.ic_round_visibility))
                    true
                }

                // Move the cursor to the end of the text to maintain the correct cursor position
                binding.edtConfirmPassword.setSelection(binding.edtConfirmPassword.text!!.length)
            }

            R.id.tvLogin-> onBackPressedDispatcher.onBackPressed()
        }
    }
}