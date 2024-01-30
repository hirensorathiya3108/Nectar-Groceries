package com.nectar.groceries.nectargroceries.ui.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.nectar.groceries.nectargroceries.R
import com.nectar.groceries.nectargroceries.databinding.ActivityMainBinding

class MainActivity : ParentActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var activity: Activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        activity = this@MainActivity
        intiViews()
    }

    private fun intiViews() {
        binding.tvLogin.setOnClickListener {
            startActivity(Intent(activity,LoginActivity::class.java))
        }
    }
}