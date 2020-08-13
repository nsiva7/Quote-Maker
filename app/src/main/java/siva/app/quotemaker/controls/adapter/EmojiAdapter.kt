package siva.app.quotemaker.controls.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ja.burhanrashid52.photoeditor.PhotoEditor
import siva.app.quotemaker.controls.listeners.EmojiListener
import siva.app.quotemaker.databinding.RowEmojiBinding

/*
* Created by Siva Nimmala on 12/8/20.
* */
class EmojiAdapter(context: Context, private val emojiListener: EmojiListener) : RecyclerView.Adapter<EmojiAdapter.EmojiHolder>() {

    private var emojiList = PhotoEditor.getEmojis(context)

    class EmojiHolder(var rowEmojiBinding: RowEmojiBinding, emojiListener: EmojiListener, emojis: List<String>) :
        RecyclerView.ViewHolder(rowEmojiBinding.root) {
        init {
            rowEmojiBinding.txtEmoji.setOnClickListener { emojiListener.onEmojiSelected(emojis[adapterPosition]) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmojiHolder {
        return EmojiHolder(RowEmojiBinding.inflate(LayoutInflater.from(parent.context), parent, false), emojiListener, emojiList)
    }

    override fun getItemCount(): Int {
        return emojiList.size
    }

    override fun onBindViewHolder(holder: EmojiHolder, position: Int) {
        holder.rowEmojiBinding.txtEmoji.text = emojiList[position]
    }
}