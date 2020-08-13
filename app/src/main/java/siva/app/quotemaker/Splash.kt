package siva.app.quotemaker

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.splash.*
import siva.app.quotemaker.controls.util.Util
import siva.app.quotemaker.ui.activities.Dashboard

/*
* Created by Siva Nimmala on 11/8/20.
* */
class Splash : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Util.setFullScreen(this)
        setContentView(R.layout.splash)

        ivSplash.postDelayed({
            startActivity(Intent(applicationContext, Dashboard::class.java))
            finish()
        }, 1000)
    }
}