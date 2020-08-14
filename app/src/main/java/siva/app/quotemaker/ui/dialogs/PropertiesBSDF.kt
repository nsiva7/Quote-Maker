package siva.app.quotemaker.ui.dialogs

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import siva.app.quotemaker.R
import siva.app.quotemaker.controls.adapter.ColorPickerAdapter
import siva.app.quotemaker.controls.listeners.ColorPickerListener
import siva.app.quotemaker.controls.listeners.PropertyListener
import siva.app.quotemaker.databinding.FragPropertiesBsdfBinding

/*
* Created by Siva Nimmala on 12/8/20.
* */
class PropertiesBSDF(private val brushColor: Int, private val brushSize: Float, var propertyListener: PropertyListener) : BottomSheetDialogFragment(),
    SeekBar.OnSeekBarChangeListener {

    private lateinit var fragPropertiesBsdfBinding: FragPropertiesBsdfBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragPropertiesBsdfBinding.inflate(layoutInflater).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragPropertiesBsdfBinding = FragPropertiesBsdfBinding.bind(view)

        fragPropertiesBsdfBinding.sbOpacity.setOnSeekBarChangeListener(this)
        fragPropertiesBsdfBinding.sbSize.setOnSeekBarChangeListener(this)

        fragPropertiesBsdfBinding.rvColors.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        fragPropertiesBsdfBinding.rvColors.setHasFixedSize(true)
        fragPropertiesBsdfBinding.rvColors.adapter = ColorPickerAdapter(requireContext(), object : ColorPickerListener {
            override fun onColorPicked(color: Int) {
                dismiss()
                propertyListener.onColorChanged(color)
            }
        })

        fragPropertiesBsdfBinding.sbSize.progress = brushSize.toInt()
        fragPropertiesBsdfBinding.sbOpacity.progress =
            requireContext().getSharedPreferences("QuoteMakerPrefs", Context.MODE_PRIVATE).getInt("brushAlpha", 100)

        fragPropertiesBsdfBinding.ivBrushPreview.layoutParams.height = brushSize.toInt()
        fragPropertiesBsdfBinding.ivBrushPreview.layoutParams.width = brushSize.toInt()
        fragPropertiesBsdfBinding.ivBrushPreview.setColorFilter(brushColor)
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        when (seekBar!!.id) {
            R.id.sbOpacity -> {
                propertyListener.onOpacityChanged(progress)
                fragPropertiesBsdfBinding.ivBrushPreview.alpha = (0.01 * progress).toFloat()
            }
            R.id.sbSize -> {
                val layoutParams = fragPropertiesBsdfBinding.ivBrushPreview.layoutParams as ConstraintLayout.LayoutParams
                layoutParams.width = progress
                layoutParams.height = progress
                fragPropertiesBsdfBinding.ivBrushPreview.layoutParams = layoutParams

                propertyListener.onBrushSizeChanged(progress)
            }
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {

    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {

    }
}