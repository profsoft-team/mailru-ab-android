package ru.profsoft.addressbook.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.activity_main.*
import ru.profsoft.addressbook.R
import ru.profsoft.addressbook.viewmodels.base.BaseViewModel
import ru.profsoft.addressbook.viewmodels.base.IViewModelState
import ru.profsoft.addressbook.viewmodels.base.NavigationCommand

abstract class BaseActivity<T : BaseViewModel<out IViewModelState>> : AppCompatActivity() {
    protected abstract val viewModel: T
    protected abstract val layout: Int
    lateinit var navController: NavController

    abstract fun subscribeOnState(state: IViewModelState)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        viewModel.observeState(this) { subscribeOnState(it) }
        viewModel.observeNavigation(this) { subscribeOnNavigation(it) }

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