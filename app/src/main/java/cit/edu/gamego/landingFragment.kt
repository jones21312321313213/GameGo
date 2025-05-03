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
    private lateinit var randomGamesGameameAdapter: GameRecyclerViewAdapterwGlide
    private lateinit var highRatedGamesGameAdapter: GameRecyclerViewAdapterwGlide
    private lateinit var ps4GamesGameAdapter: GameRecyclerViewAdapterwGlide
    private lateinit var xbox1GamesGameAdapter: GameRecyclerViewAdapterwGlide
    private lateinit var pcGamesGameAdapter: GameRecyclerViewAdapterwGlide
    private lateinit var nintendoSwitchGamesGameAdapter: GameRecyclerViewAdapterwGlide
    private lateinit var mobileGamesGameAdapter: GameRecyclerViewAdapterwGlide


    private lateinit var listOfGame: MutableList<Game>
    private val listOfRandomGames = mutableListOf<Game>()
    private val listOfHighRatedGames = mutableListOf<Game>()
    private val listOfPs4Games = mutableListOf<Game>()
    private val listOfXbox1Games = mutableListOf<Game>()
    private val listOfPcGames = mutableListOf<Game>()
    private val listOfNintendoSwitchGames = mutableListOf<Game>()
    private val listOfMobileGames = mutableListOf<Game>()


    private var isxbox1GamesLoaded =false
    private var isps4GamesLoaded = false
    private var isHighRatedLoaded = false
    private var isRandomLoaded = false
    private var isnintendoSwitchLoaded = false
    private var ispcGamesLoaded = false
    private var ismobileGamesLoaded = false



    private val isGameLoaded = BooleanArray(12)


    private lateinit var recyclerView: RecyclerView
    private lateinit var hrgRecyclerView: RecyclerView
    private lateinit var rgRecyclerView: RecyclerView
    private lateinit var ps4RecyclerView: RecyclerView
    private lateinit var xbox1RecyclerView: RecyclerView
    private lateinit var nintendoSwitchRecyclerView: RecyclerView
    private lateinit var pcRecyclerView: RecyclerView
    private lateinit var mobileRecyclerView: RecyclerView

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
         rgRecyclerView = view.findViewById(R.id.randomGamesRecyclerView)
         hrgRecyclerView = view.findViewById(R.id.highRatedGamesRecyclerView)
         ps4RecyclerView = view.findViewById(R.id.ps4GamesRecyclerView)
         xbox1RecyclerView = view.findViewById(R.id.xboxOneGamesRecyclerView)
         nintendoSwitchRecyclerView = view.findViewById(R.id.nintendoSwitchGamesRecyclerView)
         pcRecyclerView = view.findViewById(R.id.pcGamesRecyclerView)
         mobileRecyclerView = view.findViewById(R.id.mobileGamesRecyclerView)

        // setting up the recycler viewers

         recyclerView.layoutManager = LinearLayoutManager(requireContext())
         hrgRecyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
         rgRecyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
         ps4RecyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
         xbox1RecyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
         nintendoSwitchRecyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
         pcRecyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
         mobileRecyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)


        // setting up the  adapters
        randomGamesGameameAdapter = GameRecyclerViewAdapterwGlide(
            requireContext(),
            listOfRandomGames,
            onClick ={game->
                moreWithGlideFragment(game)
            }
        )

        rgRecyclerView.adapter = randomGamesGameameAdapter

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



        // fetching games using API
        // go back to here
        fetchAllGames()
        swipeRefreshLayout.setOnRefreshListener {
            // Call your API to fetch new data
            fetchAllGames()

            // Once data is fetched and UI is updated, hide the refresh indicator
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
                if (!newText.isNullOrEmpty()) {
                    progressBar.visibility = View.VISIBLE
                    listView.visibility = View.GONE
                    fetchSearchedGames(newText) // Fetch search results
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
//       fetchRandomGames()
          fetchPopularReviews()
//        fetchPs4PlatformGames()
//        fetchXboxOnePlatformGames()
//        fetchPcPlatformGames()
//        fetchNintendoSwitchPlatformsGames()
//        fetchMobilePlatformGames()

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

                        // Hide ProgressBar and show ListView after data is fetched
                        progressBar.visibility = View.GONE
                        listView.visibility = View.VISIBLE // Show ListView with results
                    } else {
                        // Handle empty case or failed response
                        filteredList.clear()
                        arrayAdapter.notifyDataSetChanged()

                        // Hide ProgressBar and show ListView if no results
                        progressBar.visibility = View.GONE
                        listView.visibility = View.VISIBLE // You may still want to show an empty list here
                    }
                }

                override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                    // Handle error (e.g., network failure)
                    filteredList.clear()
                    arrayAdapter.notifyDataSetChanged()

                    // Hide ProgressBar and show ListView if there's an error
                    progressBar.visibility = View.GONE
                    listView.visibility = View.VISIBLE // You may want to show a message instead of an empty list
                }
            })
    }
    //in the search try to make it that at least half of the images loads before showing



    @SuppressLint("NotifyDataSetChanged")
    private fun fetchRandomGames() {
        val apiKey = BuildConfig.GIANT_BOMB_API_KEY
        val randomOffset = (0..100).random()// for randomizing games
        ApiClient.api.getGames(apiKey, offset = randomOffset)
            .enqueueGameList(viewLifecycleOwner,listOfRandomGames) {
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
        if (isHighRatedLoaded || isRandomLoaded || isps4GamesLoaded || isxbox1GamesLoaded
            || ismobileGamesLoaded || isnintendoSwitchLoaded || ispcGamesLoaded
        ) {
            requireActivity().runOnUiThread {
                val cool = view?.findViewById<LinearLayout>(R.id.realContent)

                shimmerLayout.stopShimmer()
                shimmerLayout.visibility = View.GONE

                hrgRecyclerView.visibility = View.VISIBLE
                rgRecyclerView.visibility = View.VISIBLE
                recyclerView.visibility = View.VISIBLE
//
//                ps4RecyclerView.visibility = View.VISIBLE
//                xbox1RecyclerView.visibility = View.VISIBLE
//                pcRecyclerView.visibility = View.VISIBLE
//                nintendoSwitchRecyclerView.visibility = View.VISIBLE
//                mobileRecyclerView.visibility = View.VISIBLE


                cool?.visibility = View.VISIBLE

                // notify all the adapters
                arrayAdapter.notifyDataSetChanged()
//                randomGamesGameameAdapter.notifyDataSetChanged()
                highRatedGamesGameAdapter.notifyDataSetChanged()
//                ps4GamesGameAdapter.notifyDataSetChanged()
//                xbox1GamesGameAdapter.notifyDataSetChanged()
//                nintendoSwitchGamesGameAdapter.notifyDataSetChanged()
//                pcGamesGameAdapter.notifyDataSetChanged()
//                mobileGamesGameAdapter.notifyDataSetChanged()

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