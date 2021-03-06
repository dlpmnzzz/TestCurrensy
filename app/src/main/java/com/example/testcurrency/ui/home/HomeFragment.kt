package com.example.testcurrency.ui.home

import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.testcurrency.databinding.FragmentHomeBinding
import com.example.testcurrency.ui.base.BaseFragment
import com.example.testcurrency.utils.setAfterTextChangeListener
import org.koin.androidx.viewmodel.ext.android.viewModel


class HomeFragment : BaseFragment<HomeViewModel>() {

    private var isSearchEnteredFrom = false
    private var isSearchEnteredTo = false
    override val viewModel: HomeViewModel by viewModel()
    private lateinit var binding: FragmentHomeBinding
    private var selectedItemPosition = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(
            inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }
        binding.viewmodel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSpinners()
        initEditTexts()
        viewModel.selectedFrom.observe(viewLifecycleOwner) { item ->
            item?.let {
                binding.fromSpinner.text = it
                convertFrom()
            }
        }
        viewModel.selectedTo.observe(viewLifecycleOwner) { item ->
            item?.let {
                binding.toSpinner.text = it
                convertTo()
            }
        }
        viewModel.fromResult.observe(viewLifecycleOwner) {
            binding.fromEdit.setText(it)
        }
        viewModel.toResult.observe(viewLifecycleOwner) {
            binding.toEdit.setText(it)
        }
    }

    private fun initEditTexts() {
        binding.fromEdit.setAfterTextChangeListener {
            if (!isSearchEnteredFrom && viewModel.fromResult.value != binding.fromEdit.text.toString()) {
                isSearchEnteredFrom = true
                Handler(Looper.getMainLooper()).postDelayed({
                    isSearchEnteredFrom = false
                    convertFrom()
                }, 1000)
            }
        }

        binding.toEdit.setAfterTextChangeListener {
            if (!isSearchEnteredTo && viewModel.toResult.value != binding.toEdit.text.toString()) {
                isSearchEnteredTo = true
                Handler(Looper.getMainLooper()).postDelayed({
                    isSearchEnteredTo = false
                    convertTo()
                }, 1000)
            }
        }
    }

    private fun convertFrom() {
        val str = binding.fromEdit.text.toString()
        if (str.isNotEmpty()) {
            viewModel.convertCurrency(
                str.toFloat(),
                FROM_TYPE
            )
        }
    }

    private fun convertTo() {
        val str = binding.toEdit.text.toString()
        if (str.isNotEmpty()) {
            viewModel.convertCurrency(
                str.toFloat(),
                TO_TYPE
            )
        }
    }

    private fun initSpinners() {
        binding.fromSpinner.setOnClickListener {
            showDialog(FROM_TYPE)
        }
        binding.toSpinner.setOnClickListener {
            showDialog(TO_TYPE)
        }
    }

    private fun showDialog(type: String) {
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        alertDialog.setNegativeButton("Cancel") { dialog, _ ->
            selectedItemPosition = -1
            dialog.cancel()
        }.setPositiveButton("Ok") { _, _ ->
            if (selectedItemPosition != -1) {
                viewModel.itemSelected(selectedItemPosition, type)
            }
        }
        alertDialog.setTitle("Choose a currency")
        val checkedItem = getCheckedItemPosition(type)
        alertDialog.setSingleChoiceItems(
            viewModel.items,
            checkedItem
        ) { _, which ->
            selectedItemPosition = which
        }
        val alert: AlertDialog = alertDialog.create()
        alert.setCanceledOnTouchOutside(false)
        alert.show()
    }

    private fun getCheckedItemPosition(type: String): Int {
        val code = if (type == FROM_TYPE) {
            viewModel.selectedFrom.value
        } else {
            viewModel.selectedTo.value
        }
        return viewModel.items.indexOf(code)
    }
}