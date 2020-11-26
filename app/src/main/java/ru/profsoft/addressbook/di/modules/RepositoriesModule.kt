package ru.profsoft.addressbook.di.modules

import android.app.Application
import dagger.Module
import dagger.Provides
import ru.profsoft.addressbook.data.repositories.IProfilesRepository
import ru.profsoft.addressbook.data.repositories.ProfilesRepository
import javax.inject.Singleton

@Module
class RepositoriesModule {

    @Provides
    @Singleton
    fun provideProfilesRepository(
        application: Application
    ) : IProfilesRepository = ProfilesRepository(application)
}