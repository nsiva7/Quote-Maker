package siva.app.quotemaker.controls.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ja.burhanrashid52.photoeditor.PhotoFilter
import siva.app.quotemaker.controls.listeners.FilterListener
import siva.app.quotemaker.controls.util.Util
import siva.app.quotemaker.databinding.RowFilterViewBinding

/*
* Created by Siva Nimmala on 12/8/20.
* */
class FiltersAdapter(private val filterListener: FilterListener) : RecyclerView.Adapter<FiltersAdapter.FiltersHolder>() {

    private var photoFiltersPair: MutableList<Pair<String, PhotoFilter>> = mutableListOf(
        Pair("filters/original.jpg", PhotoFilter.NONE),
        Pair("filters/auto_fix.png", PhotoFilter.AUTO_FIX),
        Pair("filters/brightness.png", PhotoFilter.BRIGHTNESS),
        Pair("filters/contrast.png", PhotoFilter.CONTRAST),
        Pair("filters/documentary.png", PhotoFilter.DOCUMENTARY),
        Pair("filters/dual_tone.png", PhotoFilter.DUE_TONE),
        Pair("filters/fill_light.png", PhotoFilter.FILL_LIGHT),
        Pair("filters/fish_eye.png", PhotoFilter.FISH_EYE),
        Pair("filters/grain.png", PhotoFilter.GRAIN),
        Pair("filters/gray_scale.png", PhotoFilter.GRAY_SCALE),
        Pair("filters/lomish.png", PhotoFilter.LOMISH),
        Pair("filters/negative.png", PhotoFilter.NEGATIVE),
        Pair("filters/posterize.png", PhotoFilter.POSTERIZE),
        Pair("filters/saturate.png", PhotoFilter.SATURATE),
        Pair("filters/sepia.png", PhotoFilter.SEPIA),
        Pair("filters/sharpen.png", PhotoFilter.SHARPEN),
        Pair("filters/temprature.png", PhotoFilter.TEMPERATURE),
        Pair("filters/tint.png", PhotoFilter.TINT),
        Pair("filters/vignette.png", PhotoFilter.VIGNETTE),
        Pair("filters/cross_process.png", PhotoFilter.CROSS_PROCESS),
        Pair("filters/b_n_w.png", PhotoFilter.BLACK_WHITE),
        Pair("filters/flip_horizental.png", PhotoFilter.FLIP_HORIZONTAL),
        Pair("filters/flip_vertical.png", PhotoFilter.FLIP_VERTICAL),
        Pair("filters/rotate.png", PhotoFilter.ROTATE)
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FiltersHolder {
        return FiltersHolder(RowFilterViewBinding.inflate(LayoutInflater.from(parent.context), parent, false), filterListener, photoFiltersPair)
    }

    override fun getItemCount(): Int {
        return photoFiltersPair.size
    }

    override fun onBindViewHolder(holder: FiltersHolder, position: Int) {
        holder.filterViewBinding.ivFilter.setImageBitmap(Util.getBitmapFromAsset(holder.itemView.context, photoFiltersPair[position].first))
        holder.filterViewBinding.tvFilter.text = photoFiltersPair[position].second.name.replace("_", " ")
    }

    class FiltersHolder(
        var filterViewBinding: RowFilterViewBinding,
        filterListener: FilterListener,
        photoFiltersPair: MutableList<Pair<String, PhotoFilter>>
    ) : RecyclerView.ViewHolder(filterViewBinding.root) {
        init {
            filterViewBinding.ivFilter.setOnClickListener { filterListener.onSelectFilter(photoFiltersPair[adapterPosition].second) }
        }
    }
}