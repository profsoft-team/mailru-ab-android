package ru.profsoft.addressbook.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import ru.profsoft.addressbook.App
import ru.profsoft.addressbook.di.modules.AppModule
import ru.profsoft.addressbook.di.modules.RepositoriesModule
import ru.profsoft.addressbook.ui.MainActivity
import ru.profsoft.addressbook.ui.profile.ProfileFragment
import ru.profsoft.addressbook.ui.profiles.ProfilesFragment
import javax.inject.Singleton


@Singleton
@Component(
    modules = [
        AppModule::class,
        RepositoriesModule::class
    ]
)
interface AppComponent {
    fun inject(application: App)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    interface ComponentProvider {
        val appComponent: AppComponent
    }

    //activity, fragment
    fun inject(activity: MainActivity)
    fun inject(fragment: ProfilesFragment)
    fun inject(fragment: ProfileFragment)
}