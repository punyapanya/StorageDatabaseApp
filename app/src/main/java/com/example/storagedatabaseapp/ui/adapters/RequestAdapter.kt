package com.example.storagedatabaseapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.storagedatabaseapp.R
import com.example.storagedatabaseapp.data.db.entities.GivingRequest
import kotlinx.android.synthetic.main.item_request.view.*
import java.sql.Timestamp

class RequestAdapter : ListAdapter<GivingRequest, RequestAdapter.RequestViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = RequestViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_request, parent, false)
    )

    override fun onBindViewHolder(holder: RequestViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class RequestViewHolder(
        private val itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(request: GivingRequest) {
            itemView.apply {
                tvItemRequestedId.text =
                    resources.getString(R.string.request_requested_id, request.requestedMaterialId)
                tvItemFrom.text =
                    resources.getString(R.string.request_from_template, request.requestedFrom)
                tvItemRequestedQuantity.text =
                    resources.getString(R.string.request_quantity_template, request.quantity)

                val date = Timestamp(request.date).toString().substring(0..18)
                tvItemDate.text =
                    resources.getString(R.string.request_date_template, date)

                val status = if (request.status) "confirmed" else "refused"
                tvItemStatus.text =
                    resources.getString(R.string.request_status_template, status)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<GivingRequest>() {
            override fun areItemsTheSame(oldItem: GivingRequest, newItem: GivingRequest) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: GivingRequest, newItem: GivingRequest) =
                oldItem == newItem
        }
    }
}