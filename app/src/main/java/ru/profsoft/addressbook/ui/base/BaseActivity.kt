package ru.profsoft.addressbook.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import ru.profsoft.addressbook.R
import ru.profsoft.addressbook.extensions.visible
import ru.profsoft.addressbook.viewmodels.base.BaseViewModel
import ru.profsoft.addressbook.viewmodels.base.IViewModelState
import ru.profsoft.addressbook.viewmodels.base.NavigationCommand
import ru.profsoft.addressbook.viewmodels.base.Notify

abstract class BaseActivity<T : BaseViewModel<out IViewModelState>> : AppCompatActivity() {
    protected abstract val viewModel: T
    protected abstract val layout: Int
    lateinit var navController: NavController
    val toolbarBuilder = ToolbarBuilder()

    abstract fun subscribeOnState(state: IViewModelState)
    abstract fun renderNotification(notify: Notify)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        viewModel.observeState(this) { subscribeOnState(it) }
        viewModel.observeNavigation(this) { subscribeOnNavigation(it) }
        viewModel.observeNotifications(this) { renderNotification(it) }

        navController = findNavController(R.id.nav_host_fragment)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        viewModel.saveState()
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        viewModel.restoreState()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun subscribeOnNavigation(navigationCommand: NavigationCommand) {
        when (navigationCommand) {
            is NavigationCommand.To -> {
                navController.navigate(
                    navigationCommand.destination,
                    navigationCommand.args,
                    navigationCommand.options,
                    navigationCommand.extras
                )
            }
        }
    }
}

class ToolbarBuilder {
    var title: String? = null
    var isBackButtonVisible: Boolean = true
    var visibility: Boolean = true

    fun setTitle(title: String): ToolbarBuilder {
        this.title = title
        return this
    }

    fun setBackButtonVisible(isVisible: Boolean): ToolbarBuilder {
        this.isBackButtonVisible = isVisible
        return this
    }

    fun invalidate(): ToolbarBuilder {
        this.title = null
        this.isBackButtonVisible = true
        this.visibility = true
        return this
    }

    fun prepare(prepareFn: (ToolbarBuilder.() -> Unit)?): ToolbarBuilder {
        prepareFn?.invoke(this)
        return this
    }

    fun build(context: FragmentActivity) {

        with(context.toolbar) {
            toolbar.visible(this@ToolbarBuilder.visibility)

            if (this@ToolbarBuilder.visibility) {
                if (this@ToolbarBuilder.title != null) {
                    toolbar.title = this@ToolbarBuilder.title
                }

                if (this@ToolbarBuilder.isBackButtonVisible.not()) {
                    this.navigationIcon = null
                } else {
                    val drawable = ResourcesCompat.getDrawable(resources, R.drawable.ic_back, null)
                    this.navigationIcon = drawable
                }
            }
        }
    }
}