package com.nectar.groceries.nectargroceries.utils

import android.app.Activity
import android.widget.Toast

class Utils {
    fun showToast(activity: Activity,message:String){
        Toast.makeText(activity,message,Toast.LENGTH_SHORT).show()
    }
}