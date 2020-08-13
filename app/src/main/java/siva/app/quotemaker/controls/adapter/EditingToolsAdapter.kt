package siva.app.quotemaker.controls.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import siva.app.quotemaker.R
import siva.app.quotemaker.controls.enums.ToolType
import siva.app.quotemaker.controls.listeners.ToolSelectedListener
import siva.app.quotemaker.controls.model.ToolModel
import siva.app.quotemaker.databinding.RowEditingToolsBinding

/*
* Created by Siva Nimmala on 12/8/20.
* */
class EditingToolsAdapter(private val toolSelectedListener: ToolSelectedListener) : RecyclerView.Adapter<EditingToolsAdapter.EditingToolsHolder>() {

    private var tools: List<ToolModel> = arrayListOf(
        ToolModel("Brush", R.drawable.ic_brush, ToolType.BRUSH),
        ToolModel("Text", R.drawable.ic_text, ToolType.TEXT),
        ToolModel("Eraser", R.drawable.ic_eraser, ToolType.ERASER),
        ToolModel("Filter", R.drawable.ic_photo_filter, ToolType.FILTER),
        ToolModel("Emoji", R.drawable.ic_insert_emoticon, ToolType.EMOJI),
        ToolModel("Sticker", R.drawable.ic_sticker, ToolType.STICKER)
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditingToolsHolder {
        return EditingToolsHolder(RowEditingToolsBinding.inflate(LayoutInflater.from(parent.context), parent, false), toolSelectedListener, tools)
    }

    override fun getItemCount(): Int {
        return tools.size
    }

    override fun onBindViewHolder(holder: EditingToolsHolder, position: Int) {
        holder.rowEditingToolsBinding.tvTool.text = tools[position].toolName
        holder.rowEditingToolsBinding.ivToolIcon.setImageResource(tools[position].tollIcon)
    }

    class EditingToolsHolder(
        var rowEditingToolsBinding: RowEditingToolsBinding,
        private val toolSelectedListener: ToolSelectedListener,
        tools: List<ToolModel>
    ) : RecyclerView.ViewHolder(rowEditingToolsBinding.root) {

        init {
            rowEditingToolsBinding.ivToolIcon.setOnClickListener { toolSelectedListener.onToolSelected(tools[adapterPosition].tollType) }
            rowEditingToolsBinding.tvTool.setOnClickListener { rowEditingToolsBinding.ivToolIcon.performClick() }
        }
    }
}