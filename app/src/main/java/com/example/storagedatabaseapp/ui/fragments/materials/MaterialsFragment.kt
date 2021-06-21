package com.example.storagedatabaseapp.ui.fragments.materials

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storagedatabaseapp.MyApplication
import com.example.storagedatabaseapp.R
import com.example.storagedatabaseapp.data.datasource.LocalDataSource
import com.example.storagedatabaseapp.ui.adapters.MaterialAdapter
import com.example.storagedatabaseapp.ui.dialogs.ListDialogFragment
import com.example.storagedatabaseapp.util.makeClickable
import kotlinx.android.synthetic.main.fragment_materials.*
import javax.inject.Inject

class MaterialsFragment : Fragment(R.layout.fragment_materials) {

    private val TAG = "MaterialsFragment"
    private lateinit var btnGroup: List<Button>
    private lateinit var rvAdapter: MaterialAdapter
    private lateinit var viewModel: MaterialsViewModel

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

    private val btnListener = View.OnClickListener { view ->
        when (view.id) {
            btnMaterialsAll.id -> showListDialogFragment()
            btnMaterialsNotOrdered.id -> viewModel.getMaterialsNotOrdered()
            btnMaterialsWithRefuses.id -> viewModel.getMaterialsWithRefuses()
        }
    }

    private fun showListDialogFragment() {
        ListDialogFragment(
            R.string.dialog_title_choose_materials,
            R.array.dialog_materials_choices
        ) {
            Log.d(TAG, "btnListener: btn clicked with parameter: $it")
            when (it) {
                "All" -> viewModel.getMaterials()
                "In stock" -> viewModel.getMaterials(minQuantity = 1)
                "Not in stock" -> viewModel.getMaterials(maxQuantity = 0)
            }
        }.show(requireActivity().supportFragmentManager, "ChooseMaterialsDialogFragment")
    }

    private fun setupObservers() {
        viewModel.materials.observe(viewLifecycleOwner) {
            rvAdapter.submitList(it)
        }
        viewModel.btnGroupStates.observe(viewLifecycleOwner) {
            when (it) {
                BtnGroupState.ALL -> {
                    showBtnGroupStates(btnMaterialsAll)
                    btnMaterialsAll.text = getString(R.string.all)
                    btnMaterialsAll.isClickable = true
                }
                BtnGroupState.IN_STOCK -> {
                    showBtnGroupStates(btnMaterialsAll)
                    btnMaterialsAll.text = getString(R.string.in_stock)
                    btnMaterialsAll.isClickable = true
                }
                BtnGroupState.NOT_IN_STOCK -> {
                    showBtnGroupStates(btnMaterialsAll)
                    btnMaterialsAll.text = getString(R.string.not_in_stock)
                    btnMaterialsAll.isClickable = true
                }
                BtnGroupState.NOT_ORDERED -> {
                    showBtnGroupStates(btnMaterialsNotOrdered)
                }
                BtnGroupState.WITH_REFUSES -> {
                    showBtnGroupStates(btnMaterialsWithRefuses)
                }
                BtnGroupState.LOADING -> {
                    for (itemBtn in btnGroup) {
                        itemBtn.isClickable = false
                    }
                }
                BtnGroupState.NOTHING -> {
                    for (itemBtn in btnGroup) {
                        itemBtn.makeClickable(true)
                    }
                    btnMaterialsAll.text = getString(R.string.all)
                }
            }
        }
    }

    private fun showBtnGroupStates(selectedBtn: Button) {
        if (selectedBtn.id != btnMaterialsAll.id) {
            btnMaterialsAll.text = getString(R.string.all)
        }
        selectedBtn.makeClickable(false)
        for (itemBtn in btnGroup) {
            if (selectedBtn.id != itemBtn.id) {
                itemBtn.makeClickable(true)
            }
        }
    }

    private fun configureClickListeners() {
        btnGroup = listOf<Button>(btnMaterialsAll, btnMaterialsNotOrdered, btnMaterialsWithRefuses)
        btnGroup.forEach { it.setOnClickListener(btnListener) }
    }

    private fun configureRvAdapter() {
        rvAdapter = MaterialAdapter()
        rvAdapter.itemClickListener = {
            val action =
                MaterialsFragmentDirections.actionMaterialsFragmentToMaterialDetailFragment(it.materialId)
            findNavController().navigate(action)
        }
        rvMaterials.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvMaterials.adapter = rvAdapter
    }

    private fun configureViewModel() {
        val factory = MaterialsViewModelFactory(localDataSource)
        viewModel = ViewModelProvider(this, factory).get(MaterialsViewModel::class.java)
    }

    private fun inject() {
        val app = requireActivity().application as MyApplication
        app.appComponent.inject(this)
    }
}

enum class BtnGroupState {
    ALL,
    IN_STOCK,
    NOT_IN_STOCK,
    NOT_ORDERED,
    WITH_REFUSES,
    LOADING,
    NOTHING
}