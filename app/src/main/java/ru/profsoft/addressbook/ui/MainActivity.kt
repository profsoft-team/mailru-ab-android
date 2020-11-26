package ru.profsoft.addressbook.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import ru.profsoft.addressbook.R
import ru.profsoft.addressbook.ui.base.BaseActivity
import ru.profsoft.addressbook.viewmodels.MainViewModel
import ru.profsoft.addressbook.viewmodels.base.IViewModelState

class MainActivity : BaseActivity<MainViewModel>() {

    override val layout: Int = R.layout.activity_main
    public override val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS), PERMISSIONS_REQUEST)
            return
        }
    }

    override fun subscribeOnState(state: IViewModelState) {}

    companion object {
        private const val PERMISSIONS_REQUEST = 1
    }
}