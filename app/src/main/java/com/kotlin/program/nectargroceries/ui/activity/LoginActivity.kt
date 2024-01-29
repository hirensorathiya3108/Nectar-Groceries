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
import androidx.databinding.DataBindingUtil
import com.kotlin.program.nectargroceries.R
import com.kotlin.program.nectargroceries.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity(),View.OnClickListener {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var activity: Activity
    private var isVisibility = false

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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        activity = this@LoginActivity
        initViews()
    }

    private fun initViews() {
        binding.edtPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        isVisibility = true
        binding.ivVisibility.setImageDrawable(ContextCompat.getDrawable(activity,R.drawable.ic_round_visibility))

        binding.btnLogin.setOnClickListener(this)
        binding.ivVisibility.setOnClickListener(this)
        binding.tvSingUp.setOnClickListener(this)
    }

    private fun loginUser() {
        val edtEmail = binding.edtEmail.text.toString()
        val edtPassword = binding.edtPassword.text.toString()

        val isValidated = validated(edtEmail,edtPassword)
        if (!isValidated) return
    }

    private fun validated(edtEmail: String, edtPassword: String): Boolean {
        if (edtEmail == ""){
            binding.edtEmail.error = activity.getString(R.string.please_enter_email)
            return false
        }

        if (edtPassword == ""){
            binding.edtPassword.error = activity.getString(R.string.please_enter_password)
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

        return true
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnLogin ->{
                loginUser()
            }

            R.id.tvSingUp ->{
                startActivity(Intent(activity,RegistrationActivity::class.java))
            }

            R.id.ivVisibility ->{
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
        }
    }
}