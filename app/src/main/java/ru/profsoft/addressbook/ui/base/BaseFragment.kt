package ru.profsoft.addressbook.ui.base

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import ru.profsoft.addressbook.ui.MainActivity
import ru.profsoft.addressbook.viewmodels.base.BaseViewModel
import ru.profsoft.addressbook.viewmodels.base.IViewModelState

abstract class BaseFragment<T : BaseViewModel<out IViewModelState>> : Fragment() {
    val main: MainActivity
        get() = activity as MainActivity
    open val binding: Binding? = null
    protected abstract val viewModel: T
    protected abstract val layout: Int

    abstract fun setupViews()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(layout, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.restoreState()
        binding?.restoreUi(savedInstanceState)

        viewModel.observeState(viewLifecycleOwner) { binding?.bind(it) }

        if(binding?.isInflated == false) binding?.onFinishInflate()

        viewModel.observeNavigation(viewLifecycleOwner) {
            main.viewModel.navigate(it)
        }

        setupViews()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        viewModel.saveState()
        binding?.saveUi(outState)
        super.onSaveInstanceState(outState)
    }
}