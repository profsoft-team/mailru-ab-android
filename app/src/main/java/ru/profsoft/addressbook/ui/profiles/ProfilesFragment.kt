
package ru.profsoft.addressbook.ui.profiles

import androidx.fragment.app.viewModels
import ru.profsoft.addressbook.R
import ru.profsoft.addressbook.ui.base.BaseFragment
import ru.profsoft.addressbook.viewmodels.ProfilesViewModel

class ProfilesFragment : BaseFragment<ProfilesViewModel>() {

    override val layout: Int = R.layout.fragment_profiles
    override val viewModel: ProfilesViewModel by viewModels()

    override fun setupViews() {

    }
}