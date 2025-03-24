package cit.edu.gamego

import android.os.Bundle
import android.app.Activity
import android.widget.ListView
import android.widget.SearchView
import cit.edu.gamego.data.Game
import cit.edu.gamego.helper.GameListAdapter

class SearchWithList : Activity() {
    lateinit var listOfGame: MutableList<Game>
    lateinit var filteredList: MutableList<Game>
    lateinit var arrayAdapter: GameListAdapter
    lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val listView = findViewById<ListView>(R.id.listview)
        searchView = findViewById(R.id.searchView)

        // Original list
        listOfGame = mutableListOf(
            Game("YE Quest", "2030", R.drawable.ye),
            Game("Helldivers", "2022", R.drawable.helldivers),
            Game("Black Myth Wukong", "2024", R.drawable.bmw),
            Game("Monster Hunter World", "2018", R.drawable.mhw)
        )

        // Filtered list starts as full list
        filteredList = listOfGame.toMutableList()

        // Initialize adapter with the filtered list
        arrayAdapter = GameListAdapter(
            this,
            filteredList,
            onClickMore = { game -> showDetailsDialog(game) },
            onClickItem = { game ->
                listOfGame.add(game)
                arrayAdapter.notifyDataSetChanged()
            },
            onLongPress = { position -> showDeleteDialog(position) }
        )

        listView.adapter = arrayAdapter

        // Make sure SearchView gains focus when clicked
        searchView.setOnClickListener {
            searchView.isIconified = false // Expand SearchView when clicked
        }

        // SearchView Listener
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                filterList(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }
        })
    }

    private fun filterList(query: String?) {
        filteredList.clear()
        if (query.isNullOrEmpty()) {
            filteredList.addAll(listOfGame) // Show all items if search is empty
        } else {
            val searchQuery = query.lowercase()
            filteredList.addAll(listOfGame.filter {
                it.name.lowercase().contains(searchQuery) || it.date.contains(searchQuery)
            })
        }
        arrayAdapter.notifyDataSetChanged()
    }

    private fun showDetailsDialog(game: Game) {
        // Implement dialog if needed
    }

    private fun showDeleteDialog(position: Int) {
        // Implement delete logic if needed
    }
}
