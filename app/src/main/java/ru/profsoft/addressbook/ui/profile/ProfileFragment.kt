package ru.profsoft.addressbook.ui.profile

import androidx.fragment.app.viewModels
import ru.profsoft.addressbook.R
import ru.profsoft.addressbook.ui.base.BaseFragment
import ru.profsoft.addressbook.viewmodels.ProfileViewModel

class ProfileFragment : BaseFragment<ProfileViewModel>() {

    override val layout: Int = R.layout.fragment_profile
    override val viewModel: ProfileViewModel by viewModels()

    override fun setupViews() {

    }
}