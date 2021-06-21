package com.example.storagedatabaseapp.ui.fragments.requests

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storagedatabaseapp.MyApplication
import com.example.storagedatabaseapp.R
import com.example.storagedatabaseapp.data.datasource.LocalDataSource
import com.example.storagedatabaseapp.ui.adapters.RequestAdapter
import com.example.storagedatabaseapp.ui.fragments.materialdetail.BtnRequestGroupState
import com.example.storagedatabaseapp.util.makeClickable
import kotlinx.android.synthetic.main.fragment_requests.*
import javax.inject.Inject

class RequestsFragment : Fragment(R.layout.fragment_requests) {

    private val TAG = "RequestsFragment"
    private lateinit var rvAdapter: RequestAdapter
    private lateinit var viewModel: RequestsViewModel

    @Inject
    lateinit var localDataSource: LocalDataSource

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
        viewModel.btnGroupState.observe(viewLifecycleOwner) {
            when (it) {
                BtnRequestGroupState.ORDER -> {
                    btnRequestsAllOrder.makeClickable(false)
                    btnRequestsAllReceiving.makeClickable(true)
                }
                BtnRequestGroupState.RECEIVING -> {
                    btnRequestsAllReceiving.makeClickable(false)
                    btnRequestsAllOrder.makeClickable(true)
                }
                BtnRequestGroupState.LOADING -> {
                    btnRequestsAllReceiving.isSelected = false
                    btnRequestsAllReceiving.isClickable = false
                    btnRequestsAllOrder.isSelected = false
                    btnRequestsAllOrder.isClickable = false
                }
                BtnRequestGroupState.NOTHING -> {
                }
            }
        }
    }

    private val btnListener = View.OnClickListener { view ->
        when (view.id) {
            btnRequestsAllOrder.id -> viewModel.getRequestsOrder()
            btnRequestsAllReceiving.id -> viewModel.getRequestsReceiving()
        }
    }

    private fun configureClickListeners() {
        btnRequestsAllOrder.setOnClickListener(btnListener)
        btnRequestsAllReceiving.setOnClickListener(btnListener)
    }

    private fun configureRvAdapter() {
        rvAdapter = RequestAdapter()
        rvRequestsAll.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvRequestsAll.adapter = rvAdapter
    }

    private fun configureViewModel() {
        val factory = RequestsViewModelFactory(localDataSource)
        viewModel = ViewModelProvider(this, factory).get(RequestsViewModel::class.java)
    }

    private fun inject() {
        val app = requireActivity().application as MyApplication
        app.appComponent.inject(this)
    }
}