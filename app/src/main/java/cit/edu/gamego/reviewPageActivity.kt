package cit.edu.gamego

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.app.Activity
import android.content.Intent
import android.widget.ImageView
import android.widget.TextView
import cit.edu.gamego.data.Game

class reviewPageActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review_page)

        val gameImg = findViewById<ImageView>(R.id.game_pic_rp)
        val gameTitle  = findViewById<TextView>(R.id.game_title_rp)
        val gameRev = findViewById<TextView>(R.id.review_content)
        val back = findViewById<ImageView>(R.id.back_rp)
        val a = null

        intent?.let {
            it.getStringExtra("title")?.let { title ->
                gameTitle.text = title
            }
            it.getIntExtra("imageRes",0).let{imageResId->
                if (imageResId != 0) {
                    gameImg.setImageResource(imageResId)
                }
            }

        }

        when (gameTitle.toString().lowercase()) {
            "the ye quest" -> {
                ("This game will explore how kanye likes hitler and make it " +
                        "his inspiration to make good music").also { gameRev.text = it }
            }
            "black myth wukong 2024" -> {
                "An action role-playing game where players control a monkey on a quest to " +
                        "retrieve various relics. Inspired by the classic Chinese novel " +
                        (" Journey to the West the game features a fantasy" +
                                " world filled with mythical creatures and " +
                                "dynamic combat, allowing players to wield a staff " +
                                "nd magic spells against enemies.").also{gameRev.text = it}
            }
            "monster hunter: world 2018" ->{
                    ("In this installment of the Monster Hunter series," +
                            " players embark on the ultimate hunting experience, " +
                            "utilizing everything at their disposal to hunt monsters " +
                            "in a new world teeming with surprises and excitement. " +
                            "The game offers expansive environments and cooperative play," +
                            " allowing hunters to explore diverse ecosystems and battle formidable creatures.").also{gameRev.text = it}
            }
            "helldivers 2 2022" ->{
                    ("A cooperative third-person shooter set in the 22nd century, " +
                            "where players become part of the Helldivers," +
                            " an elite force tasked with defending Super Earth against various alien threats, " +
                            "including the Terminids and Automatons. The game emphasizes teamwork " +
                            "and strategic combat across hostile galaxies").also{gameRev.text = it}
            }
            else -> {
                gameRev.text = "REVIEW"
            }
        }



        back.setOnClickListener{
            finish()
        }

        val heart = findViewById<ImageView>(R.id.heart)

        val imageResId = gameImg.tag as? Int ?: R.drawable.aaa

        heart.setOnClickListener {
            startActivity(
                Intent(this, Favorites::class.java).apply{
                    putExtra("title",gameTitle.text.toString())
                    putExtra("releaseDate","21")
                    putExtra("imageRes",imageResId)
                }
            )
        }
    }
}