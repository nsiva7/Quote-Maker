package siva.app.quotemaker.controls.listeners

import android.graphics.Typeface

/*
* Created by Siva Nimmala on 12/8/20.
* */
interface TextEditorListener {
    fun onTextDone(input:String, color:Int, typeface: Typeface)
}