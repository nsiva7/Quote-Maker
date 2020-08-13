package siva.app.quotemaker

import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication

/*
* Created by Siva Nimmala on 11/8/20.
* */
class QuoteMakerApp: MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)
    }
}