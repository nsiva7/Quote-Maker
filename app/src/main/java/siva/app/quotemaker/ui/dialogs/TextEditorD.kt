package siva.app.quotemaker.ui.dialogs

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.postDelayed
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.dialog_add_text.*
import siva.app.quotemaker.controls.adapter.ColorPickerAdapter
import siva.app.quotemaker.controls.listeners.ColorPickerListener
import siva.app.quotemaker.controls.listeners.TextEditorListener
import siva.app.quotemaker.controls.util.Util
import siva.app.quotemaker.databinding.DialogAddTextBinding

/*
* Created by Siva Nimmala on 12/8/20.
* */
class TextEditorD(private var textEditorListener: TextEditorListener, var color:Int = Color.WHITE, var text:String = "") : DialogFragment() {

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

        dialogAddTextBinding.tvDone.setOnClickListener {
            dismiss()
            textEditorListener.onTextDone(dialogAddTextBinding.etText.text.toString().trim(), color)
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
}