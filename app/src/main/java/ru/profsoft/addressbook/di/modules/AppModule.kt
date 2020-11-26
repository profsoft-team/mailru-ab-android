package ru.profsoft.addressbook.di.modules

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class AppModule {
    @Provides
    fun provideApplicationContext(application: Application): Context = application
}