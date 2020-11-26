package ru.profsoft.addressbook.viewmodels

import androidx.lifecycle.SavedStateHandle
import ru.profsoft.addressbook.viewmodels.base.BaseViewModel
import ru.profsoft.addressbook.viewmodels.base.IViewModelState

class MainViewModel(
    handle: SavedStateHandle
) : BaseViewModel<MainState>(handle, MainState()) {

}

class MainState : IViewModelState