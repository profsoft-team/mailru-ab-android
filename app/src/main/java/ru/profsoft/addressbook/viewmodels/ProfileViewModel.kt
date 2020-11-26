package ru.profsoft.addressbook.viewmodels

import androidx.lifecycle.SavedStateHandle
import ru.profsoft.addressbook.viewmodels.base.BaseViewModel
import ru.profsoft.addressbook.viewmodels.base.IViewModelState

class ProfileViewModel(
    handle: SavedStateHandle
) : BaseViewModel<ProfileState>(handle, ProfileState()) {

}

class ProfileState : IViewModelState