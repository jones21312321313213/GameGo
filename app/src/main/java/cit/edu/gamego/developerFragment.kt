package cit.edu.gamego

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment

class developerFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.activity_developer, container, false)

        val abmeArchival = view.findViewById<Button>(R.id.about_me_archival)
        val abmeArco = view.findViewById<Button>(R.id.about_me_arco)
        val back = view.findViewById<ImageView>(R.id.abme_devs_back)

        abmeArchival.setOnClickListener {
            startActivity(Intent(requireContext(), about_me_dev1::class.java))
        }

        abmeArco.setOnClickListener {
            startActivity(Intent(requireContext(), about_me_dev2::class.java))
        }

        back.setOnClickListener {
            startActivity(Intent(requireContext(), landingWIthFragmentActivity::class.java))
        }

        return view
    }
}