package com.example.testcurrency.ui.home

import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import com.example.testcurrency.databinding.FragmentHomeBinding
import com.example.testcurrency.ui.base.BaseFragment
import com.example.testcurrency.utils.fragmentRepeatOnCreated
import com.example.testcurrency.utils.launchAndCollect
import org.koin.androidx.viewmodel.ext.android.viewModel


class HomeFragment : BaseFragment<HomeViewModel>() {

    private var isSearchEnteredFrom = false
    private var isSearchEnteredTo = false
    override val viewModel: HomeViewModel by viewModel()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var selectedItemPosition = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSpinners()
        initEditTexts()
        initClicks()
        fragmentRepeatOnCreated {
            launchAndCollect(viewModel.selectedFromCurrency) { item ->
                binding.fromSpinner.text = item
                convertFrom()
            }

            launchAndCollect(viewModel.selectedFromCurrency) { item ->
                binding.toSpinner.text = item
                convertTo()
            }

            launchAndCollect(viewModel.fromAmount) { amount ->
                binding.fromEdit.setText(amount)
            }

            launchAndCollect(viewModel.toAmount) { amount ->
                binding.toEdit.setText(amount)
            }

            launchAndCollect(viewModel.isLoading) { isVisible ->
                if (isVisible) {
                    binding.loader.visibility = View.VISIBLE
                } else {
                    binding.loader.visibility = View.GONE
                }
            }
        }
    }

    private fun initClicks() {
        binding.swapIcon.setOnClickListener {
            viewModel.swap()
        }
    }

    private fun initEditTexts() {
        binding.fromEdit.doAfterTextChanged {
            if (!isSearchEnteredFrom && viewModel.fromAmount.value != binding.fromEdit.text.toString()) {
                isSearchEnteredFrom = true
                Handler(Looper.getMainLooper()).postDelayed({
                    isSearchEnteredFrom = false
                    convertFrom()
                }, 300)
            }
        }

        binding.toEdit.doAfterTextChanged {
            if (!isSearchEnteredTo && viewModel.toAmount.value != binding.toEdit.text.toString()) {
                isSearchEnteredTo = true
                Handler(Looper.getMainLooper()).postDelayed({
                    isSearchEnteredTo = false
                    convertTo()
                }, 300)
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
            viewModel.selectedFromCurrency.value
        } else {
            viewModel.selectedToCurrency.value
        }
        return viewModel.items.indexOf(code)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}