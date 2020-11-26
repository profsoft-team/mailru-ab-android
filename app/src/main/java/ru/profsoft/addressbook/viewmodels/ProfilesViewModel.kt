package ru.profsoft.addressbook.viewmodels

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.Transformations
import ru.profsoft.addressbook.data.models.Profile
import ru.profsoft.addressbook.data.repositories.IProfilesRepository
import ru.profsoft.addressbook.viewmodels.base.BaseViewModel
import ru.profsoft.addressbook.viewmodels.base.IViewModelFactory
import ru.profsoft.addressbook.viewmodels.base.IViewModelState
import javax.inject.Inject

class ProfilesViewModel(
    handle: SavedStateHandle,
    private val profilesRepository: IProfilesRepository
) : BaseViewModel<ProfilesState>(handle, ProfilesState()) {

    private val profiles = Transformations.switchMap(state) {
        return@switchMap profilesRepository.getContactList()
    }

    fun observeProfiles(owner: LifecycleOwner, onChange: (List<Profile>) -> Unit) {
        profiles.observe(owner, Observer { onChange(it) })
    }

    fun getProfiles() {
        profilesRepository.getContacts()
    }
}

class ProfilesViewModelFactory @Inject constructor(
    private val profilesRepository: IProfilesRepository
) : IViewModelFactory<ProfilesViewModel> {
    override fun create(handle: SavedStateHandle): ProfilesViewModel {
        return ProfilesViewModel(handle, profilesRepository)
    }
}

class ProfilesState : IViewModelState