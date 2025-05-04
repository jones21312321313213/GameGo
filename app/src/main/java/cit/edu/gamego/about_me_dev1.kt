package cit.edu.gamego

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cit.edu.gamego.data.Game
import cit.edu.gamego.data.Image
import cit.edu.gamego.extensions.moreWithGlide
import cit.edu.gamego.helper.GameRecyclerViewAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop

class about_me_dev1 : Activity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_me_dev1);


        val abmetv = findViewById<TextView>(R.id.abmetv1)
        val pic = findViewById<ImageView>(R.id.pic)

        Glide.with(this)
            .load(R.drawable.archi)  // Replace with the actual image URL or resource ID
            .transform(CircleCrop())  // Apply CircleCrop transformation
            .into(pic)

        val message = """
        Hello! I am Shane Nathan B. Archival, one of the developers of this app.I was born on November 9, 2004,and I am currently 20 years old. I live in Cebu City,Philippines, and I am currently studying at Cebu Institute of Technology - University (CIT-U), pursuing a Bachelor's degree in Computer Science.
    """.trimIndent()

        abmetv.text = message;
        val bmwTrailer = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/pnSsgRJmsCc?si=Fy9aZVKwThO7lKAi\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>".trimIndent()
        val valorantTrailer = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/watch?v=lWr6dhTcu-E\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>".trimIndent()
        val overwatchTrailer = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/watch?v=GKXS_YA9s7E\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>".trimIndent()
        val gowTrailer = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/hfJ4Km46A-0?si=baY8Yfl9Zer1BSCn\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>".trimIndent()
        val eldenRingTrailer = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/AKXiKBnzpBQ?si=zBuJ8VqG7Y5gjWOU\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>".trimIndent()

        val listOfGame = listOf(
            Game("Valorant", "2023","6.6", Image(R.drawable.valo.toString(),""),valorantTrailer,"",false,"3030-77445"),
            Game("Overwatch 2", "2011","8.8", Image(R.drawable.overwatch2.toString(),""),overwatchTrailer,"",false,"3030-75883"),
            Game("Black Myth Wukong", "2024","9.3", Image(R.drawable.bmw.toString(),""),bmwTrailer,"",false,"3030-80279"),
            Game("God of War: Ragnarock", "2018","9.9", Image(R.drawable.gowrag.toString(),""),gowTrailer,"",false,"3030-80643"),
            Game("Elden Ring", "2018","10.0", Image(R.drawable.eldenring.toString(),""),eldenRingTrailer,"",false,"3030-79403")
        )

        val recyclerView = findViewById<RecyclerView>(R.id.horizontalRecyclerView)

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false)

        recyclerView.adapter = GameRecyclerViewAdapter(
            this,
            listOfGame,
            onClick = {game ->
                moreWithGlide(game)
            }, isAlternativeLayout = true
        )


        val back = findViewById<ImageView>(R.id.abme_dev1_back);

        back.setOnClickListener{
            finish()
        }
    }
}