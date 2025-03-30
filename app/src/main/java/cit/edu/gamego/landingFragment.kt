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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cit.edu.gamego.data.Game
import cit.edu.gamego.helper.GameListAdapter
import cit.edu.gamego.helper.GameRecyclerViewAdapter
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


       // temp
        val bmwTrailer = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/pnSsgRJmsCc?si=Fy9aZVKwThO7lKAi\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>".trimIndent()
        val helldiversTrailer = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/UC5EpJR0GBQ?si=1IogxXO2cIXA1Mw5\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>".trimIndent()
        val mhwTrailer = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/OotQrKEqe94?si=ZJMQ0Ipel03V7Ouq\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>".trimIndent()
        val kanyeeee = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/X3d5rT7FGLE?si=Fekc_iv8eCUh2DHp\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>".trimIndent()
        val dota2Trailer = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/-cSFPIwMEq4?si=snIX76ATWr7M80fI\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>".trimIndent()
        val lolTrailer = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/RprbAMOPsH0?si=RCu5hNf6GQoDNvjg\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>".trimIndent()
        val cs2Trailer = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/nSE38xjMLqE?si=NrrEFt0Up-TpVYpJ\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>".trimIndent()
        val valoTrailer = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/e_E9W2vsRbQ?si=0IaLcsue6tIicRol\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>".trimIndent()
        val gowTrailer = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/hfJ4Km46A-0?si=baY8Yfl9Zer1BSCn\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>".trimIndent()
        val eldenRingTrailer = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/AKXiKBnzpBQ?si=zBuJ8VqG7Y5gjWOU\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>".trimIndent()
        //////////////////////// RECYCLER VIEW
        val listOfGame2 = listOf(
            Game("YE Quest", "2030",1.1, R.drawable.ye,kanyeeee),
            Game("Helldivers 2", "2022",8.2, R.drawable.helldivers,helldiversTrailer),
            Game("Black Myth Wukong", "2024",9.3, R.drawable.bmw,bmwTrailer),
            Game("Monster Hunter: World", "2018",8.4, R.drawable.mhw,mhwTrailer),
            Game("DOTA 2", "2011",8.8, R.drawable.dota,dota2Trailer),
            Game("League of Legends", "2012",0.0, R.drawable.lol,lolTrailer),
            Game("Counter Strike 2", "2023",6.6, R.drawable.cs2,cs2Trailer),
            Game("God of War: Ragnarok", "2022",9.9, R.drawable.gowrag,gowTrailer),
            Game("Valorant", "2020",5.5, R.drawable.valo,valoTrailer),
            Game("Elden Ring", "2018",10.0, R.drawable.eldenring,eldenRingTrailer)
        )


        val recyclerView =view.findViewById<RecyclerView>(R.id.recyclerview)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

    recyclerView.adapter =GameRecyclerViewAdapter(
        requireContext(),
        listOfGame2,
        onClick ={game->
            startActivity(
                Intent(requireContext(),reviewPageActivity::class.java).apply{
                        putExtra("title",game.name)
                        putExtra("imageRes",game.photo)
                        putExtra("ratings",game.rating)
                        putExtra("trailer",game.gameTrailer)
                }
            )
        }
    )


        //////////////////////////////////////LIST VIEW BELOW
        // Initially hide listView
        listView.visibility = View.GONE

        // Sample Data
        listOfGame = mutableListOf(
            Game("YE Quest", "2030",1.1, R.drawable.ye,kanyeeee),
            Game("Helldivers 2", "2022",8.2, R.drawable.helldivers,helldiversTrailer),
            Game("Black Myth Wukong", "2024",9.3, R.drawable.bmw,bmwTrailer),
            Game("Monster Hunter: World", "2018",8.4, R.drawable.mhw,mhwTrailer),
            Game("DOTA 2", "2011",8.8, R.drawable.dota,dota2Trailer),
            Game("League of Legends", "2012",0.0, R.drawable.lol,lolTrailer),
            Game("Counter Strike 2", "2023",6.6, R.drawable.cs2,cs2Trailer),
            Game("God of War: Ragnarok", "2022",9.9, R.drawable.gowrag,gowTrailer),
            Game("Valorant", "2020",5.5, R.drawable.valo,valoTrailer),
            Game("Elden Ring", "2018",10.0, R.drawable.eldenring,eldenRingTrailer)
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
                putExtra("trailer",game.gameTrailer)
                putExtra("ratings",game.rating)
            }
        )
    }

    private fun abc(game: Game){

    }

    private fun showDeleteDialog(position: Int) {
        // Implement delete logic
    }
}