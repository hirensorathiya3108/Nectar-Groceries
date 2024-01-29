package com.kotlin.program.nectargroceries.ui.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.kotlin.program.nectargroceries.R
import com.kotlin.program.nectargroceries.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
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