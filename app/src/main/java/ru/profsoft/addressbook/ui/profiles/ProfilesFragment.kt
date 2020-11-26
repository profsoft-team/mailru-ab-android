package ru.profsoft.addressbook.ui.profiles

import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_profiles.*
import ru.profsoft.addressbook.App
import ru.profsoft.addressbook.R
import ru.profsoft.addressbook.ui.base.BaseFragment
import ru.profsoft.addressbook.ui.base.ToolbarBuilder
import ru.profsoft.addressbook.viewmodels.ProfilesViewModel
import ru.profsoft.addressbook.viewmodels.ProfilesViewModelFactory
import ru.profsoft.addressbook.viewmodels.base.NavigationCommand
import ru.profsoft.addressbook.viewmodels.base.SavedStateViewModelFactory
import javax.inject.Inject

class ProfilesFragment : BaseFragment<ProfilesViewModel>() {

    init {
        App.INSTANCE.appComponent.inject(this@ProfilesFragment)
    }

    @Inject
    internal lateinit var profilesViewModelFactory: ProfilesViewModelFactory

    override val layout: Int = R.layout.fragment_profiles
    override val viewModel: ProfilesViewModel by viewModels {
        SavedStateViewModelFactory(profilesViewModelFactory, this)
    }

    override val prepareToolbar: (ToolbarBuilder.() -> Unit)? = {
        this.setTitle(getString(R.string.contacts_title))
            .setBackButtonVisible(false)
    }
    private lateinit var profileAdapter: ProfileAdapter

    override fun setupViews() {
        initAdapter()

        viewModel.observeProfiles(viewLifecycleOwner) {
            profileAdapter.updateData(it)
        }
    }

    private fun initAdapter() {
        profileAdapter = ProfileAdapter {
            val bundle = bundleOf("profile" to it)
            viewModel.navigate(NavigationCommand.To(R.id.profileFragment, bundle))
        }

        rv_profiles.apply {
            addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))
            layoutManager = LinearLayoutManager(requireContext())
            adapter = profileAdapter
        }
    }
}