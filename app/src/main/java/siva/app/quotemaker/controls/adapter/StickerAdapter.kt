package siva.app.quotemaker.controls.adapter

import android.content.res.Resources
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import siva.app.quotemaker.R
import siva.app.quotemaker.controls.listeners.StickerListener
import siva.app.quotemaker.databinding.RowStickerBinding

/*
* Created by Siva Nimmala on 12/8/20.
* */
class StickerAdapter(private val stickerListener: StickerListener, private val resources: Resources):RecyclerView.Adapter<StickerAdapter.StickerHolder>() {

    private val stickers = arrayListOf(R.drawable.aa, R.drawable.bb)

    class StickerHolder(var rowStickerBinding: RowStickerBinding, stickerListener: StickerListener, resources: Resources, stickers: List<Int>) :
        RecyclerView.ViewHolder(rowStickerBinding.root) {

        init {
            rowStickerBinding.ivSticker.setOnClickListener {
                stickerListener.onStickerSelected(
                    BitmapFactory.decodeResource(
                        resources,
                        stickers[adapterPosition]
                    )
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StickerHolder {
        return StickerHolder(RowStickerBinding.inflate(LayoutInflater.from(parent.context), parent, false), stickerListener, resources, stickers)
    }

    override fun getItemCount(): Int {
        return stickers.size
    }

    override fun onBindViewHolder(holder: StickerHolder, position: Int) {
        holder.rowStickerBinding.ivSticker.setImageResource(stickers[position])
    }
}