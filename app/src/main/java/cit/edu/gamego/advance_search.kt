package cit.edu.gamego

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import cit.edu.gamego.data.ApiClient
import cit.edu.gamego.data.FilterChipItem
import cit.edu.gamego.data.Game
import cit.edu.gamego.data.GameApiResponse
import cit.edu.gamego.databinding.FragmentAdvanceSearchBinding
import cit.edu.gamego.extensions.moreWithGlideFragment
import cit.edu.gamego.helper.GameListAdapter
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class advance_search : Fragment() {
    private val listOfSearchedGames = mutableListOf<Game>()
    private lateinit var arrayAdapter: GameListAdapter
    private var b: FragmentAdvanceSearchBinding? = null
    private val binding get() = b!!

    private val selectedFilters = mutableSetOf<String>()
    private var pendingApiCalls = 0

    private val genreChips = listOf(
        FilterChipItem("Action", 1, "3060-1"),
        FilterChipItem("Action-Adventure", 43, "3060-43"),
        FilterChipItem("MMORPG", 16, "3060-16"),
        FilterChipItem("First Person Shooter", 32, "3060-32"),
        FilterChipItem("Platformer", 41, "3060-41"),
        FilterChipItem("Fighting", 9, "3060-9"),
        FilterChipItem("Driving/Racing", 6, "3060-6"),
        FilterChipItem("Sports", 3, "3060-3"),
        FilterChipItem("Simulation", 7, "3060-7"),
        FilterChipItem("Strategy", 2, "3060-2"),
        FilterChipItem("Puzzle", 18, "3060-18"),
        FilterChipItem("Role Playing", 5, "3060-5"),
        FilterChipItem("Shooter", 11, "3060-11"),
        FilterChipItem("Gambling", 49, "3060-49"),
        FilterChipItem("MOBA", 50, "3060-50")
    )

    private val themeChips = listOf(
        FilterChipItem("Horror", 1, "3032-1"),
        FilterChipItem("Fantasy", 2, "3032-2"),
        FilterChipItem("Sci-fi", 3, "3032-3"),
        FilterChipItem("Dating", 4, "3032-4"),
        FilterChipItem("Comedy", 6, "3032-6"),
        FilterChipItem("WWII", 7, "3032-7"),
        FilterChipItem("Espionage", 11, "3032-11"),
        FilterChipItem("Modern Military", 12, "3032-12"),
        FilterChipItem("Crime", 13, "3032-13"),
        FilterChipItem("Anime", 16, "3032-16"),
        FilterChipItem("Martial Arts", 17, "3032-17"),
        FilterChipItem("Superhero", 19, "3032-19"),
        FilterChipItem("Medieval", 27, "3032-27"),
        FilterChipItem("Prehistoric", 28, "3032-28"),
        FilterChipItem("Aquatic", 31, "3032-31")
    )

    private val platformChips = listOf(
        FilterChipItem("PC (Windows)", 94, "3045-94"),
        FilterChipItem("Linux", 152, "3045-152"),
        FilterChipItem("PS4", 146, "3045-146"),
        FilterChipItem("Xbox One", 145, "3045-145"),
        FilterChipItem("Nintendo Switch", 157, "3045-157"),
        FilterChipItem("NES", 21, "3045-21"),
        FilterChipItem("PS3", 35, "3045-35"),
        FilterChipItem("Xbox 360", 20, "3045-20"),
        FilterChipItem("Nintendo Wii", 36, "3045-36"),
        FilterChipItem("Android", 123, "3045-123"),
        FilterChipItem("iPhone", 96, "3045-96"),
        FilterChipItem("iPad", 121, "3045-121"),
        FilterChipItem("PS Vita", 129, "3045-129"),
        FilterChipItem("Nintendo GameCube", 23, "3045-23"),
        FilterChipItem("Mac (Apple)", 17, "3045-17")
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        b = FragmentAdvanceSearchBinding.inflate(inflater, container, false)

        setupToggle(binding.genreTitle, binding.genreChipGroup)
        setupToggle(binding.themeTitle, binding.themeChipGroup)
        setupToggle(binding.platformTitle, binding.platformChipGroup)

        createChips(genreChips, binding.genreChipGroup)
        createChips(themeChips, binding.themeChipGroup)
        createChips(platformChips, binding.platformChipGroup)

        arrayAdapter = GameListAdapter(
            requireContext(),
            listOfSearchedGames,
            onClickMore = { game -> moreWithGlideFragment(game) },
            onClickItem = { game -> moreWithGlideFragment(game) },
            onLongPress = { }
        )
        binding.listView.adapter = arrayAdapter

        binding.searchButton.setOnClickListener {
            val filterQuery = buildFilterQuery()
            if (filterQuery.isNotEmpty()) {
                fetchSearchedGames(filterQuery)
            } else {
                listOfSearchedGames.clear()
                arrayAdapter.notifyDataSetChanged()
                Log.d("AdvanceSearch", "No filters selected, no search performed")
            }
        }

        return binding.root
    }

    private fun setupToggle(titleView: TextView, chipGroup: ChipGroup) {
        titleView.setOnClickListener {
            if (chipGroup.visibility == View.VISIBLE) {
                chipGroup.animate().alpha(0f).setDuration(200).withEndAction {
                    chipGroup.visibility = View.GONE
                }
                titleView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_down, 0)
            } else {
                chipGroup.alpha = 0f
                chipGroup.visibility = View.VISIBLE
                chipGroup.animate().alpha(1f).setDuration(200)
                titleView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_up, 0)
            }
        }
    }

    private fun createChips(items: List<FilterChipItem>, chipGroup: ChipGroup) {
        for (item in items) {
            val chip = Chip(requireContext()).apply {
                text = item.name
                isCheckable = true
                tag = item
                setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) selectedFilters.add(item.guid) else selectedFilters.remove(item.guid)
                }
            }
            chipGroup.addView(chip)
        }
    }

    private fun buildFilterQuery(): String {
        val genreFilters = genreChips.filter { selectedFilters.contains(it.guid) }.map { "genres:${it.id}" }
        val themeFilters = themeChips.filter { selectedFilters.contains(it.guid) }.map { "themes:${it.id}" }
        val platformFilters = platformChips.filter { selectedFilters.contains(it.guid) }.map { "platforms:${it.id}" }
        return (genreFilters + themeFilters + platformFilters).joinToString(",")
    }

    private fun fetchSearchedGames(filter: String) {
        binding.progressBar.visibility = View.VISIBLE
        binding.listView.visibility = View.GONE
        listOfSearchedGames.clear()
        arrayAdapter.notifyDataSetChanged()

        val filters = filter.split(",")
        val platformFilters = filters.filter { it.startsWith("platforms:") }
        val themeFilters = filters.filter { it.startsWith("themes:") }
        val genreFilters = filters.filter { it.startsWith("genres:") }

        pendingApiCalls = platformFilters.size + themeFilters.size + genreFilters.size

        if (pendingApiCalls == 0) {
            binding.progressBar.visibility = View.GONE
            binding.listView.visibility = View.VISIBLE
            return
        }

        // Clear previous results when starting new search
        listOfSearchedGames.clear()
        arrayAdapter.notifyDataSetChanged()

        // For debugging - log the actual filters being sent
        Log.d("AdvanceSearch", "Platform filters: $platformFilters")
        Log.d("AdvanceSearch", "Theme filters: $themeFilters")
        Log.d("AdvanceSearch", "Genre filters: $genreFilters")

        // Make sure we're using the correct filter format
        platformFilters.forEach { filterStr ->
            val platformId = filterStr.substringAfter("platforms:").toIntOrNull() ?: return@forEach
            val call = ApiClient.api.getGamesByPlatform(
                apiKey = BuildConfig.GIANT_BOMB_API_KEY,
                filter = "platforms:$platformId",
                limit = 15
            )
            Log.d("AdvanceSearch", "Making platform call: ${call.request()}")
            call.enqueue(createApiCallback("Platform"))
        }

        themeFilters.forEach { filterStr ->
            val themeId = filterStr.substringAfter("themes:").toIntOrNull() ?: return@forEach
            val call = ApiClient.api.getGamesByTheme(
                apiKey = BuildConfig.GIANT_BOMB_API_KEY,
                filter = "themes:$themeId",
                limit = 15
            )
            Log.d("AdvanceSearch", "Making theme call: ${call.request()}")
            call.enqueue(createApiCallback("Theme"))
        }

        genreFilters.forEach { filterStr ->
            val genreId = filterStr.substringAfter("genres:").toIntOrNull() ?: return@forEach
            val call = ApiClient.api.getGamesByGenre(
                apiKey = BuildConfig.GIANT_BOMB_API_KEY,
                filter = "genres:$genreId",
                limit = 15
            )
            Log.d("AdvanceSearch", "Making genre call: ${call.request()}")
            call.enqueue(createApiCallback("Genre"))
        }
    }

    private fun createApiCallback(filterType: String): Callback<GameApiResponse> {
        return object : Callback<GameApiResponse> {
            override fun onResponse(call: Call<GameApiResponse>, response: Response<GameApiResponse>) {
                pendingApiCalls--
                if (response.isSuccessful) {
                    val games = response.body()?.results ?: emptyList()
                    Log.d("AdvanceSearch", "Received ${games.size} $filterType games")

                    val newGames = games.mapNotNull { gameResult ->
                        try {
                            Game(
                                guid = gameResult.guid ?: "",
                                name = gameResult.name?.toString() ?: "Unknown",
                                photo = gameResult.image
                            )
                        } catch (e: Exception) {
                            Log.e("AdvanceSearch", "Error mapping game: ${e.message}")
                            null
                        }
                    }

                    newGames.forEach { newGame ->
                        if (!listOfSearchedGames.any { it.guid == newGame.guid }) {
                            listOfSearchedGames.add(newGame)
                        }
                    }
                } else {
                    Log.e("AdvanceSearch", "$filterType API Error: ${response.code()} - ${response.errorBody()?.string()}")
                }

                if (pendingApiCalls == 0) {
                    updateUIAfterFetch()
                }
            }

            override fun onFailure(call: Call<GameApiResponse>, t: Throwable) {
                pendingApiCalls--
                Log.e("AdvanceSearch", "$filterType API Failed: ${t.message}")
                if (pendingApiCalls == 0) {
                    updateUIAfterFetch()
                }
            }
        }
    }

    private fun updateUIAfterFetch() {
        requireActivity().runOnUiThread {
            binding.progressBar.visibility = View.GONE
            arrayAdapter.notifyDataSetChanged()
            binding.listView.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        b = null
    }
}