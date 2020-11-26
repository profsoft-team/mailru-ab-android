package ru.profsoft.addressbook

import android.app.Application
import ru.profsoft.addressbook.di.AppComponent
import ru.profsoft.addressbook.di.DaggerAppComponent

class App : Application(), AppComponent.ComponentProvider {

    override lateinit var appComponent: AppComponent

    companion object {
        lateinit var INSTANCE: App
            private set
    }

    init {
        INSTANCE = this
    }

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .application(this)
            .build()

        appComponent.inject(this)
    }
}