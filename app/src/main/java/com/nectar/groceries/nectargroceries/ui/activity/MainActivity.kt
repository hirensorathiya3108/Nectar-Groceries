package com.nectar.groceries.nectargroceries.ui.activity

import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.nectar.groceries.nectargroceries.R
import com.nectar.groceries.nectargroceries.data.model.user.ProfileData
import com.nectar.groceries.nectargroceries.data.preference.AppPreference
import com.nectar.groceries.nectargroceries.databinding.ActivityMainBinding
import com.nectar.groceries.nectargroceries.extensions.isEnable
import com.nectar.groceries.nectargroceries.extensions.isNotEnable
import com.nectar.groceries.nectargroceries.ui.adapter.FragmentViewPagerAdapter
import com.nectar.groceries.nectargroceries.ui.fragment.AccountFragment
import com.nectar.groceries.nectargroceries.ui.fragment.CartFragment
import com.nectar.groceries.nectargroceries.ui.fragment.ShopFragment

class MainActivity : ParentActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var activity: Activity
    private lateinit var fragmentViewPagerAdapter: FragmentViewPagerAdapter
    private lateinit var appPreference: AppPreference
    private lateinit var profileData: ProfileData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        activity = this@MainActivity
        intiViews()
    }

    private fun intiViews() {
        appPreference = AppPreference(activity)
//        profileData = appPreference.getProfileDetails()!!
        setFragment()
//        Log.e("intiViews: ","profileData => $profileData")

        // TODO: set color in bottom view
        val iconColorStates = createColorStateList(ContextCompat.getColor(activity,R.color.themeColor),ContextCompat.getColor(activity,R.color.black))
        val textColorStates = createColorStateList(ContextCompat.getColor(activity,R.color.themeColor), ContextCompat.getColor(activity,R.color.black))

        binding.bottomNav.itemIconTintList = iconColorStates
        binding.bottomNav.itemTextColor = textColorStates
    }

    private fun setFragment() {
        val fragments = arrayListOf(
            ShopFragment.newInstance("ShopFragment"),
            CartFragment.newInstance("CartFragment"),
            AccountFragment.newInstance("AccountFragment"),
        )

        fragmentViewPagerAdapter = FragmentViewPagerAdapter(supportFragmentManager, lifecycle)
        fragmentViewPagerAdapter.setFragments(fragments = fragments)
        binding.vpFragment.adapter = fragmentViewPagerAdapter
        binding.vpFragment.offscreenPageLimit = ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT

        binding.vpFragment.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.bottomNav.menu.getItem(position).isChecked = true
                enableMenuItemsForFragment()
            }
        })

        binding.bottomNav.setOnNavigationItemSelectedListener { item ->
            val position = when (item.itemId) {
                R.id.shopView -> 0
                R.id.cartView -> 1
                R.id.accountView -> 2
                else -> -1
            }
            if (position != -1) {
                binding.vpFragment.currentItem = position
                enableMenuItemsForFragment()
            }
            true
        }
    }

    private fun enableMenuItemsForFragment() {
        val shopView = binding.bottomNav.menu.findItem(R.id.shopView)
        val cartView = binding.bottomNav.menu.findItem(R.id.cartView)
        val accountView = binding.bottomNav.menu.findItem(R.id.accountView)

        shopView.isEnable()
        cartView.isEnable()
        accountView.isEnable()

        when (getCurrentFragment()) {
            is ShopFragment -> {
                shopView.isNotEnable()
                shopView.isChecked = true
            }

            is CartFragment -> {
                cartView.isNotEnable()
            }

            is AccountFragment -> {
                accountView.isNotEnable()
            }
        }
    }

    private fun getCurrentFragment() =
        fragmentViewPagerAdapter.openCurrentFragment(binding.vpFragment)

    private fun createColorStateList(activeColor: Int, inActiveColor: Int): ColorStateList {
        return ColorStateList(
            arrayOf(
                intArrayOf(-android.R.attr.state_checked),
                intArrayOf(android.R.attr.state_checked)
            ),
            intArrayOf(
                inActiveColor,
                activeColor
            )
        )
    }
}