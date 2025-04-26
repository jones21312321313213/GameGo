package cit.edu.gamego

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.SearchView // Use this for the correct SearchView

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cit.edu.gamego.data.Game
import cit.edu.gamego.helper.GameListAdapter
import cit.edu.gamego.helper.GameRecyclerViewAdapterwGlide

import cit.edu.gamego.data.ApiClient
import cit.edu.gamego.data.Image
import cit.edu.gamego.data.ReviewListResponse
import cit.edu.gamego.data.SingleGameResponse
import cit.edu.gamego.extensions.enqueueGameList
import cit.edu.gamego.extensions.extractGuidFromUrl
import cit.edu.gamego.extensions.toGame
import cit.edu.gamego.helper.GameRecyclerViewAdapter
import cit.edu.gamego.data.AppCache
import com.facebook.shimmer.ShimmerFrameLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlinx.coroutines.*

import androidx.lifecycle.lifecycleScope
import cit.edu.gamego.data.GameResult
import cit.edu.gamego.data.GiantBombApi
import cit.edu.gamego.data.SearchResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class landingFragment : Fragment() {
    private lateinit var filteredList: MutableList<Game>
    private lateinit var searchView: SearchView
    private lateinit var listView: ListView
    private lateinit var shimmerLayout: ShimmerFrameLayout

    private lateinit var arrayAdapter: GameListAdapter
    private lateinit var randomGamesGameameAdapter: GameRecyclerViewAdapterwGlide
    private lateinit var highRatedGamesGameAdapter: GameRecyclerViewAdapterwGlide
    private lateinit var ps4GamesGameAdapter: GameRecyclerViewAdapterwGlide
    private lateinit var xbox1GamesGameAdapter: GameRecyclerViewAdapterwGlide
    private lateinit var pcGamesGameAdapter: GameRecyclerViewAdapterwGlide
    private lateinit var nintendoSwitchGamesGameAdapter: GameRecyclerViewAdapterwGlide
    private lateinit var mobileGamesGameAdapter: GameRecyclerViewAdapterwGlide
    private lateinit var horrorGamesGameAdapter: GameRecyclerViewAdapterwGlide
    private lateinit var fantasyGamesGameAdapter: GameRecyclerViewAdapterwGlide
    private lateinit var sciFiGamesGameAdapter: GameRecyclerViewAdapterwGlide
    private lateinit var adventureGamesGameAdapter: GameRecyclerViewAdapterwGlide
    private lateinit var romanceGamesGameAdapter: GameRecyclerViewAdapterwGlide

    private lateinit var listOfGame: MutableList<Game>
    private val listOfRandomGames = mutableListOf<Game>()
    private val listOfHighRatedGames = mutableListOf<Game>()
    private val listOfPs4Games = mutableListOf<Game>()
    private val listOfXbox1Games = mutableListOf<Game>()
    private val listOfPcGames = mutableListOf<Game>()
    private val listOfNintendoSwitchGames = mutableListOf<Game>()
    private val listOfMobileGames = mutableListOf<Game>()
    private val listOfHorroGames = mutableListOf<Game>()
    private val listOfFantasyGames = mutableListOf<Game>()
    private val listOfSciFiGames = mutableListOf<Game>()
    private val listOfAdventureGames = mutableListOf<Game>()
    private val listOfRomanceGames = mutableListOf<Game>()

    private var isxbox1GamesLoaded =false
    private var isps4GamesLoaded = false
    private var isHighRatedLoaded = false
    private var isRandomLoaded = false
    private var isnintendoSwitchLoaded = false
    private var ispcGamesLoaded = false
    private var ismobileGamesLoaded = false
    private var ishorroGamesLoaded = false
    private var isfantasyGamesLoaded = false
    private var issciFiGamesLoaded = false
    private var isadventureGamesLoaded = false
    private var isromanceGamesLoaded = false


    private val isGameLoaded = BooleanArray(12)


    private lateinit var recyclerView: RecyclerView
    private lateinit var hrgRecyclerView: RecyclerView
    private lateinit var rgRecyclerView: RecyclerView
    private lateinit var ps4RecyclerView: RecyclerView
    private lateinit var xbox1RecyclerView: RecyclerView
    private lateinit var nintendoSwitchRecyclerView: RecyclerView
    private lateinit var pcRecyclerView: RecyclerView
    private lateinit var mobileRecyclerView: RecyclerView
    private lateinit var horrorRecyclerView: RecyclerView
    private lateinit var fantasyRecyclerView: RecyclerView
    private lateinit var sciFiRecyclerView: RecyclerView
    private lateinit var adventureRecyclerView: RecyclerView
    private lateinit var romanceRecyclerView: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.activity_landing, container, false)

        searchView = view.findViewById(R.id.searchView)
        listView = view.findViewById(R.id.listview)

        shimmerLayout = view.findViewById(R.id.shimmer)
        shimmerLayout.startShimmer()
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

        val listOfGame2 = mutableListOf(
            Game("YE Quest", "2030", "1.1", Image(R.drawable.ye.toString(), R.drawable.ye.toString()), kanyeeee, "The visionary's journey through a surreal rap universe.", false, "game_yequest", listOf("PS5", "PC"), "Yeezy Interactive", listOf("Adventure", "Rhythm"), listOf("Hip-Hop", "Satire"), listOf("YE Series"),listOf("Yeezy Productions"), "YQ"),
            Game("Helldivers 2", "2022", "8.2", Image(R.drawable.helldivers.toString(),R.drawable.helldivers.toString()), helldiversTrailer, "A chaotic co-op shooter fighting for liberty across galaxies.", false, "game_helldivers2", listOf("PS5", "PC"), "Arrowhead Game Studios", listOf("Shooter", "Co-op"), listOf("Sci-fi", "Military"),  listOf("Helldivers"), listOf("PlayStation Studios"),"HD2"),
            Game("Black Myth Wukong", "2024", "9.3", Image(R.drawable.bmw.toString(),R.drawable.bmw.toString()), bmwTrailer, "An epic retelling of the legendary Monkey King's story.", false, "game_blackmythwukong", listOf("PS5", "PC"), "Game Science", listOf("Action RPG"), listOf("Mythology", "Fantasy"),  listOf("Black Myth"),listOf("Game Science"), "BMW"),
            Game("Monster Hunter: World", "2018", "8.4", Image(R.drawable.mhw.toString(),R.drawable.mhw.toString()), mhwTrailer, "Hunt massive monsters in a living, breathing ecosystem.", false, "game_mhw", listOf("PS4", "Xbox One", "PC"), "Capcom", listOf("Action", "RPG"), listOf("Fantasy", "Exploration"), listOf("Monster Hunter"),  listOf("Capcom"),"MHW"),
            Game("DOTA 2", "2011", "8.8", Image(R.drawable.dota.toString(),R.drawable.dota.toString()), dota2Trailer, "Valve’s legendary competitive MOBA title.", false, "game_dota2", listOf("PC"), "Valve", listOf("MOBA"), listOf("Fantasy"),  listOf("DOTA"), listOf("Valve"),"Defense of the Ancients 2"),
            Game("League of Legends", "2012", "0.0", Image(R.drawable.lol.toString(),R.drawable.lol.toString()), lolTrailer, "The world’s biggest MOBA with global competitive scenes.", false, "game_lol", listOf("PC"), "Riot Games", listOf("MOBA"), listOf("Fantasy"),  listOf("League Universe"),listOf("Riot Games"), "LoL"),
            Game("Counter Strike 2", "2023", "6.6", Image(R.drawable.cs2.toString(),R.drawable.cs2.toString()), cs2Trailer, "The next evolution of the iconic tactical shooter.", false, "game_cs2", listOf("PC"), "Valve", listOf("Shooter", "Tactical"), listOf("Modern Warfare"),  listOf("Counter-Strike"), listOf("Valve"),"CS2"),
            Game("God of War: Ragnarok", "2022", "9.9", Image(R.drawable.gowrag.toString(),R.drawable.gowrag.toString()), gowTrailer, "The epic Norse saga of Kratos and Atreus continues.", false, "game_gowrag", listOf("PS4", "PS5"), "Santa Monica Studio", listOf("Action", "Adventure"), listOf("Mythology"),  listOf("God of War"),listOf("Sony Interactive Entertainment"), "GOW: Ragnarok"),
            Game("Valorant", "2020", "5.5", Image(R.drawable.valo.toString(),R.drawable.valo.toString()), valoTrailer, "A 5v5 tactical shooter with unique agent abilities.", false, "game_valorant", listOf("PC"), "Riot Games", listOf("Shooter", "Tactical"), listOf("Futuristic"),  listOf("Valorant"),listOf("Riot Games"), "Valo"),
            Game("Elden Ring", "2018", "10.0", Image(R.drawable.eldenring.toString(),R.drawable.eldenring.toString()), eldenRingTrailer, "A dark fantasy masterpiece from FromSoftware and George R.R. Martin.", false, "game_eldenring", listOf("PS4", "PS5", "Xbox", "PC"), "FromSoftware", listOf("RPG", "Soulslike"), listOf("Dark Fantasy"),  listOf("Elden Ring"),listOf("Bandai Namco"), "ER")
        )

        /////


        // finding the recycler view in the layout
         recyclerView =view.findViewById(R.id.recyclerview)
         rgRecyclerView = view.findViewById(R.id.randomGamesRecyclerView)
         hrgRecyclerView = view.findViewById(R.id.highRatedGamesRecyclerView)
         ps4RecyclerView = view.findViewById(R.id.ps4GamesRecyclerView)
         xbox1RecyclerView = view.findViewById(R.id.xboxOneGamesRecyclerView)
         nintendoSwitchRecyclerView = view.findViewById(R.id.nintendoSwitchGamesRecyclerView)
         pcRecyclerView = view.findViewById(R.id.pcGamesRecyclerView)
         mobileRecyclerView = view.findViewById(R.id.mobileGamesRecyclerView)
         horrorRecyclerView = view.findViewById(R.id.horrorGamesRecyclerView)
         fantasyRecyclerView = view.findViewById(R.id.fantasyGamesRecyclerView)
         sciFiRecyclerView = view.findViewById(R.id.sciFiGamesRecyclerView)
         adventureRecyclerView = view.findViewById(R.id.adventureGamesRecyclerView)
         romanceRecyclerView = view.findViewById(R.id.romanceGamesRecyclerView)
        // setting up the recycler viewers

         recyclerView.layoutManager = LinearLayoutManager(requireContext())
         hrgRecyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
         rgRecyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
         ps4RecyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
         xbox1RecyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
         nintendoSwitchRecyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
         pcRecyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
         mobileRecyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
         horrorRecyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
         fantasyRecyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
         sciFiRecyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
         adventureRecyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
         romanceRecyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)

        // setting up the  adapters
        randomGamesGameameAdapter = GameRecyclerViewAdapterwGlide(
            requireContext(),
            listOfRandomGames,
            onClick ={game->
                moreWGlide(game)
            }
        )

        rgRecyclerView.adapter = randomGamesGameameAdapter

        highRatedGamesGameAdapter = GameRecyclerViewAdapterwGlide(
            requireContext(),
            listOfHighRatedGames,
            onClick ={game->
                moreWGlide(game)
            }
        )
        hrgRecyclerView.adapter = highRatedGamesGameAdapter

        ps4GamesGameAdapter = GameRecyclerViewAdapterwGlide(
            requireContext(),
            listOfPs4Games,
            onClick ={game->
                moreWGlide(game)
            }
        )

        ps4RecyclerView.adapter = ps4GamesGameAdapter

        xbox1GamesGameAdapter = GameRecyclerViewAdapterwGlide(
            requireContext(),
            listOfXbox1Games,
            onClick ={game->
                moreWGlide(game)
            }
        )

        xbox1RecyclerView.adapter = xbox1GamesGameAdapter

        nintendoSwitchGamesGameAdapter = GameRecyclerViewAdapterwGlide(
            requireContext(),
            listOfNintendoSwitchGames,
            onClick ={game->
                moreWGlide(game)
            }
        )

        nintendoSwitchRecyclerView.adapter = nintendoSwitchGamesGameAdapter

        pcGamesGameAdapter = GameRecyclerViewAdapterwGlide(
            requireContext(),
            listOfPcGames,
            onClick ={game->
                moreWGlide(game)
            }
        )

        pcRecyclerView.adapter = pcGamesGameAdapter

        mobileGamesGameAdapter = GameRecyclerViewAdapterwGlide(
            requireContext(),
            listOfMobileGames,
            onClick ={game->
                moreWGlide(game)
            }
        )

        mobileRecyclerView.adapter = mobileGamesGameAdapter

        horrorGamesGameAdapter = GameRecyclerViewAdapterwGlide(
            requireContext(),
            listOfHorroGames,
            onClick ={game->
                moreWGlide(game)
            }
        )

        horrorRecyclerView.adapter = horrorGamesGameAdapter

        fantasyGamesGameAdapter = GameRecyclerViewAdapterwGlide(
            requireContext(),
            listOfFantasyGames,
            onClick ={game->
                moreWGlide(game)
            }
        )

        fantasyRecyclerView.adapter = fantasyGamesGameAdapter

        sciFiGamesGameAdapter = GameRecyclerViewAdapterwGlide(
            requireContext(),
            listOfSciFiGames,
            onClick ={game->
                moreWGlide(game)
            }
        )

        sciFiRecyclerView.adapter = sciFiGamesGameAdapter

        adventureGamesGameAdapter = GameRecyclerViewAdapterwGlide(
            requireContext(),
            listOfAdventureGames,
            onClick ={game->
                moreWGlide(game)
            }
        )

        adventureRecyclerView.adapter = adventureGamesGameAdapter

        romanceGamesGameAdapter = GameRecyclerViewAdapterwGlide(
            requireContext(),
            listOfRomanceGames,
            onClick ={game->
                moreWGlide(game)
            }
        )

        romanceRecyclerView.adapter = romanceGamesGameAdapter
        // this recyclerview is not for API
        recyclerView.adapter = GameRecyclerViewAdapter(
            requireContext(),
            listOfGame2,
            onClick ={game->
                more(game)
            }
        )

        // fetching games using API
 //       fetchRandomGames()
        fetchPopularReviews()
