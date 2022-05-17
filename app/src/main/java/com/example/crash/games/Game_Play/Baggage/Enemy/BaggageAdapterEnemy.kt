package com.example.crash.games.Game_Play.Baggage.Enemy

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.crash.games.Game_Play.Baggage.FirstListFragment
import com.example.crash.games.Game_Play.Baggage.SecondListFragment

class BaggageAdapterEnemy (fm: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fm, lifecycle) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) {
            FirstListFragmentEnemy()
        } else SecondListFragmentEnemy()
    }
}