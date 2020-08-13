package siva.app.quotemaker.controls.listeners

import siva.app.quotemaker.controls.enums.ToolType

/*
* Created by Siva Nimmala on 12/8/20.
* */
interface ToolSelectedListener {
    fun onToolSelected(toolType: ToolType)
}