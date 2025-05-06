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
import android.widget.ProgressBar
import android.widget.SearchView // Use this for the correct SearchView
import androidx.lifecycle.Lifecycle

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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import cit.edu.gamego.data.GameResult
import cit.edu.gamego.data.GiantBombApi
import cit.edu.gamego.data.SearchResponse
import cit.edu.gamego.extensions.moreWithGlideFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class landingFragment : Fragment() {
    private lateinit var filteredList: MutableList<Game>
    private lateinit var searchView: SearchView
    private lateinit var listView: ListView
    private lateinit var shimmerLayout: ShimmerFrameLayout
    private lateinit var progressBar: ProgressBar

    private lateinit var arrayAdapter: GameListAdapter
    private lateinit var highRatedGamesGameAdapter: GameRecyclerViewAdapterwGlide
    private lateinit var ps4GamesGameAdapter: GameRecyclerViewAdapterwGlide
    private lateinit var xbox1GamesGameAdapter: GameRecyclerViewAdapterwGlide
    private lateinit var pcGamesGameAdapter: GameRecyclerViewAdapterwGlide
    private lateinit var nintendoSwitchGamesGameAdapter: GameRecyclerViewAdapterwGlide
    private lateinit var mobileGamesGameAdapter: GameRecyclerViewAdapterwGlide

    private lateinit var ps3GamesGameAdapter: GameRecyclerViewAdapterwGlide
    private lateinit var nesGamesGameAdapter: GameRecyclerViewAdapterwGlide
    private lateinit var xbox360GameAdapter: GameRecyclerViewAdapterwGlide

    private lateinit var listOfGame: MutableList<Game>
    private val listOfHighRatedGames = mutableListOf<Game>()
    private val listOfPs4Games = mutableListOf<Game>()
    private val listOfXbox1Games = mutableListOf<Game>()
    private val listOfPcGames = mutableListOf<Game>()
    private val listOfNintendoSwitchGames = mutableListOf<Game>()
    private val listOfMobileGames = mutableListOf<Game>()

    private val listOfNESGames = mutableListOf<Game>()
    private val listOfXbox360Games = mutableListOf<Game>()
    private val listOfPs3Games = mutableListOf<Game>()


    private var isxbox1GamesLoaded =false
    private var isps4GamesLoaded = false
    private var isHighRatedLoaded = false
    private var isnintendoSwitchLoaded = false
    private var ispcGamesLoaded = false
    private var ismobileGamesLoaded = false

    private var isps3GamesLoaded = false
    private var isNESGamesLoaded = false
    private var isxbox360GamesLoaded = false



    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private var searchJob: Job? = null

    private lateinit var recyclerView: RecyclerView
    private lateinit var hrgRecyclerView: RecyclerView
    private lateinit var rgRecyclerView: RecyclerView
    private lateinit var ps4RecyclerView: RecyclerView
    private lateinit var xbox1RecyclerView: RecyclerView
    private lateinit var nintendoSwitchRecyclerView: RecyclerView
    private lateinit var pcRecyclerView: RecyclerView
    private lateinit var mobileRecyclerView: RecyclerView

    private lateinit var xbo360RecyclerView: RecyclerView
    private lateinit var ps3RecyclerView: RecyclerView
    private lateinit var nesRecyclerView: RecyclerView

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
        val swipeRefreshLayout = view.findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout)


        /////


        // finding the recycler view in the layout
         recyclerView =view.findViewById(R.id.recyclerview)
         hrgRecyclerView = view.findViewById(R.id.highRatedGamesRecyclerView)
         ps4RecyclerView = view.findViewById(R.id.ps4GamesRecyclerView)
         xbox1RecyclerView = view.findViewById(R.id.xboxOneGamesRecyclerView)
         nintendoSwitchRecyclerView = view.findViewById(R.id.nintendoSwitchGamesRecyclerView)
         pcRecyclerView = view.findViewById(R.id.pcGamesRecyclerView)
         mobileRecyclerView = view.findViewById(R.id.mobileGamesRecyclerView)
         xbo360RecyclerView =  view.findViewById(R.id.xbox360GamesRecyclerView)
         ps3RecyclerView = view.findViewById(R.id.ps3GamesRecyclerView)
         nesRecyclerView =  view.findViewById(R.id.nesGamesRecyclerView)
        // setting up the recycler viewers

         recyclerView.layoutManager = LinearLayoutManager(requireContext())
         hrgRecyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
         ps4RecyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
         xbox1RecyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
         nintendoSwitchRecyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
         pcRecyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
         mobileRecyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
         xbo360RecyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
         nesRecyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
         ps3RecyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)

        // setting up the  adapters



        highRatedGamesGameAdapter = GameRecyclerViewAdapterwGlide(
            requireContext(),
            listOfHighRatedGames,
            onClick ={game->
                moreWithGlideFragment(game)
            }
        )
        hrgRecyclerView.adapter = highRatedGamesGameAdapter

        ps4GamesGameAdapter = GameRecyclerViewAdapterwGlide(
            requireContext(),
            listOfPs4Games,
            onClick ={game->
                moreWithGlideFragment(game)
            }
        )

        ps4RecyclerView.adapter = ps4GamesGameAdapter

        xbox1GamesGameAdapter = GameRecyclerViewAdapterwGlide(
            requireContext(),
            listOfXbox1Games,
            onClick ={game->
                moreWithGlideFragment(game)
            }
        )

        xbox1RecyclerView.adapter = xbox1GamesGameAdapter

        nintendoSwitchGamesGameAdapter = GameRecyclerViewAdapterwGlide(
            requireContext(),
            listOfNintendoSwitchGames,
            onClick ={game->
                moreWithGlideFragment(game)
            }
        )

        nintendoSwitchRecyclerView.adapter = nintendoSwitchGamesGameAdapter

        pcGamesGameAdapter = GameRecyclerViewAdapterwGlide(
            requireContext(),
            listOfPcGames,
            onClick ={game->
                moreWithGlideFragment(game)
            }
        )

        pcRecyclerView.adapter = pcGamesGameAdapter

        mobileGamesGameAdapter = GameRecyclerViewAdapterwGlide(
            requireContext(),
            listOfMobileGames,
            onClick ={game->
                moreWithGlideFragment(game)
            }
        )

        mobileRecyclerView.adapter = mobileGamesGameAdapter


        ps3GamesGameAdapter = GameRecyclerViewAdapterwGlide(
            requireContext(),
            listOfPs3Games,
            onClick ={game->
                moreWithGlideFragment(game)
            }
        )

        ps3RecyclerView.adapter = ps3GamesGameAdapter



        xbox360GameAdapter = GameRecyclerViewAdapterwGlide(
            requireContext(),
            listOfXbox360Games,
            onClick ={game->
                moreWithGlideFragment(game)
            }
        )

        xbo360RecyclerView.adapter = xbox360GameAdapter

        nesGamesGameAdapter = GameRecyclerViewAdapterwGlide(
            requireContext(),
            listOfNESGames,
            onClick ={game->
                moreWithGlideFragment(game)
            }
        )

       nesRecyclerView.adapter = nesGamesGameAdapter



        // fetching games using API

        viewLifecycleOwner.lifecycleScope.launch {
            fetchAllGames()
        }

        // when swiping up it will reset shimmer and hide recyucler views
        swipeRefreshLayout.setOnRefreshListener {
            resetShimmerAndReload()
            // Stop the refresh icon after a short delay (optional)
            swipeRefreshLayout.isRefreshing = false
        }


        listOfGame = mutableListOf(
            Game("YE Quest", "2030", "1.1", Image(R.drawable.ye.toString(), R.drawable.ye.toString()), " ", "The visionary's journey through a surreal rap universe.", false, "game_yequest", listOf("PS5", "PC"), "Yeezy Interactive", listOf("Adventure", "Rhythm"), listOf("Hip-Hop", "Satire"), listOf("YE Series"),listOf("Yeezy Productions"), "YQ")
        )

        ////////////////////////////////////// FOr search bar which is a list view
        // Initially hide listView
        listView.visibility = View.GONE

         progressBar = view.findViewById(R.id.progressBar)
        filteredList = listOfGame.toMutableList()

        // Initialize Adapter
        arrayAdapter = GameListAdapter(
            requireContext(),
            filteredList,
            onClickMore = { game -> more(game) },
            onClickItem = { game ->

                moreWithGlideFragment(game)
            },
            onLongPress = { position -> showDeleteDialog(position) }
        )

        listView.adapter = arrayAdapter


        // Show ProgressBar while searching
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchJob?.cancel()
                if (!newText.isNullOrEmpty()) {

                    listView.visibility = View.GONE

                    searchJob = coroutineScope.launch {
                        delay(500) // Wait for 2 seconds
                        progressBar.visibility = View.VISIBLE
                        fetchSearchedGames(newText)
                    }
                } else {
                    listView.visibility = View.GONE
                    progressBar.visibility = View.GONE
                }
                return true
            }
        })

        searchView.setOnCloseListener {
            listView.visibility = View.GONE
            progressBar.visibility = View.GONE
            false
        }

        return view;
    }

    private fun fetchAllGames(){
        fetchPopularReviews()
        fetchPs4PlatformGames()
        fetchXboxOnePlatformGames()
        fetchPcPlatformGames()
        fetchNintendoSwitchPlatformsGames()
        fetchMobilePlatformGames()

        fetchNESPlatformGames()
        fetchXbox360PlatformGames()
        fetchPs3PlatformGames()

    }

    private fun fetchSearchedGames(query: String) {
        val apiKey = BuildConfig.GIANT_BOMB_API_KEY

        ApiClient.api.searchGames(apiKey = apiKey, query = query)
            .enqueue(object : Callback<SearchResponse> {
                override fun onResponse(call: Call<SearchResponse>, response: Response<SearchResponse>) {
                    if (response.isSuccessful) {
                        val games = response.body()?.results ?: emptyList()
                        filteredList.clear()
                        filteredList.addAll(games.map { it.toGame() })
                        arrayAdapter.notifyDataSetChanged()

                        progressBar.visibility = View.GONE
                        listView.visibility = View.VISIBLE
                    } else {
                        filteredList.clear()
                        arrayAdapter.notifyDataSetChanged()

                        progressBar.visibility = View.GONE
                        listView.visibility = View.VISIBLE
                    }
                }

                override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                    filteredList.clear()
                    arrayAdapter.notifyDataSetChanged()

                    progressBar.visibility = View.GONE
                    listView.visibility = View.VISIBLE
                }
            })
    }
    //in the search try to make it that at least half of the images loads before showing







    @SuppressLint("NotifyDataSetChanged")
    private fun fetchPs4PlatformGames() {
            // where to find the GIANT_BOMB_API_KEY or store the API KEY? IN the local properties this applies to all API keys in the code
            val apiKey = BuildConfig.GIANT_BOMB_API_KEY
            val randomOffset = (0..100).random()// for randomizing games
            ApiClient.api.getGamesByPlatform(apiKey,offset = randomOffset, filter = "platforms:146")
                .enqueueGameList(viewLifecycleOwner,listOfPs4Games) {
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
            .enqueueGameList(viewLifecycleOwner,listOfXbox1Games) {
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
            .enqueueGameList(viewLifecycleOwner,listOfPcGames) {
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
            .enqueueGameList(viewLifecycleOwner,listOfNintendoSwitchGames) {
                nintendoSwitchGamesGameAdapter.notifyDataSetChanged()
                isnintendoSwitchLoaded = true
                checkIfDataLoaded()
            }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fetchMobilePlatformGames() {
        val apiKey = BuildConfig.GIANT_BOMB_API_KEY
        val randomOffset = (0..100).random()// for randomizing games
        ApiClient.api.getGamesByPlatform(apiKey,offset = randomOffset, filter = "platforms:123")
            .enqueueGameList(viewLifecycleOwner,listOfMobileGames) {
                mobileGamesGameAdapter.notifyDataSetChanged()
                ismobileGamesLoaded = true
                checkIfDataLoaded()
            }
    }





    @SuppressLint("NotifyDataSetChanged")
    private fun fetchNESPlatformGames() {
        val apiKey = BuildConfig.GIANT_BOMB_API_KEY
        val randomOffset = (0..100).random()// for randomizing games
        ApiClient.api.getGamesByPlatform(apiKey,offset = randomOffset, filter = "platforms:21")
            .enqueueGameList(viewLifecycleOwner,listOfNESGames) {
                nesGamesGameAdapter.notifyDataSetChanged()
                isNESGamesLoaded = true
                checkIfDataLoaded()
            }
    }



    @SuppressLint("NotifyDataSetChanged")
    private fun fetchPs3PlatformGames() {
        val apiKey = BuildConfig.GIANT_BOMB_API_KEY
        val randomOffset = (0..100).random()// for randomizing games
        ApiClient.api.getGamesByPlatform(apiKey,offset = randomOffset, filter = "platforms:35")
            .enqueueGameList(viewLifecycleOwner,listOfPs3Games) {
                ps3GamesGameAdapter.notifyDataSetChanged()
                isps3GamesLoaded = true
                checkIfDataLoaded()
            }
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun fetchXbox360PlatformGames() {
        val apiKey = BuildConfig.GIANT_BOMB_API_KEY
        val randomOffset = (0..100).random()// for randomizing games
        ApiClient.api.getGamesByPlatform(apiKey,offset = randomOffset, filter = "platforms:20")
            .enqueueGameList(viewLifecycleOwner,listOfXbox360Games) {
                xbox360GameAdapter.notifyDataSetChanged()
                isxbox360GamesLoaded = true
                checkIfDataLoaded()
            }
    }



    // Modify the function to directly fetch game details using the GUID
    private fun fetchPopularReviews() {
        val apiKey = BuildConfig.GIANT_BOMB_API_KEY
        val randomOffset = (0..100).random()// for randomizing games
        val call = ApiClient.api.getPopularReviews(apiKey,offset = randomOffset)
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
        val cachedGame = AppCache.gameCache[guid]
        if (cachedGame != null) {
            Log.d("CACHE", "Game already cached: ${cachedGame.name}")
            listOfHighRatedGames.add(cachedGame)

            // Check if fragment is still alive and has a view
            if (!isAdded || view == null) return

            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                if (!isAdded || view == null) return@launchWhenStarted
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
                if (!isAdded || view == null) return // Early exit if fragment is gone

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

                    // Check again before updating UI
                    if (!isAdded || view == null) return

                    viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                        if (!isAdded || view == null) return@launchWhenStarted
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


    // for shimmers
    @SuppressLint("NotifyDataSetChanged")
    private fun checkIfDataLoaded() {
        if (isHighRatedLoaded  || isps4GamesLoaded || isxbox1GamesLoaded
            || ismobileGamesLoaded ||  isnintendoSwitchLoaded || ispcGamesLoaded || isNESGamesLoaded || isxbox360GamesLoaded || isps3GamesLoaded
        ) {
            requireActivity().runOnUiThread {
                val cool = view?.findViewById<LinearLayout>(R.id.realContent)

                shimmerLayout.stopShimmer()
                shimmerLayout.visibility = View.GONE

                hrgRecyclerView.visibility = View.VISIBLE
                recyclerView.visibility = View.VISIBLE

                ps4RecyclerView.visibility = View.VISIBLE
                xbox1RecyclerView.visibility = View.VISIBLE
                pcRecyclerView.visibility = View.VISIBLE
                nintendoSwitchRecyclerView.visibility = View.VISIBLE
                mobileRecyclerView.visibility = View.VISIBLE
                nesRecyclerView.visibility = View.VISIBLE
                xbo360RecyclerView.visibility = View.VISIBLE
                ps3RecyclerView.visibility = View.VISIBLE


                cool?.visibility = View.VISIBLE

                // notify  adapters
                arrayAdapter.notifyDataSetChanged()
                highRatedGamesGameAdapter.notifyDataSetChanged()
                ps4GamesGameAdapter.notifyDataSetChanged()
                xbox1GamesGameAdapter.notifyDataSetChanged()
                nintendoSwitchGamesGameAdapter.notifyDataSetChanged()
                pcGamesGameAdapter.notifyDataSetChanged()
                mobileGamesGameAdapter.notifyDataSetChanged()

                nesGamesGameAdapter.notifyDataSetChanged()
                xbox360GameAdapter.notifyDataSetChanged()
                ps3GamesGameAdapter.notifyDataSetChanged()

                Log.d("SHIMMER", "All data loaded—shimmer stopped")
            }
        }
    }


    private fun resetShimmerAndReload() {
        // Reset all loading flags
        isHighRatedLoaded = false
        isps4GamesLoaded = false
        isxbox1GamesLoaded = false
        ismobileGamesLoaded = false
        isnintendoSwitchLoaded = false
        ispcGamesLoaded = false
        isNESGamesLoaded = false
        isps3GamesLoaded = false
        isxbox360GamesLoaded = false


        hrgRecyclerView.visibility = View.GONE
        recyclerView.visibility = View.GONE
         ps4RecyclerView.visibility = View.GONE
         xbox1RecyclerView.visibility = View.GONE
         mobileRecyclerView.visibility = View.GONE
         nintendoSwitchRecyclerView.visibility = View.GONE
         pcRecyclerView.visibility = View.GONE
        nesRecyclerView.visibility = View.GONE
        xbo360RecyclerView.visibility = View.GONE
        ps3RecyclerView.visibility = View.GONE

        // Show shimmer
        shimmerLayout.visibility = View.VISIBLE
        shimmerLayout.startShimmer()

        //  hiding  the real content container
        view?.findViewById<LinearLayout>(R.id.realContent)?.visibility = View.GONE

        // w8 3sec b4 refetching
        viewLifecycleOwner.lifecycleScope.launch {
            delay(3000L)
            if (!isAdded || view == null) return@launch
            fetchAllGames()
        }

    }


    private fun abc(game: Game){

    }

    private fun showDeleteDialog(position: Int) {
        // Implement delete logic
    }
}