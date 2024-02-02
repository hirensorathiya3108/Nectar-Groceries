package com.nectar.groceries.nectargroceries.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.nectar.groceries.nectargroceries.R
import com.nectar.groceries.nectargroceries.data.model.user.ProfileData
import com.nectar.groceries.nectargroceries.data.preference.AppPersistence
import com.nectar.groceries.nectargroceries.data.preference.AppPreference
import com.nectar.groceries.nectargroceries.database.FirebaseDB
import com.nectar.groceries.nectargroceries.databinding.ActivityLoginBinding
import com.nectar.groceries.nectargroceries.extensions.beGone
import com.nectar.groceries.nectargroceries.extensions.beInvisible
import com.nectar.groceries.nectargroceries.extensions.beVisible
import com.nectar.groceries.nectargroceries.utils.Utils
import com.nectar.groceries.nectargroceries.view.dialog.ForgotPasswordDialog

class LoginActivity : ParentActivity(), View.OnClickListener {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var activity: Activity
    private lateinit var appPreference: AppPreference
    private lateinit var profileData: ProfileData
    private var isVisibility = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        try {
            if (Build.VERSION.SDK_INT >= 28) {
                val attributes = window.attributes
                attributes.layoutInDisplayCutoutMode =
                    WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                window.attributes = attributes
            } else {
                window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            }
            window.decorView.systemUiVisibility =
                (View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        activity = this@LoginActivity
        initViews()
    }

    private fun initViews() {
        appPreference = AppPreference(activity)
        binding.edtPassword.inputType =
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        isVisibility = true
        binding.ivVisibility.setImageDrawable(
            ContextCompat.getDrawable(
                activity,
                R.drawable.ic_round_visibility
            )
        )

        binding.btnLogin.setOnClickListener(this)
        binding.ivVisibility.setOnClickListener(this)
        binding.tvForgotPassword.setOnClickListener(this)
        binding.tvSingUp.setOnClickListener(this)
    }

    private fun loginUser() {
        val edtEmail = binding.edtEmail.text.toString()
        val edtPassword = binding.edtPassword.text.toString()

        val isValidated = validated(edtEmail, edtPassword)
        if (!isValidated) return

        loginWithFirebase(edtEmail, edtPassword)
    }

    private fun loginWithFirebase(edtEmail: String, edtPassword: String) {
        changeInProgress(true)
        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.signInWithEmailAndPassword(edtEmail, edtPassword)
            .addOnCompleteListener(object :
                OnCompleteListener<AuthResult> {
                override fun onComplete(task: Task<AuthResult>) {
                    if (task.isSuccessful) {
                        val email = firebaseAuth.currentUser?.email
                        getUserData()
                        Utils().showToast(activity, "Successfully login account")
                    } else {
                        Utils().showToast(activity, task.exception!!.localizedMessage!!)
                        changeInProgress(false)
                    }
                }

            })
    }

    private fun getUserData() {
        val documentReference =
            FirebaseDB().getCollectionReferenceForUser().document("data")
        documentReference.addSnapshotListener { value, error ->
            if (error != null) {
                Utils().showToast(activity,"Error fetching document: $error")
                return@addSnapshotListener
            }
            if (value != null) {
                 val profileData = value.toObject<ProfileData>(ProfileData::class.java)!!
                if (profileData != null) {
                    val json = Gson().toJson(profileData)
                    appPreference.setPreference(AppPersistence.keys.USER_INFO_DATA,json)
                    if(profileData.address_info.city.isNotEmpty()) appPreference.setPreference(AppPersistence.keys.IS_FILE_ADDRESS_INFO,true)
                    appPreference.setPreference(AppPersistence.keys.IS_LOGIN,true)
                    changeInProgress(false)
                    Utils().showToast(activity, "User info get successfully")
                    startActivity(Intent(activity, MainActivity::class.java))
                    finish()
                } else {
                    changeInProgress(false)
                    appPreference.setPreference(AppPersistence.keys.IS_LOGIN,false)
                }
            }
        }
    }

    private fun validated(edtEmail: String, edtPassword: String): Boolean {
        if (edtEmail == "") {
            binding.edtEmail.error = activity.getString(R.string.please_enter_email)
            return false
        }

        if (edtPassword == "") {
            Utils().showToast(activity,activity.getString(R.string.please_enter_password))
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(edtEmail).matches()) {
            binding.edtEmail.error = activity.getString(R.string.email_is_invalid)
            return false
        }

        val passwordLength = 8
        if (edtPassword.length != passwordLength) {
            Utils().showToast(activity,activity.getString(R.string.password_length_invalid))
            return false
        }

        return true
    }

    private fun changeInProgress(isShow: Boolean) {
        if (isShow) {
            binding.clProgress.beVisible()
            binding.btnLogin.beInvisible()
        } else {
            binding.clProgress.beGone()
            binding.btnLogin.beVisible()
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnLogin -> {
                loginUser()
            }

            R.id.tvForgotPassword -> {
                ForgotPasswordDialog().showDialog(activity)
            }
            R.id.tvSingUp -> {
                startActivity(Intent(activity, RegistrationActivity::class.java))
                finish()
            }

            R.id.ivVisibility -> {
                isVisibility = if (isVisibility) {
                    // If checked, show the password
                    binding.edtPassword.inputType =
                        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    binding.ivVisibility.setImageDrawable(
                        ContextCompat.getDrawable(
                            activity,
                            R.drawable.ic_round_visibility_off
                        )
                    )
                    false
                } else {
                    // If unchecked, hide the password
                    binding.edtPassword.inputType =
                        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                    binding.ivVisibility.setImageDrawable(
                        ContextCompat.getDrawable(
                            activity,
                            R.drawable.ic_round_visibility
                        )
                    )
                    true
                }
                // Move the cursor to the end of the text to maintain the correct cursor position
                binding.edtPassword.setSelection(binding.edtPassword.text!!.length)
            }
        }
    }
}