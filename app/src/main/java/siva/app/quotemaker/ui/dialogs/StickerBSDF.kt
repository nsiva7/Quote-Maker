package siva.app.quotemaker.ui.dialogs

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Bitmap
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.frag_emoji_bsdf.*
import siva.app.quotemaker.controls.adapter.EmojiAdapter
import siva.app.quotemaker.controls.adapter.StickerAdapter
import siva.app.quotemaker.controls.listeners.EmojiListener
import siva.app.quotemaker.controls.listeners.StickerListener
import siva.app.quotemaker.databinding.FragStickerBsdfBinding

/*
* Created by Siva Nimmala on 12/8/20.
* */
class StickerBSDF(var stickerListener: StickerListener) : BottomSheetDialogFragment() {

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)

        val fragStickerBsdfBinding = FragStickerBsdfBinding.inflate(LayoutInflater.from(requireContext()))
        dialog.setContentView(fragStickerBsdfBinding.root)

        val params = (fragStickerBsdfBinding.root.parent as View).layoutParams as CoordinatorLayout.LayoutParams
        val behavior = params.behavior  as BottomSheetBehavior

        behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    dismiss()
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        (fragStickerBsdfBinding.root.parent as View).setBackgroundColor(Color.TRANSPARENT)

        fragStickerBsdfBinding.rvStickers.layoutManager = GridLayoutManager(requireContext(), 5)
        fragStickerBsdfBinding.rvStickers.adapter = context?.let {
            StickerAdapter(object : StickerListener {
                override fun onStickerSelected(bitmap: Bitmap) {
                    dialog.dismiss()
                    stickerListener.onStickerSelected(bitmap)
                }
            }, resources)
        }
    }
}