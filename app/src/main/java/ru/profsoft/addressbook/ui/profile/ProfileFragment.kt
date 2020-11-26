package ru.profsoft.addressbook.ui.profile

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_profile.*
import ru.profsoft.addressbook.App
import ru.profsoft.addressbook.R
import ru.profsoft.addressbook.data.models.Profile
import ru.profsoft.addressbook.ui.base.BaseFragment
import ru.profsoft.addressbook.ui.base.ToolbarBuilder
import ru.profsoft.addressbook.viewmodels.ProfileViewModel
import java.util.*

class ProfileFragment : BaseFragment<ProfileViewModel>() {

    init {
        App.INSTANCE.appComponent.inject(this@ProfileFragment)
    }

    private lateinit var phoneNumberAdapter: PhoneNumberAdapter

    override val layout: Int = R.layout.fragment_profile
    override val viewModel: ProfileViewModel by viewModels()
    override val prepareToolbar: (ToolbarBuilder.() -> Unit)? = {
        this.setTitle(getString(R.string.profile_title))
    }

    override fun setupViews() {
        val profile = requireArguments().get("profile") as Profile
        initAdapter(profile.phones)

        if(profile.image == null)
            iv_avatar.setInitials(profile.name[0].toString().toUpperCase(Locale.getDefault()))
        else
            iv_avatar.setImageBitmap(profile.image)
        tv_name.text = profile.name
    }

    private fun initAdapter(phones: List<String>) {
        phoneNumberAdapter = PhoneNumberAdapter(phones)

        rv_phone_number.apply {
            addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))
            layoutManager = LinearLayoutManager(requireContext())
            adapter = phoneNumberAdapter
        }
    }
}