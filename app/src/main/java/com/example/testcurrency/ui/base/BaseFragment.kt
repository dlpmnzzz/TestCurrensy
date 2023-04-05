package com.example.testcurrency.ui.base

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope

abstract class BaseFragment<VM : BaseViewModel> : Fragment() {

    protected abstract val viewModel: VM
    val fragmentScope:  LifecycleCoroutineScope
        get() = viewLifecycleOwner.lifecycleScope

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.message.observe(viewLifecycleOwner) {
            showToast(it)
        }
    }

    private fun showToast(text: String?, duration: Int = Toast.LENGTH_LONG) {
        text?.let { Toast.makeText(context, text, duration).show() }
    }
}