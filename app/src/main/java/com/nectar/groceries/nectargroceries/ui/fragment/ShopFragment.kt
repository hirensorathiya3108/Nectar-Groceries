package com.nectar.groceries.nectargroceries.ui.fragment

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.nectar.groceries.nectargroceries.R
import com.nectar.groceries.nectargroceries.databinding.FragmentShopBinding
import needle.Needle
import needle.UiRelatedTask


class ShopFragment : ParentFragment() {
    private lateinit var binding :FragmentShopBinding
    private lateinit var activity: Activity
    companion object{
        fun newInstance(title: String): ShopFragment {
            val fragment = ShopFragment()
            val args = Bundle()
            args.putString("title", title)
            fragment.arguments = args
            return fragment
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        activity = requireActivity()
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_shop, container, false)
        initViews()
        return binding.root
    }

    private fun initViews() {
        getExclusiveOffer()
        getExclusiveOffer()
    }

    private fun getExclusiveOffer() {
        Needle.onBackgroundThread().execute(object:UiRelatedTask<Void?>(){
            override fun doWork(): Void? {
                return null
            }

            override fun thenDoUiRelatedWork(result: Void?) {
                setExclusiveAdapter()
            }

        })

    }

    private fun setExclusiveAdapter() {
        binding.rcvExclusiveOffer.apply {
            layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
            setHasFixedSize(true)
            adapter
        }
    }
}