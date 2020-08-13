package siva.app.quotemaker.ui.activities

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.setMargins
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.saved_quotes.*
import siva.app.quotemaker.R
import siva.app.quotemaker.controls.util.Util
import siva.app.quotemaker.controls.util.Util.Companion.setFullScreen
import java.io.File

/*
* Created by Siva Nimmala on 13/8/20.
* */
class SavedQuotes : AppCompatActivity() {

    private var quotes:Array<File>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFullScreen(this)
        setContentView(R.layout.saved_quotes)

        initQuotes()

        ivBack.setOnClickListener { startActivity(Intent(this, Dashboard::class.java));finish() }

        quotes = initQuotes()

        if (quotes == null) {
            tvNoQuotes.visibility = VISIBLE
        }else{
            rvQuotes.layoutManager = GridLayoutManager(this, 3)
            rvQuotes.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder?>() {
                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                    return object : RecyclerView.ViewHolder(ImageView(this@SavedQuotes)){}
                }

                override fun getItemCount(): Int {
                    return quotes!!.size
                }

                override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                    val params = ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                    params.setMargins(20)
                    holder.itemView.layoutParams = params

                    holder.itemView.setOnClickListener {
                        val openImageIntent = Intent(Intent.ACTION_VIEW)
                        openImageIntent.setDataAndType(Util.buildFileProviderUri(this@SavedQuotes, quotes!![position]), "image/*")
                        openImageIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        startActivity(openImageIntent)
                    }
                    Glide.with(this@SavedQuotes).load(quotes!![position]).into(holder.itemView as ImageView)
                }
            }
        }
    }

    private fun initQuotes(): Array<File>? {
        val quotesPath = File(Environment.getExternalStorageDirectory(), "/Quote_Maker/Quotes")
        if (quotesPath.exists()) {
            return quotesPath.listFiles { _, name -> name.endsWith(".png") }
        }
        return null
    }
}