package cit.edu.gamego.helper

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import cit.edu.gamego.GameDescriptionFragment
import cit.edu.gamego.GameDetailsFragment
import cit.edu.gamego.GameMediaFragment

class FragmentPageAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
): FragmentStateAdapter(fragmentManager, lifecycle){
    override fun getItemCount(): Int {
        return 3
    }
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> GameDescriptionFragment()
            1 -> GameMediaFragment()
            else -> GameDetailsFragment()
        }
    }

}