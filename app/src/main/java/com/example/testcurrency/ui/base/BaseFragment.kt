package com.example.testcurrency.ui.base

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment

abstract class BaseFragment<VM : BaseViewModel> : Fragment() {

    protected abstract val viewModel: VM

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