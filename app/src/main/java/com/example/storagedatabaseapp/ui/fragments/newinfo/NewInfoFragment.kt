package com.example.storagedatabaseapp.ui.fragments.newinfo

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.storagedatabaseapp.MyApplication
import com.example.storagedatabaseapp.R
import com.example.storagedatabaseapp.data.datasource.LocalDataSource
import com.example.storagedatabaseapp.data.db.entities.GivingRequest
import com.example.storagedatabaseapp.data.db.entities.Material
import com.example.storagedatabaseapp.data.db.entities.Style
import com.example.storagedatabaseapp.data.db.entities.TakingRequest
import com.example.storagedatabaseapp.ui.MainActivity
import com.example.storagedatabaseapp.util.makeClickable
import kotlinx.android.synthetic.main.fragment_new_info.*
import java.lang.NumberFormatException
import java.util.*
import javax.inject.Inject

class NewInfoFragment : Fragment(R.layout.fragment_new_info) {

    private val TAG = "NewInfoFragment"
    private lateinit var viewModel: NewInfoViewModel
    private var isChipGroupChecked = false

    @Inject
    lateinit var localDataSource: LocalDataSource

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        inject()
        configureViewModel()
        configureClickListeners()
        setupObservers()

        spnMaterialStyle.adapter = ArrayAdapter(
            requireContext(),
            R.layout.support_simple_spinner_dropdown_item,
            viewModel.materialStyles
        )
    }

    override fun onResume() {
        super.onResume()
        btnAddMaterial.makeClickable(false)
        btnAddRequest.makeClickable(false)
    }

    private fun setupObservers() {
        viewModel.partners.observe(viewLifecycleOwner) {
            val adapter =
                ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, it)
            spnRequestPartners.adapter = adapter
        }
        viewModel.materialTypes.observe(viewLifecycleOwner) {
            val adapter =
                ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, it)
            etMaterialType.setAdapter(adapter)
        }
        viewModel.insertionMaterialState.observe(viewLifecycleOwner) {
            if (it) {
                Toast.makeText(context, R.string.new_material_created, Toast.LENGTH_SHORT).show()
                etMaterialType.setText("")
                staticViewMaterial.performClick()
            } else {
                Toast.makeText(context, R.string.error_material_created, Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.insertionRequestState.observe(viewLifecycleOwner) {
            if (it) {
                Toast.makeText(context, R.string.new_request_created, Toast.LENGTH_SHORT).show()
                etRequestMaterialId.setText("")
                etRequestQuantity.setText("")
            } else {
                Toast.makeText(context, R.string.error_request_created, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun configureClickListeners() {
        val onTextChangedListener = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                val str1 = etRequestMaterialId.text.toString()
                val str2 = etRequestQuantity.text.toString()
                if (str1.isEmpty() || str2.isEmpty() || !isChipGroupChecked) {
                    btnAddRequest.makeClickable(false)
                } else if (str1.isNotEmpty() && str2.isNotEmpty() && isChipGroupChecked) {
                    btnAddRequest.makeClickable(true)
                }
            }
        }
        etRequestMaterialId.addTextChangedListener(onTextChangedListener)
        etRequestQuantity.addTextChangedListener(onTextChangedListener)
        etMaterialType.addTextChangedListener {
            val str = it.toString()
            if (str.length <= 2) {
                btnAddMaterial.makeClickable(false)
            } else {
                btnAddMaterial.makeClickable(true)
            }
        }

        btnAddMaterial.setOnClickListener {
            val type = etMaterialType.text.toString().toLowerCase(Locale.ROOT)
            val style = Style.valueOf(spnMaterialStyle.selectedItem.toString())
            viewModel.insertMaterial(Material(type, style, 0))
            hideKeyboard()
        }
        btnAddRequest.setOnClickListener {
            val id: Long
            val quantity: Int
            try {
                id = etRequestMaterialId.text.toString().toLong()
                quantity = etRequestQuantity.text.toString().toInt()
            } catch (e: NumberFormatException) {
                Toast.makeText(context, R.string.error_request_created, Toast.LENGTH_SHORT).show()
                Log.d(TAG, "configureClickListeners: ${e.message}")
                return@setOnClickListener
            }
            val partner = spnRequestPartners.selectedItem.toString()

            when (cgRequestType.checkedChipId) {
                R.id.chipGivingRequest -> {
                    val request = GivingRequest(id, quantity, partner, System.currentTimeMillis())
                    viewModel.insertGivingRequest(request)
                }
                R.id.chipTakingRequest -> {
                    val request = TakingRequest(id, quantity, partner, System.currentTimeMillis())
                    viewModel.insertTakingRequest(request)
                }
                else -> {
                    Toast.makeText(context, R.string.error_request_created, Toast.LENGTH_SHORT)
                        .show()
                }
            }
            hideKeyboard()
        }
        cgRequestType.setOnCheckedChangeListener { _, i ->
            when (i) {
                R.id.chipGivingRequest, R.id.chipTakingRequest -> {
                    isChipGroupChecked = true
                    val str1 = etRequestMaterialId.text.toString()
                    val str2 = etRequestQuantity.text.toString()
                    if (str1.isNotEmpty() && str2.isNotEmpty()) {
                        btnAddRequest.makeClickable(true)
                    }
                }
                else -> {
                    isChipGroupChecked = false
                    btnAddRequest.makeClickable(false)
                }
            }
        }

        staticViewMaterial.setOnClickListener {
            if (expandableViewMaterial.visibility == View.GONE) {
                expandableViewMaterial.visibility = View.VISIBLE
                ivExpandMaterial.setImageResource(R.drawable.ic_expand_less)
                tvClickToTabMaterial.text = getString(R.string.click_here_to_close_the_tab)
            } else {
                expandableViewMaterial.visibility = View.GONE
                ivExpandMaterial.setImageResource(R.drawable.ic_expand_more)
                tvClickToTabMaterial.text = getString(R.string.click_here_to_open_the_tab)
            }
        }
        staticViewRequest.setOnClickListener {
            if (expandableViewRequest.visibility == View.GONE) {
                expandableViewRequest.visibility = View.VISIBLE
                ivExpandRequest.setImageResource(R.drawable.ic_expand_less)
                tvClickToTabRequest.text = getString(R.string.click_here_to_close_the_tab)
            } else {
                expandableViewRequest.visibility = View.GONE
                ivExpandRequest.setImageResource(R.drawable.ic_expand_more)
                tvClickToTabRequest.text = getString(R.string.click_here_to_open_the_tab)
            }
        }
    }

    private fun hideKeyboard() = (requireActivity() as MainActivity).hideKeyBoard()

    private fun configureViewModel() {
        val factory = NewInfoViewModelFactory(localDataSource)
        viewModel = ViewModelProvider(this, factory).get(NewInfoViewModel::class.java)
    }

    private fun inject() {
        val app = requireActivity().application as MyApplication
        app.appComponent.inject(this)
    }
}