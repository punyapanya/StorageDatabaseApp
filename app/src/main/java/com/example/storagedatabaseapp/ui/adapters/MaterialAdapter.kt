package com.example.storagedatabaseapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.storagedatabaseapp.R
import com.example.storagedatabaseapp.data.db.entities.Material
import kotlinx.android.synthetic.main.item_material.view.*

class MaterialAdapter : ListAdapter<Material, MaterialAdapter.MaterialViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MaterialViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_material, parent, false),
        itemClickListener
    )

    override fun onBindViewHolder(holder: MaterialViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    var itemClickListener: (Material) -> Unit = { /* no-op */ }

    class MaterialViewHolder(
        private val itemView: View,
        private val itemClickListener: (Material) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(material: Material) {
            itemView.apply {
                tvItemMaterial.text = resources.getString(
                    R.string.material_name_style_template, material.type, material.style.name
                )
                tvItemQuantity.text = resources.getString(
                    R.string.material_quantity_template, material.quantity
                )
                tvItemId.text = resources.getString(
                    R.string.material_id_template, material.materialId.toInt()
                )
                setOnClickListener { itemClickListener(material) }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Material>() {
            override fun areItemsTheSame(oldItem: Material, newItem: Material) =
                oldItem.materialId == newItem.materialId

            override fun areContentsTheSame(oldItem: Material, newItem: Material) =
                oldItem == newItem
        }
    }
}