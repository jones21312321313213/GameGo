package cit.edu.gamego

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import cit.edu.gamego.data.Game
import androidx.recyclerview.widget.RecyclerView
import cit.edu.gamego.data.Image
import cit.edu.gamego.helper.GameRecyclerViewAdapter
import cit.edu.gamego.helper.GameRecyclerViewAdapterwGlide


class about_me_dev2 : Activity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_me_dev2);


        val abmetv = findViewById<TextView>(R.id.abmetv2)

        val message = """
        Hello! I am Joshua D. Arco, one of the developers of this app.I was born on July 15 2004,and I am currently 20 years old. I live in Naga City,Cebu,Philippines, and I am currently studying at Cebu Institute of Technology - University (CIT-U), pursuing a Bachelor's degree in Computer Science.
    """.trimIndent()

        abmetv.text = message;
        val bmwTrailer = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/pnSsgRJmsCc?si=Fy9aZVKwThO7lKAi\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>".trimIndent()
        val dota2Trailer = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/-cSFPIwMEq4?si=snIX76ATWr7M80fI\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>".trimIndent()
        val cs2Trailer = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/nSE38xjMLqE?si=NrrEFt0Up-TpVYpJ\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>".trimIndent()
        val gowTrailer = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/hfJ4Km46A-0?si=baY8Yfl9Zer1BSCn\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>".trimIndent()
        val eldenRingTrailer = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/AKXiKBnzpBQ?si=zBuJ8VqG7Y5gjWOU\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>".trimIndent()

        val listOfGame = listOf(
            Game("Counter Strike 2", "2023","6.6", Image(R.drawable.cs2.toString()),cs2Trailer),
            Game("DOTA 2", "2011","8.8", Image(R.drawable.dota.toString()),dota2Trailer),
            Game("Black Myth Wukong", "2024","9.3", Image(R.drawable.bmw.toString()),bmwTrailer),
            Game("God of War: Ragnarock", "2018","9.9", Image(R.drawable.gowrag.toString()),gowTrailer),
            Game("Elden Ring", "2018","10.0", Image(R.drawable.eldenring.toString()),eldenRingTrailer)
        )


        val recyclerView = findViewById<RecyclerView>(R.id.horizontalRecyclerView)

        recyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)

        recyclerView.adapter = GameRecyclerViewAdapter(
            this,
            listOfGame,
            onClick = {game ->
                startActivity(
                    Intent(this,reviewPageActivity::class.java).apply{
                        putExtra("title",game.name)
                        putExtra("imageRes",game.photo?.medium_url)
                        putExtra("ratings",game.rating)
                        putExtra("trailer",game.gameTrailer)
                    }
                )
            }, isAlternativeLayout = true
        )


        val back = findViewById<ImageView>(R.id.abme_dev2_back);

        back.setOnClickListener{
            finish()
        }
    }
}