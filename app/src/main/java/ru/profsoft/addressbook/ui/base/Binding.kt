package ru.profsoft.addressbook.ui.base

import android.os.Bundle
import ru.profsoft.addressbook.viewmodels.base.IViewModelState

abstract class Binding {
    val delegates = mutableMapOf<String, RenderProp<out Any>>()
    var isInflated = false

    open val afterInflated: (() -> Unit)? = null
    fun onFinishInflate() {
        if(!isInflated) {
            afterInflated?.invoke()
            isInflated = true
            rebind()
        }
    }

    fun rebind() {
        delegates.forEach { it.value.bind() }
    }

    abstract fun bind(data : IViewModelState)

    open fun saveUi(outState: Bundle) {
    }

    open fun restoreUi(savedState: Bundle?) {

    }
}