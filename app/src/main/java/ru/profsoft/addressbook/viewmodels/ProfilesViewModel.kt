package ru.profsoft.addressbook.viewmodels

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Parcelable
import android.provider.Settings
import androidx.lifecycle.*
import kotlinx.android.parcel.Parcelize
import ru.profsoft.addressbook.data.models.Profile
import ru.profsoft.addressbook.data.repositories.IProfilesRepository
import ru.profsoft.addressbook.viewmodels.base.*
import javax.inject.Inject

class ProfilesViewModel(
    handle: SavedStateHandle,
    private val profilesRepository: IProfilesRepository
) : BaseViewModel<ProfilesState>(handle, ProfilesState()) {
    private val activityResult = MutableLiveData<Event<PendingAction>>()
    private val readContactsPermission = listOf(
        Manifest.permission.READ_CONTACTS
    )

    private val profiles = Transformations.switchMap(state) {
        return@switchMap profilesRepository.getContactList()
    }

    fun observeActivityResults(owner: LifecycleOwner, handler: (action: PendingAction) -> Unit) {
        activityResult.observe(owner, EventObserver { handler(it) })
    }

    fun observeProfiles(owner: LifecycleOwner, onChange: (List<Profile>) -> Unit) {
        profiles.observe(owner, Observer { onChange(it) })
    }

    fun getContacts() {
        profilesRepository.getContacts()
    }

    fun handlePermission(permissionResult: Map<String, Pair<Boolean, Boolean>>) {
        val isAllGranted = permissionResult.values.map { it.first }.contains(false).not()
        val isAllMayBeShown = permissionResult.values.map { it.second }.contains(false).not()

        when {
            isAllGranted -> getContacts()
            isAllMayBeShown.not() -> executeOpenSettings()
            else -> {
                val message = Notify.Message(
                    "Need permissions for reading address book",
                    "Retry"
                ) { requestPermissions(readContactsPermission) }

                notify(message)
            }
        }
    }

    fun showPermissionDialog() {
        requestPermissions(readContactsPermission)
    }

    private fun executeOpenSettings() {
        val handler = {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.parse("package:ru.profsoft.addressbook")
            }
            startForResult(PendingAction.SettingsAction(intent))
        }
        notify(Notify.Message("Need permissions for reading address book", "Open settings", handler))
    }

    private fun startForResult(action: PendingAction) {
        activityResult.value = Event(action)
    }
}

class ProfilesViewModelFactory @Inject constructor(
    private val profilesRepository: IProfilesRepository
) : IViewModelFactory<ProfilesViewModel> {
    override fun create(handle: SavedStateHandle): ProfilesViewModel {
        return ProfilesViewModel(handle, profilesRepository)
    }
}

data class ProfilesState(
    val pendingAction: PendingAction? = null
) : IViewModelState {
    override fun save(outState: SavedStateHandle) {
        outState.set("pendingAction", pendingAction)
    }

    override fun restore(savedState: SavedStateHandle): IViewModelState {
        return copy(pendingAction = savedState["pendingAction"])
    }
}

sealed class PendingAction : Parcelable {
    abstract val payload: Any?

    @Parcelize
    data class SettingsAction(override val payload: Intent) : PendingAction()
}