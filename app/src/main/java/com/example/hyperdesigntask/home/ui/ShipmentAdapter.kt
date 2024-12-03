package com.example.hyperdesigntask.home.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hyperdesigntask.data.model.Shipment
import com.example.hyperdesigntask.databinding.ItemShipmentBinding

class ShipmentAdapter(
    private val onShipmentClickListener: OnShipmentClickListener
) : RecyclerView.Adapter<ShipmentAdapter.ShipmentViewHolder>() {

    private val shipmentItems = mutableListOf<Shipment>()

    class ShipmentViewHolder(val binding: ItemShipmentBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShipmentViewHolder {
        val binding = ItemShipmentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ShipmentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShipmentViewHolder, position: Int) {
        val shipment = shipmentItems[position]
        holder.binding.shipment = shipment
        holder.binding.executePendingBindings()

        holder.binding.root.setOnClickListener {
            onShipmentClickListener.onShipmentClick(shipment)
        }
    }

    override fun getItemCount(): Int = shipmentItems.size

    fun setShipmentItems(newShipments: List<Shipment>) {
        shipmentItems.clear()
        shipmentItems.addAll(newShipments)
        notifyDataSetChanged()
    }
}

interface OnShipmentClickListener {
    fun onShipmentClick(shipment: Shipment)
}