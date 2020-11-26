package ru.profsoft.addressbook.viewmodels

import androidx.lifecycle.SavedStateHandle
import ru.profsoft.addressbook.viewmodels.base.BaseViewModel
import ru.profsoft.addressbook.viewmodels.base.IViewModelState

class ProfilesViewModel(
    handle: SavedStateHandle
) : BaseViewModel<ProfilesState>(handle, ProfilesState()) {

}

class ProfilesState : IViewModelState