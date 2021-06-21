package com.example.storagedatabaseapp.ui.fragments.materialdetail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storagedatabaseapp.MyApplication
import com.example.storagedatabaseapp.R
import com.example.storagedatabaseapp.data.datasource.LocalDataSource
import com.example.storagedatabaseapp.ui.adapters.RequestAdapter
import com.example.storagedatabaseapp.util.makeClickable
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_material_detail.*
import javax.inject.Inject

class MaterialDetailFragment : Fragment(R.layout.fragment_material_detail) {

    private val TAG = "MaterialDetailFragment"
    private val args: MaterialDetailFragmentArgs by navArgs()
    private lateinit var rvAdapter: RequestAdapter
    private lateinit var viewModel: MaterialDetailViewModel

    @Inject
    lateinit var localDataSource: LocalDataSource

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().bottomNavigationView.visibility = View.GONE
        inject()
        configureRvAdapter()
        configureClickListeners()
        configureViewModel()
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.requests.observe(viewLifecycleOwner) {
            rvAdapter.submitList(it)
        }
        viewModel.material.observe(viewLifecycleOwner) { material ->
            tvDetailMaterial.text = getString(
                R.string.material_name_style_template, material.type, material.style.name
            )
            tvDetailQuantity.text = getString(
                R.string.material_quantity_template, material.quantity
            )
            tvDetailId.text = getString(
                R.string.material_id_template, material.materialId.toInt()
            )
        }
        viewModel.btnGroupState.observe(viewLifecycleOwner) {
            when (it) {
                BtnRequestGroupState.ORDER -> {
                    btnRequestsOrder.makeClickable(false)
                    btnRequestsReceiving.makeClickable(true)
                }
                BtnRequestGroupState.RECEIVING -> {
                    btnRequestsReceiving.makeClickable(false)
                    btnRequestsOrder.makeClickable(true)
                }
                BtnRequestGroupState.LOADING -> {
                    btnRequestsReceiving.isSelected = false
                    btnRequestsReceiving.isClickable = false
                    btnRequestsOrder.isSelected = false
                    btnRequestsOrder.isClickable = false
                }
                BtnRequestGroupState.NOTHING -> {
                }
            }
        }
    }

    private val btnListener = View.OnClickListener { view ->
        when (view.id) {
            btnRequestsOrder.id -> viewModel.getRequestsOrder()
            btnRequestsReceiving.id -> viewModel.getRequestsReceiving()
        }
    }

    private fun configureClickListeners() {
        btnRequestsOrder.setOnClickListener(btnListener)
        btnRequestsReceiving.setOnClickListener(btnListener)
    }

    private fun configureRvAdapter() {
        rvAdapter = RequestAdapter()
        rvRequests.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvRequests.adapter = rvAdapter
    }

    private fun configureViewModel() {
        val factory = MaterialDetailViewModelFactory(localDataSource, args.materialId)
        viewModel = ViewModelProvider(this, factory).get(MaterialDetailViewModel::class.java)
    }

    private fun inject() {
        val app = requireActivity().application as MyApplication
        app.appComponent.inject(this)
    }
}

enum class BtnRequestGroupState {
    ORDER,
    RECEIVING,
    LOADING,
    NOTHING
}