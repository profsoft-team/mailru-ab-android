package ru.profsoft.addressbook.ui

import androidx.activity.viewModels
import ru.profsoft.addressbook.R
import ru.profsoft.addressbook.ui.base.BaseActivity
import ru.profsoft.addressbook.viewmodels.MainViewModel
import ru.profsoft.addressbook.viewmodels.base.IViewModelState

class MainActivity : BaseActivity<MainViewModel>() {

    override val layout: Int = R.layout.activity_main
    public override val viewModel: MainViewModel by viewModels()

    override fun subscribeOnState(state: IViewModelState) {}
}