//        fetchPs4PlatformGames()
//        fetchXboxOnePlatformGames()
//        fetchPcPlatformGames()
//        fetchNintendoSwitchPlatformsGames()
//        fetchMobilePlatformGames()
//        fetchHorrorThemeGames()
//        fetchSciFiThemeGames()
//        fetchAdventureThemeGames()
//        fetchFantasyThemeGames()
//        fetchRomanceThemeGames()
        ////////////////////////////////////// FOr search bar which is a list view
        // Initially hide listView
        listView.visibility = View.GONE

        // Sample Data
         listOfGame = mutableListOf(
             Game("YE Quest", "2030", "1.1", Image(R.drawable.ye.toString(), R.drawable.ye.toString()), kanyeeee, "The visionary's journey through a surreal rap universe.", false, "game_yequest", listOf("PS5", "PC"), "Yeezy Interactive", listOf("Adventure", "Rhythm"), listOf("Hip-Hop", "Satire"), listOf("YE Series"),listOf("Yeezy Productions"), "YQ"),
             Game("Helldivers 2", "2022", "8.2", Image(R.drawable.helldivers.toString(),R.drawable.helldivers.toString()), helldiversTrailer, "A chaotic co-op shooter fighting for liberty across galaxies.", false, "game_helldivers2", listOf("PS5", "PC"), "Arrowhead Game Studios", listOf("Shooter", "Co-op"), listOf("Sci-fi", "Military"),  listOf("Helldivers"), listOf("PlayStation Studios"),"HD2"),
             Game("Black Myth Wukong", "2024", "9.3", Image(R.drawable.bmw.toString(),R.drawable.bmw.toString()), bmwTrailer, "An epic retelling of the legendary Monkey King's story.", false, "game_blackmythwukong", listOf("PS5", "PC"), "Game Science", listOf("Action RPG"), listOf("Mythology", "Fantasy"),  listOf("Black Myth"),listOf("Game Science"), "BMW"),
             Game("Monster Hunter: World", "2018", "8.4", Image(R.drawable.mhw.toString(),R.drawable.mhw.toString()), mhwTrailer, "Hunt massive monsters in a living, breathing ecosystem.", false, "game_mhw", listOf("PS4", "Xbox One", "PC"), "Capcom", listOf("Action", "RPG"), listOf("Fantasy", "Exploration"), listOf("Monster Hunter"),  listOf("Capcom"),"MHW"),
             Game("DOTA 2", "2011", "8.8", Image(R.drawable.dota.toString(),R.drawable.dota.toString()), dota2Trailer, "Valve’s legendary competitive MOBA title.", false, "game_dota2", listOf("PC"), "Valve", listOf("MOBA"), listOf("Fantasy"),  listOf("DOTA"), listOf("Valve"),"Defense of the Ancients 2"),
             Game("League of Legends", "2012", "0.0", Image(R.drawable.lol.toString(),R.drawable.lol.toString()), lolTrailer, "The world’s biggest MOBA with global competitive scenes.", false, "game_lol", listOf("PC"), "Riot Games", listOf("MOBA"), listOf("Fantasy"),  listOf("League Universe"),listOf("Riot Games"), "LoL"),
             Game("Counter Strike 2", "2023", "6.6", Image(R.drawable.cs2.toString(),R.drawable.cs2.toString()), cs2Trailer, "The next evolution of the iconic tactical shooter.", false, "game_cs2", listOf("PC"), "Valve", listOf("Shooter", "Tactical"), listOf("Modern Warfare"),  listOf("Counter-Strike"), listOf("Valve"),"CS2"),
             Game("God of War: Ragnarok", "2022", "9.9", Image(R.drawable.gowrag.toString(),R.drawable.gowrag.toString()), gowTrailer, "The epic Norse saga of Kratos and Atreus continues.", false, "game_gowrag", listOf("PS4", "PS5"), "Santa Monica Studio", listOf("Action", "Adventure"), listOf("Mythology"),  listOf("God of War"),listOf("Sony Interactive Entertainment"), "GOW: Ragnarok"),
             Game("Valorant", "2020", "5.5", Image(R.drawable.valo.toString(),R.drawable.valo.toString()), valoTrailer, "A 5v5 tactical shooter with unique agent abilities.", false, "game_valorant", listOf("PC"), "Riot Games", listOf("Shooter", "Tactical"), listOf("Futuristic"),  listOf("Valorant"),listOf("Riot Games"), "Valo"),
             Game("Elden Ring", "2018", "10.0", Image(R.drawable.eldenring.toString(),R.drawable.eldenring.toString()), eldenRingTrailer, "A dark fantasy masterpiece from FromSoftware and George R.R. Martin.", false, "game_eldenring", listOf("PS4", "PS5", "Xbox", "PC"), "FromSoftware", listOf("RPG", "Soulslike"), listOf("Dark Fantasy"),  listOf("Elden Ring"),listOf("Bandai Namco"), "ER")
         )


        filteredList = listOfGame.toMutableList()

        // Initialize Adapter
        arrayAdapter = GameListAdapter(
            requireContext(),
            filteredList,
            onClickMore = { game -> more(game) },
            onClickItem = { game ->
                // Do not add the same game again, just highlight/select it
                moreWGlide(game)
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
                searchView.clearFocus()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!newText.isNullOrEmpty()) {
                    fetchSearchedGames(newText) // api call
                    listView.visibility = View.VISIBLE
                } else {
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

    private fun fetchSearchedGames(query: String) {
        val apiKey = BuildConfig.GIANT_BOMB_API_KEY

        ApiClient.api.searchGames(apiKey = apiKey, query = query)
            .enqueue(object : Callback<SearchResponse> {
                override fun onResponse(call: Call<SearchResponse>, response: Response<SearchResponse>) {
                    if (response.isSuccessful) {
                        val games = response.body()?.results ?: emptyList()

                        filteredList.clear()
                        filteredList.addAll(games.map { it.toGame() }) // 👈 map API models to your Game model if needed
                        arrayAdapter.notifyDataSetChanged()
                    } else {
                        // handle empty case
                        filteredList.clear()
                        arrayAdapter.notifyDataSetChanged()
                    }
                }

                override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                    // handle error
                    filteredList.clear()
                    arrayAdapter.notifyDataSetChanged()
                }
            })
    }
    // continue this



    @SuppressLint("NotifyDataSetChanged")
    private fun fetchRandomGames() {
        val apiKey = BuildConfig.GIANT_BOMB_API_KEY
        val randomOffset = (0..100).random()// for randomizing games
        ApiClient.api.getGames(apiKey, offset = randomOffset)
            .enqueueGameList(listOfRandomGames) {
                randomGamesGameameAdapter.notifyDataSetChanged()
                isRandomLoaded = true
                checkIfDataLoaded()
            }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fetchPs4PlatformGames() {
        val apiKey = BuildConfig.GIANT_BOMB_API_KEY
        val randomOffset = (0..100).random()// for randomizing games
        ApiClient.api.getGamesByPlatform(apiKey,offset = randomOffset, filter = "platforms:146")
            .enqueueGameList(listOfPs4Games) {
                ps4GamesGameAdapter.notifyDataSetChanged()
                isps4GamesLoaded = true
                checkIfDataLoaded()
            }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fetchXboxOnePlatformGames() {
        val apiKey = BuildConfig.GIANT_BOMB_API_KEY
        val randomOffset = (0..100).random()// for randomizing games
        ApiClient.api.getGamesByPlatform(apiKey,offset = randomOffset, filter = "platforms:145")
            .enqueueGameList(listOfXbox1Games) {
                xbox1GamesGameAdapter.notifyDataSetChanged()
                isxbox1GamesLoaded = true
                checkIfDataLoaded()
            }
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun fetchPcPlatformGames() {
        val apiKey = BuildConfig.GIANT_BOMB_API_KEY
        val randomOffset = (0..100).random()// for randomizing games
        ApiClient.api.getGamesByPlatform(apiKey,offset = randomOffset, filter = "platforms:94")
            .enqueueGameList(listOfPcGames) {
                pcGamesGameAdapter.notifyDataSetChanged()
                ispcGamesLoaded = true
                checkIfDataLoaded()
            }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fetchNintendoSwitchPlatformsGames() {
        val apiKey = BuildConfig.GIANT_BOMB_API_KEY
        val randomOffset = (0..100).random()// for randomizing games
        ApiClient.api.getGamesByPlatform(apiKey,offset = randomOffset, filter = "platforms:157")
            .enqueueGameList(listOfNintendoSwitchGames) {
                nintendoSwitchGamesGameAdapter.notifyDataSetChanged()
                isnintendoSwitchLoaded = true
                checkIfDataLoaded()
            }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fetchMobilePlatformGames() {
        val apiKey = BuildConfig.GIANT_BOMB_API_KEY
        val randomOffset = (0..100).random()// for randomizing games
        ApiClient.api.getGamesByPlatform(apiKey,offset = randomOffset, filter = "platforms:34")
            .enqueueGameList(listOfMobileGames) {
                mobileGamesGameAdapter.notifyDataSetChanged()
                ismobileGamesLoaded = true
                checkIfDataLoaded()
            }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fetchHorrorThemeGames() {
        val apiKey = BuildConfig.GIANT_BOMB_API_KEY
        val randomOffset = (0..100).random()// for randomizing games
        ApiClient.api.getGamesByTheme(apiKey, offset = randomOffset,filter ="themes:1")
            .enqueueGameList(listOfHorroGames) {
                horrorGamesGameAdapter.notifyDataSetChanged()
                ishorroGamesLoaded= true
                checkIfDataLoaded()
            }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fetchFantasyThemeGames() {
        val apiKey = BuildConfig.GIANT_BOMB_API_KEY
        val randomOffset = (0..100).random()// for randomizing games
        ApiClient.api.getGamesByTheme(apiKey, offset = randomOffset,filter ="themes:17")
            .enqueueGameList(listOfFantasyGames) {
                fantasyGamesGameAdapter.notifyDataSetChanged()
                isfantasyGamesLoaded= true
                checkIfDataLoaded()
            }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fetchSciFiThemeGames() {
        val apiKey = BuildConfig.GIANT_BOMB_API_KEY
        val randomOffset = (0..100).random()// for randomizing games
        ApiClient.api.getGamesByTheme(apiKey, offset = randomOffset,filter ="themes:18")
            .enqueueGameList(listOfSciFiGames) {
                sciFiGamesGameAdapter.notifyDataSetChanged()
                issciFiGamesLoaded= true
                checkIfDataLoaded()
            }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fetchAdventureThemeGames() {
        val apiKey = BuildConfig.GIANT_BOMB_API_KEY
        val randomOffset = (0..100).random()// for randomizing games
        ApiClient.api.getGamesByTheme(apiKey, offset = randomOffset,filter ="themes:22")
            .enqueueGameList(listOfAdventureGames) {
                adventureGamesGameAdapter.notifyDataSetChanged()
                isadventureGamesLoaded= true
                checkIfDataLoaded()
            }
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun fetchRomanceThemeGames() {
        val apiKey = BuildConfig.GIANT_BOMB_API_KEY
        val randomOffset = (0..100).random()// for randomizing games
        ApiClient.api.getGamesByTheme(apiKey, offset = randomOffset,filter ="themes:33")
            .enqueueGameList(listOfRomanceGames) {
                romanceGamesGameAdapter.notifyDataSetChanged()
                isromanceGamesLoaded= true
                checkIfDataLoaded()
            }
    }

    // Modify the function to directly fetch game details using the GUID
    private fun fetchPopularReviews() {
        val apiKey = BuildConfig.GIANT_BOMB_API_KEY
        val call = ApiClient.api.getPopularReviews(apiKey)
        call.enqueue(object : Callback<ReviewListResponse> {
            override fun onResponse(call: Call<ReviewListResponse>, response: Response<ReviewListResponse>) {
                if (response.isSuccessful) {
                    val reviews = response.body()?.results

                    // Start coroutine scope for spacing out requests
                    CoroutineScope(Dispatchers.IO).launch {
                        reviews?.forEachIndexed { index, review ->
                            val gameGuid = review.game?.site_detail_url?.extractGuidFromUrl()
                            if (gameGuid != null) {
                                fetchGameDetails(gameGuid)
                                delay(1500L) // Wait 1 second before next request
                            }
                        }
                    }
                } else {
                    Log.e("ApiError", "Error: ${response.errorBody()}")
                }
            }
            override fun onFailure(call: Call<ReviewListResponse>, t: Throwable) {
                Log.e("ApiError", "Failure: ${t.message}")
            }
        })
    }

    // there is an error in here which when u go to another fragment and go back to this fragment it will esxsit
    // Modify the function to fetch game details directly for a single GUID
    fun fetchGameDetails(guid: String) {
        // ✅ Step 1: Check if it's already in the cache
        val cachedGame = AppCache.gameCache[guid]
        if (cachedGame != null) {
            Log.d("CACHE", "Game already cached: ${cachedGame.name}")
            listOfHighRatedGames.add(cachedGame)
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                highRatedGamesGameAdapter.notifyDataSetChanged()
                isHighRatedLoaded = true
                checkIfDataLoaded()
            }
            return
        }

        val apiKey = BuildConfig.GIANT_BOMB_API_KEY
        val call = ApiClient.api.getGameByGuid(guid, apiKey)
        call.enqueue(object : Callback<SingleGameResponse> {
            override fun onResponse(call: Call<SingleGameResponse>, response: Response<SingleGameResponse>) {
                if (!isAdded || view == null) return //  early exit if fragment is gone

                val game = response.body()?.results
                if (response.isSuccessful && game != null) {
                    val gameObj = Game(
                        guid = guid,
                        name = game.name ?: "Unknown Game",
                        date = game.original_release_date ?: "Unknown Date",
                        rating = game.original_game_rating?.joinToString { it.name } ?: "N/A",
                        photo = game.image,
                        platform = game.platforms?.map { it.name } ?: emptyList(),
                        genre = game.genres?.map { it.name } ?: emptyList(),
                        theme = game.themes?.map { it.name } ?: emptyList(),
                        franchise = game.franchises?.map { it.name } ?: emptyList(),
                        publishers = game.publishers?.map { it.name } ?: emptyList(),
                        developer = game.developers?.joinToString { it.name } ?: "Unknown",
                        alias = game.aliases ?: "None"
                    )

                    AppCache.gameCache[guid] = gameObj
                    listOfHighRatedGames.add(gameObj)

                    // ✅ Only touch views on safe thread & lifecycle
                    if (isAdded && view != null) {
                        highRatedGamesGameAdapter.notifyDataSetChanged()
                        isHighRatedLoaded = true
                        checkIfDataLoaded()
                    }
                } else {
                    Log.w("DETAILS", "Failed response or null body for GUID: $guid")
                }
            }
            override fun onFailure(call: Call<SingleGameResponse>, t: Throwable) {
                Log.e("DETAILS", "Network failure for GUID $guid: ${t.message}")
            }
        })
    }


    private fun filterList(query: String?) {
        filteredList.clear()
        if (query.isNullOrEmpty()) {
            filteredList.addAll(listOfGame) // Show all if search is empty
        } else {
            val searchQuery = query.lowercase()
            filteredList.addAll(listOfGame.filter {
                //added things here
                it.name.lowercase().contains(searchQuery) || it.date?.contains(searchQuery) ?: false
            })
        }
        arrayAdapter.notifyDataSetChanged()

        // Show listView only when there are resultsa
        listView.visibility = if (filteredList.isNotEmpty()) View.VISIBLE else View.GONE
    }

    private fun more(game: Game) {
        startActivity(
            Intent(requireContext(), reviewPageActivity::class.java).apply {
                putExtra("title", game.name)
                putExtra("date",game.date)
                putExtra("imageRes", game.photo?.medium_url)
                putExtra("trailer", game.gameTrailer)
                putExtra("ratings", game.rating)
                putExtra("desc", game.description)
                putExtra("isLiked", game.isLiked)
                putExtra("platform", game.platform?.joinToString(", ") ?: "N/A")
                putExtra("developer", game.developer ?: "N/A")
                putExtra("publishers", game.publishers?.joinToString(", ") ?: "N/A")
                putExtra("genre", game.genre?.joinToString(", ") ?: "N/A")
                putExtra("theme", game.theme?.joinToString(", ") ?: "N/A")
                putExtra("franchise", game.franchise?.joinToString(", ") ?: "N/A")
                putExtra("alias", game.alias ?: "N/A")
            }
        )
    }


    private fun moreWGlide(game: Game) {
        startActivity(
            Intent(requireContext(), reviewPageActivity::class.java).apply {
                putExtra("guid",game.guid)
            }
        )
    }
    // for shimmers
    @SuppressLint("NotifyDataSetChanged")
    private fun checkIfDataLoaded() {
        if (isHighRatedLoaded || isRandomLoaded || isps4GamesLoaded || isxbox1GamesLoaded
            || ismobileGamesLoaded || isnintendoSwitchLoaded || ispcGamesLoaded || ishorroGamesLoaded
            || issciFiGamesLoaded || isfantasyGamesLoaded || isadventureGamesLoaded || isromanceGamesLoaded
        ) {
            requireActivity().runOnUiThread {
                val cool = view?.findViewById<LinearLayout>(R.id.realContent)

                shimmerLayout.stopShimmer()
                shimmerLayout.visibility = View.GONE

                hrgRecyclerView.visibility = View.VISIBLE
                rgRecyclerView.visibility = View.VISIBLE
                recyclerView.visibility = View.VISIBLE
//                ps4RecyclerView.visibility = View.VISIBLE
//                xbox1RecyclerView.visibility = View.VISIBLE
//                pcRecyclerView.visibility = View.VISIBLE
//                nintendoSwitchRecyclerView.visibility = View.VISIBLE
//                mobileRecyclerView.visibility = View.VISIBLE
//                horrorRecyclerView.visibility = View.VISIBLE
//                fantasyRecyclerView.visibility = View.VISIBLE
//                adventureRecyclerView.visibility = View.VISIBLE
//                sciFiRecyclerView.visibility = View.VISIBLE
//                romanceRecyclerView.visibility = View.VISIBLE

                cool?.visibility = View.VISIBLE

                // notify all the adapters
                arrayAdapter.notifyDataSetChanged()
                randomGamesGameameAdapter.notifyDataSetChanged()
                highRatedGamesGameAdapter.notifyDataSetChanged()
//                ps4GamesGameAdapter.notifyDataSetChanged()
//                xbox1GamesGameAdapter.notifyDataSetChanged()
//                nintendoSwitchGamesGameAdapter.notifyDataSetChanged()
//                pcGamesGameAdapter.notifyDataSetChanged()
//                mobileGamesGameAdapter.notifyDataSetChanged()
//                horrorGamesGameAdapter.notifyDataSetChanged()
//                fantasyGamesGameAdapter.notifyDataSetChanged()
//                sciFiGamesGameAdapter.notifyDataSetChanged()
//                romanceGamesGameAdapter.notifyDataSetChanged()
//                adventureGamesGameAdapter.notifyDataSetChanged()

                Log.d("SHIMMER", "All data loaded—shimmer stopped")
            }
        }
    }

    private fun abc(game: Game){

    }

    private fun showDeleteDialog(position: Int) {
        // Implement delete logic
    }
}