package com.mvproject.tvprogramguide.customlists

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class OrderCategoryAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return ORDERS_PAGE_COUNT
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            SELECTED_CHANNELS -> SelectedChannelsFragment()
            else -> AllChannelsFragment()
        }
    }

    companion object {
        const val ORDERS_PAGE_COUNT = 2
        const val SELECTED_CHANNELS = 1
    }
}
