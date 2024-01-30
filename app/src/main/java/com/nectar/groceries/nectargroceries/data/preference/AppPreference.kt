package com.nectar.groceries.nectargroceries.data.preference

import android.content.Context
import com.google.gson.Gson
import com.nectar.groceries.nectargroceries.MyApplication
import com.nectar.groceries.nectargroceries.data.model.ProfileData

class AppPreference(context: Context?) {

    var mContext: Context? = context

    fun setPreference(name: Enum<*>?, Value: Any?) {
        mContext?.let { AppPersistence.start(it).save(name!!, Value!!) }
    }

    fun getPreference(name: Enum<*>?): Any? {
        return mContext?.let { AppPersistence.start(it).get(name!!) }
    }

//    public Object getJSONPreference(Enum name) {
//        return getGson().fromJson(getPreference(name), Object.class);
//    }
//
//        public Object getJSONPreference(Enum name) {
//            return getGson().fromJson(getPreference(name), Object.class);
//        }

    fun removePreference(name: Enum<*>?) {
        mContext?.let { AppPersistence.start(it).remove(name!!) }
    }

    fun getProfileDetails(): ProfileData? {
        if (AppPreference(MyApplication.context).getPreference(AppPersistence.keys.USER_INFO_DATA) == null)
            AppPreference(MyApplication.context).setPreference(AppPersistence.keys.USER_INFO_DATA, "")

        return if (getPreference(AppPersistence.keys.USER_INFO_DATA) != null ||
            (!(getPreference(AppPersistence.keys.USER_INFO_DATA)?.equals(""))!!)
        ) {
            Gson().fromJson(
                getPreference(AppPersistence.keys.USER_INFO_DATA).toString(),
                ProfileData::class.java
            )
        } else {
            null
        }

    }

    fun <T> fromJson(jsonString: String?, theClass: Class<T>?): T {
        return getGson().fromJson(jsonString, theClass)
    }

    fun getGson(): Gson {
        return Gson()
    }
}