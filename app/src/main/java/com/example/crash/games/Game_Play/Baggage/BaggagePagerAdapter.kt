package com.example.crash.games.Game_Play.Baggage

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.crash.basic_menu.Achievements.Achievements
import com.example.crash.basic_menu.PlayMenu

class BaggagePagerAdapter(fm: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fm, lifecycle) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) {
            FirstListFragment()
        } else SecondListFragment()
    }
}