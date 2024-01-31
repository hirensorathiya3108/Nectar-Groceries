package com.nectar.groceries.nectargroceries.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.nectar.groceries.nectargroceries.ui.fragment.ParentFragment

class FragmentViewPagerAdapter(fragmentManager: FragmentManager, lifeCycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifeCycle) {

    private var fragments: ArrayList<ParentFragment> = ArrayList()

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

    override fun getItemCount(): Int {
        return fragments.size
    }

    fun setFragments(fragments:ArrayList<ParentFragment>){
        this.fragments = fragments
    }

    fun openCurrentFragment(viewPager: ViewPager2): Fragment {
        return fragments[viewPager.currentItem]
    }
}