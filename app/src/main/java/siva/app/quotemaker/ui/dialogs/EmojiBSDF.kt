package siva.app.quotemaker.ui.dialogs

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import siva.app.quotemaker.controls.adapter.EmojiAdapter
import siva.app.quotemaker.controls.listeners.EmojiListener
import siva.app.quotemaker.databinding.FragEmojiBsdfBinding

/*
* Created by Siva Nimmala on 12/8/20.
* */
class EmojiBSDF(var emojiListener: EmojiListener) : BottomSheetDialogFragment() {

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        val fragEmojiBsdfBinding = FragEmojiBsdfBinding.inflate(LayoutInflater.from(requireContext()))
        dialog.setContentView(fragEmojiBsdfBinding.root)

        val params = (fragEmojiBsdfBinding.root.parent as View).layoutParams as CoordinatorLayout.LayoutParams
        val behavior = params.behavior as BottomSheetBehavior

        behavior.addBottomSheetCallback(object : BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    dismiss()
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        (fragEmojiBsdfBinding.root.parent as View).setBackgroundColor(Color.TRANSPARENT)

        fragEmojiBsdfBinding.rvEmoji.layoutManager = GridLayoutManager(requireContext(), 5)
        fragEmojiBsdfBinding.rvEmoji.adapter = context?.let {
            EmojiAdapter(it, object : EmojiListener {
                override fun onEmojiSelected(emoji: String) {
                    dialog.dismiss()
                    emojiListener.onEmojiSelected(emoji)
                }
            })
        }
    }
}