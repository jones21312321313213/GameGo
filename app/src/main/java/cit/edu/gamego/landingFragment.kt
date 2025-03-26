package cit.edu.gamego

import android.content.Intent
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.SearchView // Use this for the correct SearchView
import android.widget.TextView
import cit.edu.gamego.data.Game
import cit.edu.gamego.helper.GameListAdapter
import com.google.android.material.search.SearchBar

class landingFragment : Fragment() {

    private lateinit var listOfGame: MutableList<Game>
    private lateinit var filteredList: MutableList<Game>
    private lateinit var arrayAdapter: GameListAdapter
    private lateinit var searchView: SearchView
    private lateinit var listView: ListView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.activity_landing, container, false)

        searchView = view.findViewById(R.id.searchView)
        listView = view.findViewById(R.id.listview)


        /////////////////TEMOPORARY RANI
        val ye = view.findViewById<LinearLayout>(R.id.yeQuestLL)
        val yeIV  = view.findViewById<ImageView>(R.id.yeQuestIV)
        val yeTV = view.findViewById<TextView>(R.id.yeQuestTV)

        val bmwLl =  view.findViewById<LinearLayout>(R.id.bmwLL)
        val bmwIv =  view.findViewById<ImageView>(R.id.bmwIV)
        val bmwTv =  view.findViewById<TextView>(R.id.bmwTV)

        val mwhLl =  view.findViewById<LinearLayout>(R.id.mhwLL)
        val mwhIv =  view.findViewById<ImageView>(R.id.mhwIV)
        val mhwTv =  view.findViewById<TextView>(R.id.mhwTV)

        val hd2Ll =  view.findViewById<LinearLayout>(R.id.hd2LL)
        val hd2Iv =  view.findViewById<ImageView>(R.id.hd2IV)
        val hd2Tv =  view.findViewById<TextView>(R.id.hd2TV)

        ye.setOnClickListener{
            val title = yeTV.text.toString()
            val imageResId = yeIV.tag as? Int ?: R.drawable.aaa // Fallback image
            startActivity(
                Intent(requireContext(),reviewPageActivity::class.java).apply{
                    putExtra("title",title)
                    putExtra("imageRes",R.drawable.ye)
                }
            )
        }

        bmwLl.setOnClickListener{
            val title = bmwTv.text.toString()
            val imageResId = bmwIv.tag as? Int ?: R.drawable.aaa // Fallback image
            startActivity(
                Intent(requireContext(),reviewPageActivity::class.java).apply{
                    putExtra("title",title)
                    putExtra("imageRes",R.drawable.bmw)
                }
            )
        }


        mwhLl.setOnClickListener{
            val title = mhwTv.text.toString()
            val imageResId = mwhIv.tag as? Int ?: R.drawable.aaa // Fallback image
            startActivity(
                Intent(requireContext(),reviewPageActivity::class.java).apply{
                    putExtra("title",title)
                    putExtra("imageRes",R.drawable.mhw)
                }
            )
        }

        hd2Ll.setOnClickListener{
            val title = hd2Tv.text.toString()
            val imageResId = hd2Iv.tag as? Int ?: R.drawable.aaa // Fallback image
            startActivity(
                Intent(requireContext(),reviewPageActivity::class.java).apply{
                    putExtra("title",title)
                    putExtra("imageRes",R.drawable.helldivers)
                }
            )
        }


        ////////////////

        //////////////////////////////////////LIST VIEW BELOW
        // Initially hide listView
        listView.visibility = View.GONE

        // Sample Data
        listOfGame = mutableListOf(
            Game("YE Quest", "2030", R.drawable.ye),
            Game("Helldivers", "2022", R.drawable.helldivers),
            Game("Black Myth Wukong", "2024", R.drawable.bmw),
            Game("Monster Hunter World", "2018", R.drawable.mhw)
        )

        filteredList = listOfGame.toMutableList()

        // Initialize Adapter
        arrayAdapter = GameListAdapter(
            requireContext(),
            filteredList,
            onClickMore = { game -> more(game) },
            onClickItem = { game ->
                // Do not add the same game again, just highlight/select it
                abc(game)
            },
            onLongPress = { position -> showDeleteDialog(position) }
        )

        listView.adapter = arrayAdapter


        ////////////////////////////SEARCH VIWE BELOW
        searchView.setOnClickListener {
            searchView.isIconified = false
        }

        // Show ListView when clicking SearchView
        searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            listView.visibility = if (hasFocus) View.VISIBLE else View.GONE
        }

        // SearchView Listener
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                filterList(query)
                searchView.clearFocus()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                if (newText.isNullOrEmpty()) {
                    searchView.isIconified = true // Collapse SearchView when cleared
                    listView.visibility = View.GONE
                }
                return true
            }
        })
        searchView.setOnCloseListener {
            listView.visibility = View.GONE // Hide ListView
            false
        }

        return view;
    }

    private fun filterList(query: String?) {
        filteredList.clear()
        if (query.isNullOrEmpty()) {
            filteredList.addAll(listOfGame) // Show all if search is empty
        } else {
            val searchQuery = query.lowercase()
            filteredList.addAll(listOfGame.filter {
                it.name.lowercase().contains(searchQuery) || it.date.contains(searchQuery)
            })
        }
        arrayAdapter.notifyDataSetChanged()

        // Show listView only when there are resultsa
        listView.visibility = if (filteredList.isNotEmpty()) View.VISIBLE else View.GONE
    }

    private fun more(game: Game) {
        startActivity(
            Intent(requireContext(),reviewPageActivity::class.java).apply{
                putExtra("title",game.name)
                putExtra("imageRes",game.photo)
            }
        )
    }

    private fun abc(game: Game){

    }

    private fun showDeleteDialog(position: Int) {
        // Implement delete logic
    }
}