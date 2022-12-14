package com.psm.hiring.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class jobsViewPagerAdapter(fragmentManager : FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {

    private var arrayList : ArrayList<Fragment> = ArrayList<Fragment>()

    override fun createFragment(position: Int): Fragment {
        return arrayList[position]
    }


    override fun getItemCount(): Int {
        return  arrayList.size
    }

    fun addFragment(fragment : Fragment){
        arrayList.add(fragment)
    }
}