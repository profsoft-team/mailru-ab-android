package ru.profsoft.addressbook.ui.profiles

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_profiles.*
import ru.profsoft.addressbook.App
import ru.profsoft.addressbook.R
import ru.profsoft.addressbook.extensions.checkReadContactsPermission
import ru.profsoft.addressbook.ui.base.BaseFragment
import ru.profsoft.addressbook.ui.base.ToolbarBuilder
import ru.profsoft.addressbook.viewmodels.PendingAction
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
    private lateinit var resultRegistry: ActivityResultRegistry
    lateinit var permissionLauncher: ActivityResultLauncher<Array<out String>>
    lateinit var settingsLauncher: ActivityResultLauncher<Intent>

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (::resultRegistry.isInitialized.not()) resultRegistry = requireActivity().activityResultRegistry

        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions(), resultRegistry, ::callbackPermissions)
        settingsLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(), resultRegistry, ::callbackSettings)
    }

    override fun setupViews() {
        initAdapter()

        viewModel.observeProfiles(viewLifecycleOwner) {
            profileAdapter.updateData(it)
        }

        viewModel.observePermissions(viewLifecycleOwner) {
            permissionLauncher.launch(it.toTypedArray())
        }

        viewModel.observeActivityResults(viewLifecycleOwner) {
            when(it) {
                is PendingAction.SettingsAction -> settingsLauncher.launch(it.payload)
            }
        }

        viewModel.showPermissionDialog()
    }

    private fun initAdapter() {
        profileAdapter = ProfileAdapter {
            val bundle = bundleOf("profile" to it)
            viewModel.navigate(NavigationCommand.To(R.id.profileFragment, bundle))
        }
        profileAdapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        rv_profiles.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = profileAdapter
        }
    }

    private fun callbackSettings(result: ActivityResult) {
        if (requireContext().checkReadContactsPermission())
            viewModel.getContacts()
    }

    private fun callbackPermissions(result: Map<String, Boolean>) {
        val permissionResult = result.mapValues { (permission, isGranted) ->
            if (isGranted) true to true
            else false to ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                permission
            )
        }

        viewModel.handlePermission(permissionResult)
    }
}