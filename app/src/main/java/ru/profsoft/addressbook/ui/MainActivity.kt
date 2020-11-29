package ru.profsoft.addressbook.ui

import androidx.activity.viewModels
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import ru.profsoft.addressbook.R
import ru.profsoft.addressbook.ui.base.BaseActivity
import ru.profsoft.addressbook.viewmodels.MainViewModel
import ru.profsoft.addressbook.viewmodels.base.IViewModelState
import ru.profsoft.addressbook.viewmodels.base.Notify

class MainActivity : BaseActivity<MainViewModel>() {

    override val layout: Int = R.layout.activity_main
    public override val viewModel: MainViewModel by viewModels()

    override fun subscribeOnState(state: IViewModelState) {}

    override fun renderNotification(notify: Notify) {
        val snackbar = Snackbar.make(container, notify.message, Snackbar.LENGTH_SHORT)

        when(notify) {
            is Notify.Message -> {
                with(snackbar) {
                    setBackgroundTint(getColor(R.color.design_default_color_error))
                    setTextColor(getColor(android.R.color.white))
                    setActionTextColor(getColor(android.R.color.white))
                    setAction(notify.label) { notify.handler?.invoke() }
                }
            }
        }

        snackbar.show()
    }
}