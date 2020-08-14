package siva.app.quotemaker.ui.dialogs

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.core.view.setPadding
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import siva.app.quotemaker.R
import siva.app.quotemaker.controls.adapter.ColorPickerAdapter
import siva.app.quotemaker.controls.listeners.ColorPickerListener
import siva.app.quotemaker.controls.listeners.TextEditorListener
import siva.app.quotemaker.controls.util.Util
import siva.app.quotemaker.databinding.DialogAddTextBinding

/*
* Created by Siva Nimmala on 12/8/20.
* */
class TextEditorD(private var textEditorListener: TextEditorListener, var color: Int = Color.WHITE, var text: String = "") : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return DialogAddTextBinding.inflate(inflater, container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dialogAddTextBinding = DialogAddTextBinding.bind(view)

        view.postDelayed({ Util.showKeyboard(requireActivity(), dialogAddTextBinding.etText) }, 500)

        dialogAddTextBinding.rvTextColorPicker.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        dialogAddTextBinding.rvTextColorPicker.setHasFixedSize(true)
        dialogAddTextBinding.rvTextColorPicker.adapter = ColorPickerAdapter(requireContext(), object : ColorPickerListener {
            override fun onColorPicked(color: Int) {
                this@TextEditorD.color = color
                dialogAddTextBinding.etText.setTextColor(color)
            }
        })

        if (text.isNotEmpty()) {
            dialogAddTextBinding.etText.setText(text)
        }

        dialogAddTextBinding.etText.setTextColor(color)
        dialogAddTextBinding.etText.typeface = getTypeFace(requireContext(), 0)

        dialogAddTextBinding.tvDone.setOnClickListener {
            dismiss()
            textEditorListener.onTextDone(dialogAddTextBinding.etText.text.toString().trim(), color, dialogAddTextBinding.etText.typeface)
        }

        dialogAddTextBinding.spFonts.adapter = FontsAdapter(requireContext())
        dialogAddTextBinding.spFonts.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                dialogAddTextBinding.etText.typeface = getTypeFace(parent!!.context, position)
            }
        }
    }

    override fun onStart() {
        super.onStart()

        val dialog = dialog
        if (dialog != null) {
            dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    class FontsAdapter(val context: Context) : BaseAdapter() {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val textView = TextView(parent!!.context)
            textView.typeface = getTypeFace(context, position)
            textView.setPadding(20)
            textView.setTextColor(Color.BLACK)
            textView.textSize = 20F
            if (position == 0){
                textView.text = context.getString(R.string.defaultFontLabel)
            }else{
                textView.text = context.getString(R.string.fontSampleText)
            }
            return textView
        }

        override fun getItem(position: Int): Any {
            return position
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return 20
        }

    }

    companion object {
        fun getTypeFace(context: Context, pos: Int): Typeface {
            when (pos) {
                1 -> return Typeface.createFromAsset(context.assets, "fonts/Amano.ttf")
                2 -> return Typeface.createFromAsset(context.assets, "fonts/Angelina.ttf")
                3 -> return Typeface.createFromAsset(context.assets, "fonts/Asul-Regular.ttf")
                4 -> return Typeface.createFromAsset(context.assets, "fonts/ayuma.ttf")
                5 -> return Typeface.createFromAsset(context.assets, "fonts/Bloody.ttf")
                6 -> return Typeface.createFromAsset(context.assets, "fonts/CandyCane.ttf")
                7 -> return Typeface.createFromAsset(context.assets, "fonts/Comfortaa-Regular.ttf")
                8 -> return Typeface.createFromAsset(context.assets, "fonts/ConeOfSilence.ttf")
                9 -> return Typeface.createFromAsset(context.assets, "fonts/Dinosaur Skin.ttf")
                10 -> return Typeface.createFromAsset(context.assets, "fonts/Dosis-Regular.ttf")
                11 -> return Typeface.createFromAsset(context.assets, "fonts/Exo2-Regular.ttf")
                12 -> return Typeface.createFromAsset(context.assets, "fonts/Inconsolata-Regular.ttf")
                13 -> return Typeface.createFromAsset(context.assets, "fonts/JosefinSans-Regular.ttf")
                14 -> return Typeface.createFromAsset(context.assets, "fonts/Jura-Regular.ttf")
                15 -> return Typeface.createFromAsset(context.assets, "fonts/Lemonada-Regular.ttf")
                16 -> return Typeface.createFromAsset(context.assets, "fonts/MarkaziText-Regular.ttf")
                17 -> return Typeface.createFromAsset(context.assets, "fonts/Orbitron-Regular.ttf")
                18 -> return Typeface.createFromAsset(context.assets, "fonts/Quicksand-Regular.ttf")
                19 -> return Typeface.createFromAsset(context.assets, "fonts/YanoneKaffeesatz-Regular.ttf")
            }
            return Typeface.createFromAsset(context.assets, "fonts/default.ttf")
        }
    }
}