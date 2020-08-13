package siva.app.quotemaker.controls.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import siva.app.quotemaker.R
import siva.app.quotemaker.controls.listeners.ColorPickerListener
import siva.app.quotemaker.databinding.ColorPickerItemListBinding

/*
* Created by Siva Nimmala on 12/8/20.
* */
class ColorPickerAdapter(context: Context, private val colorPickerListener: ColorPickerListener) :RecyclerView.Adapter<ColorPickerAdapter.ColorPickerHolder>() {

    private val colors = mutableListOf(
        ContextCompat.getColor(context, R.color.blue_color_picker),
        ContextCompat.getColor(context, R.color.brown_color_picker),
        ContextCompat.getColor(context, R.color.green_color_picker),
        ContextCompat.getColor(context, R.color.orange_color_picker),
        ContextCompat.getColor(context, R.color.red_color_picker),
        ContextCompat.getColor(context, R.color.black),
        ContextCompat.getColor(context, R.color.red_orange_color_picker),
        ContextCompat.getColor(context, R.color.sky_blue_color_picker),
        ContextCompat.getColor(context, R.color.violet_color_picker),
        ContextCompat.getColor(context, R.color.white),
        ContextCompat.getColor(context, R.color.yellow_color_picker),
        ContextCompat.getColor(context, R.color.yellow_green_color_picker)
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorPickerHolder {
        return ColorPickerHolder(ColorPickerItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false), colorPickerListener, colors)
    }

    override fun getItemCount(): Int {
        return colors.size
    }

    override fun onBindViewHolder(holder: ColorPickerHolder, position: Int) {
        holder.colorPickerItemListBinding.colorPickerView.setBackgroundColor(colors[position])
    }

    class ColorPickerHolder(var colorPickerItemListBinding: ColorPickerItemListBinding, colorPickerListener: ColorPickerListener, colors:List<Int>):RecyclerView.ViewHolder(colorPickerItemListBinding.root){
        init {
            itemView.setOnClickListener { colorPickerListener.onColorPicked(colors[adapterPosition]) }
        }
    }
}