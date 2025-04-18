package cit.edu.gamego

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cit.edu.gamego.data.ApiClient
import cit.edu.gamego.data.AppCache
import cit.edu.gamego.data.Game
import cit.edu.gamego.data.SingleGameResponse
import cit.edu.gamego.extensions.extractGuidFromUrl
import cit.edu.gamego.helper.GameRecyclerViewAdapter
import cit.edu.gamego.helper.GameRecyclerViewAdapterwGlide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class GameSimilarGamesFragment : Fragment() {
    private val listOfSimilarGames = mutableListOf<Game>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: GameRecyclerViewAdapterwGlide
    private val MAX_SIMILAR_GAMES = 5
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_game_similar_games, container, false)
        recyclerView = view.findViewById(R.id.horizontalRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        adapter = GameRecyclerViewAdapterwGlide(
            requireContext(),
            listOfSimilarGames,
            onClick = { game ->
                startActivity(
                    Intent(requireContext(), reviewPageActivity::class.java).apply {
                        putExtra("guid", game.guid)
                    }
                )
                requireActivity().finish()
            },
            isAlternativeLayout = true
        )

        recyclerView.adapter = adapter

        val similarGames: List<String> =
            arguments?.getString("similarGames")?.split(",")?.filter { it.isNotBlank() } ?: emptyList()

        Log.d("TestGame", "Extracted URLs: $similarGames")

        similarGames.forEach { gameUrl ->
            val guid = gameUrl.extractGuidFromUrl()
            Log.d("SimilarGameGUID", "Extracted GUID: $guid")
            if (guid != null) {
                if (guid.isNotEmpty()) {
                    fetchGameDetails(guid)
                }
            }
        }
        return view
    }

    private fun fetchGameDetails(guid: String) {
        if(listOfSimilarGames.size >= MAX_SIMILAR_GAMES) return

        val parentGameGuid = arguments?.getString("parentGameGuid") // Make sure this is passed in arguments
        // 💾 Check gameCache first
        AppCache.gameCache[guid]?.let { cachedGame ->
            Log.d("CACHE", "Loaded from game cache: ${cachedGame.name}")
            listOfSimilarGames.add(cachedGame)
            adapter.notifyItemInserted(listOfSimilarGames.size - 1)

            // Optionally cache this in similarGamesCache under parent
            if (parentGameGuid != null) {
                val updatedList = (AppCache.similarGamesCache[parentGameGuid] ?: emptyList()) + cachedGame
                AppCache.similarGamesCache[parentGameGuid] = updatedList
            }
            return
        }
        // 🔑 If not cached, fetch from API
        val apiKey = BuildConfig.GIANT_BOMB_API_KEY
        val call = ApiClient.api.getGameByGuid(guid, apiKey)

        call.enqueue(object : Callback<SingleGameResponse> {
            override fun onResponse(call: Call<SingleGameResponse>, response: Response<SingleGameResponse>) {
                if (response.isSuccessful) {
                    val game = response.body()?.results
                    Log.d("DETAILS", "Fetched game: ${game?.name}")
                    val gameObj = Game(
                        guid = guid,
                        name = game?.name ?: "Unknown Game",
                        date = game?.original_release_date ?: "Unknown Date",
                        rating = game?.original_game_rating?.joinToString { r -> r.name } ?: "N/A",
                        photo = game?.image,
                        platform = game?.platforms?.map { p -> p.name } ?: emptyList(),
                        genre = game?.genres?.map { g -> g.name } ?: emptyList(),
                        theme = game?.themes?.map { t -> t.name } ?: emptyList(),
                        franchise = game?.franchises?.map { f -> f.name } ?: emptyList(),
                        publishers = game?.publishers?.map { p -> p.name } ?: emptyList(),
                        developer = game?.developers?.joinToString { d -> d.name } ?: "Unknown",
                        alias = game?.aliases ?: "None",
                    )

                    AppCache.gameCache[guid] = gameObj
                    listOfSimilarGames.add(gameObj)
                    adapter.notifyItemInserted(listOfSimilarGames.size - 1)
                    // 📦 Also update similar games cache under the parent
                    if (parentGameGuid != null) {
                        val updatedList = (AppCache.similarGamesCache[parentGameGuid] ?: emptyList()) + gameObj
                        AppCache.similarGamesCache[parentGameGuid] = updatedList
                    }
                } else {
                    Log.e("DETAILS", "Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<SingleGameResponse>, t: Throwable) {
                Log.e("DETAILS", "Failure: ${t.message}")
            }
        })
    }


}