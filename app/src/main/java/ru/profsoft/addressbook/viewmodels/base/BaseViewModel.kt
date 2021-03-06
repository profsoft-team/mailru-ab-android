package ru.profsoft.addressbook.viewmodels.base

import android.os.Bundle
import androidx.annotation.UiThread
import androidx.lifecycle.*
import androidx.navigation.NavOptions
import androidx.navigation.Navigator

abstract class BaseViewModel<T : IViewModelState>(
    private val handleState: SavedStateHandle,
    initState: T
) : ViewModel() {
    val navigation = MutableLiveData<Event<NavigationCommand>>()
    val permissions = MutableLiveData<Event<List<String>>>()
    val notifications = MutableLiveData<Event<Notify>>()

    val state: MediatorLiveData<T> = MediatorLiveData<T>().apply {
        value = initState
    }

    val currentState
        get() = state.value!!

    @UiThread
    protected inline fun updateState(update: (currentState: T) -> T) {
        val updatedState: T = update(currentState)
        state.value = updatedState
    }

    fun observeState(owner: LifecycleOwner, onChanged: (newState: T) -> Unit) {
        state.observe(owner, Observer { onChanged(it!!) })
    }

    open fun navigate(navigationCommand: NavigationCommand) {
        navigation.value = Event(navigationCommand)
    }

    fun observeNavigation(owner: LifecycleOwner, onNavigate: (navigationCommand: NavigationCommand) -> Unit) {
        navigation.observe(owner, EventObserver { onNavigate(it) })
    }

    protected fun notify(content: Notify) {
        notifications.value = Event(content)
    }

    fun observeNotifications(owner: LifecycleOwner, onNotify: (notification: Notify) -> Unit) {
        notifications.observe(owner, EventObserver { onNotify(it) })
    }

    fun saveState() {
        currentState.save(handleState)
    }

    @Suppress("UNCHECKED_CAST")
    fun restoreState() {
        state.value = currentState.restore(handleState) as T
    }

    fun requestPermissions(requestedPermission: List<String>) {
        permissions.value = Event(requestedPermission)
    }

    fun observePermissions(owner: LifecycleOwner, handle: (permissions: List<String>) -> Unit) {
        permissions.observe(owner, EventObserver { handle(it) })
    }

}

class Event<out E>(private val content: E) {
    var hasBeenHandled = false

    fun getContentIfNotHandled(): E? {
        return if (hasBeenHandled) null
        else {
            hasBeenHandled = true
            content
        }
    }
}

class EventObserver<E>(private val onEventUnhandledContent: (E) -> Unit) : Observer<Event<E>> {

    override fun onChanged(event: Event<E>?) {
        event?.getContentIfNotHandled()?.let {
            onEventUnhandledContent(it)
        }
    }
}

sealed class NavigationCommand {
    data class To(
        val destination: Int,
        val args: Bundle? = null,
        val options: NavOptions? = null,
        val extras: Navigator.Extras? = null
    ): NavigationCommand()
}

sealed class Notify {
    abstract val message: String

    data class Message(
        override val message: String,
        val label: String? = null,
        val handler: (() -> Unit)? = null
    ) : Notify()
}