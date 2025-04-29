package cit.edu.gamego

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cit.edu.gamego.databinding.FragmentAdvanceSearchBinding
import com.google.android.material.chip.ChipGroup


class advance_search : Fragment() {
    private var b: FragmentAdvanceSearchBinding? = null
    private val binding get() = b!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        b = FragmentAdvanceSearchBinding.inflate(inflater, container, false)

        with(binding) {
            genreChipGroup.visibility = View.GONE
            themeChipGroup.visibility = View.GONE
            platformChipGroup.visibility = View.GONE

            // Set initial arrow states
            genreTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_down, 0)
            themeTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_down, 0)
            platformTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_down, 0)

            // Set up toggle functionality with animations
            setupToggle(genreTitle, genreChipGroup)
            setupToggle(themeTitle, themeChipGroup)
            setupToggle(platformTitle, platformChipGroup)
        }

        return binding.root;
    }

    private fun setupToggle(titleView: TextView, chipGroup: ChipGroup) {
        titleView.setOnClickListener {
            if (chipGroup.visibility == View.VISIBLE) {
                // Collapse with animation
                chipGroup.animate()
                    .alpha(0f)
                    .setDuration(200)
                    .withEndAction {
                        chipGroup.visibility = View.GONE
                    }
                titleView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_down, 0)
            } else {
                // Expand with animation
                chipGroup.alpha = 0f
                chipGroup.visibility = View.VISIBLE
                chipGroup.animate()
                    .alpha(1f)
                    .setDuration(200)
                titleView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_up, 0)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        b = null
    }

}