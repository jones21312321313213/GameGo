package cit.edu.gamego

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import cit.edu.gamego.data.ApiClient
import cit.edu.gamego.data.FilterChipItem
import cit.edu.gamego.data.Game
import cit.edu.gamego.data.GameApiResponse
import cit.edu.gamego.databinding.FragmentAdvanceSearchBinding
import cit.edu.gamego.extensions.enqueueGameList
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
                fetchSearchedGames(filterQuery,viewLifecycleOwner)
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

    @SuppressLint("NotifyDataSetChanged")
    private fun fetchSearchedGames(filter: String, lifecycleOwner: LifecycleOwner) {
        binding.progressBar.visibility = View.VISIBLE
        binding.listView.visibility = View.GONE
        listOfSearchedGames.clear()
        arrayAdapter.notifyDataSetChanged()

        val apiKey = BuildConfig.GIANT_BOMB_API_KEY
        val randomOffset = (0..100).random()

        Log.d("AdvancedSearch", "Filter applied: $filter")  // Debugging the filter applied

        var apiUrl: String

        when {
            filter.startsWith("platforms:") -> {
                // Extracting platform IDs, e.g., "platforms:1,platforms:2"
                val platformFilters = filter.split(",")
                platformFilters.forEach { platformFilter ->
                    val platformId = platformFilter.substringAfter("platforms:").toIntOrNull()
                    if (platformId != null) {
                        apiUrl = "https://www.giantbomb.com/api/games/?api_key=$apiKey&offset=$randomOffset&filter=platforms:$platformId"
                        Log.d("AdvancedSearch", "Requesting platform games: $apiUrl") // Log the URL

                        ApiClient.api.getGamesByPlatform(
                            apiKey = apiKey,
                            offset = randomOffset,
                            filter = "platforms:$platformId"
                        ).enqueueGameList(lifecycleOwner, listOfSearchedGames) {
                            handleApiResponse()
                        }
                    }
                }
            }

            filter.startsWith("genres:") -> {
                // Extracting genre IDs, e.g., "genres:1,genres:2"
                val genreFilters = filter.split(",")
                genreFilters.forEach { genreFilter ->
                    val genreId = genreFilter.substringAfter("genres:").toIntOrNull()
                    if (genreId != null) {
                        apiUrl = "https://www.giantbomb.com/api/games/?api_key=$apiKey&offset=$randomOffset&filter=genres:$genreId"
                        Log.d("AdvancedSearch", "Requesting genre games: $apiUrl") // Log the URL

                        ApiClient.api.getGamesByGenre(
                            apiKey = apiKey,
                            offset = randomOffset,
                            filter = "genres:$genreId"
                        ).enqueueGameList(lifecycleOwner, listOfSearchedGames) {
                            handleApiResponse()
                        }
                    }
                }
            }

            filter.startsWith("themes:") -> {
                // Extracting theme IDs, e.g., "themes:1,themes:2"
                val themeFilters = filter.split(",")
                themeFilters.forEach { themeFilter ->
                    val themeId = themeFilter.substringAfter("themes:").toIntOrNull()
                    if (themeId != null) {
                        apiUrl = "https://www.giantbomb.com/api/games/?api_key=$apiKey&offset=$randomOffset&filter=themes:$themeId"
                        Log.d("AdvancedSearch", "Requesting theme games: $apiUrl") // Log the URL

                        ApiClient.api.getGamesByTheme(
                            apiKey = apiKey,
                            offset = randomOffset,
                            filter = "themes:$themeId"
                        ).enqueueGameList(lifecycleOwner, listOfSearchedGames) {
                            handleApiResponse()
                        }
                    }
                }
            }

            else -> {
                Log.e("fetchSearchedGames", "Unsupported filter format: $filter")
                binding.progressBar.visibility = View.GONE
            }
        }
    }


    private fun buildFilterQuery(): String {
        val genreFilters = genreChips.filter { selectedFilters.contains(it.guid) }.map { "genres:${it.id}" }
        val themeFilters = themeChips.filter { selectedFilters.contains(it.guid) }.map { "themes:${it.id}" }
        val platformFilters = platformChips.filter { selectedFilters.contains(it.guid) }.map { "platforms:${it.id}" }

        val filterQuery = (genreFilters + themeFilters + platformFilters).joinToString(",")
        Log.d("AdvancedSearch", "Generated filter query: $filterQuery")  // Debugging the generated filter query
        return filterQuery
    }




    private fun handleApiResponse() {
        // Check if no games were found
        if (listOfSearchedGames.isEmpty()) {
            Toast.makeText(
                context,
                "No games found for the selected filter(s)",
                Toast.LENGTH_SHORT
            ).show()
        }
        arrayAdapter.notifyDataSetChanged()
        binding.progressBar.visibility = View.GONE
        binding.listView.visibility = View.VISIBLE
    }


    override fun onDestroyView() {
        super.onDestroyView()
        b = null
    }
}