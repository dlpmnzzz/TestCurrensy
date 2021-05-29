package com.example.testcurrency.ui.home

import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSpinners()
        initEditTexts()
        viewModel.selectedFrom.observe(viewLifecycleOwner) { item ->
            item?.let {
                binding.fromSpinner.text = it
            }
        }
        viewModel.selectedTo.observe(viewLifecycleOwner) { item ->
            item?.let {
                binding.toSpinner.text = it
            }
        }
    }

    private fun initEditTexts() {
        binding.fromEdit.setAfterTextChangeListener {
            if (!isSearchEnteredFrom) {
                isSearchEnteredFrom = true
                Handler(Looper.getMainLooper()).postDelayed({
                    isSearchEnteredFrom = false
                    viewModel.convertCurrency(
                        binding.fromEdit.text.toString().toDouble(),
                        FROM_TYPE
                    )
                }, 500)
            }
        }

        binding.toEdit.setAfterTextChangeListener {
            if (!isSearchEnteredTo) {
                isSearchEnteredTo = true
                Handler(Looper.getMainLooper()).postDelayed({
                    isSearchEnteredTo = false
                    viewModel.convertCurrency(
                        binding.fromEdit.text.toString().toDouble(),
                        TO_TYPE
                    )
                }, 500)
            }
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
        }.setPositiveButton("Ok") { _, which ->
            if (selectedItemPosition != -1) {
                viewModel.itemSelected(selectedItemPosition, type)
            }
        }
        alertDialog.setTitle("AlertDialog")
        val checkedItem = 1
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
}