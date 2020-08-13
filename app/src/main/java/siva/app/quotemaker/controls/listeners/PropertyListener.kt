package siva.app.quotemaker.controls.listeners

/*
* Created by Siva Nimmala on 12/8/20.
* */
interface PropertyListener {
    fun onColorChanged(color : Int)
    fun onOpacityChanged(opacity : Int)
    fun onBrushSizeChanged(brushSize : Int)
}