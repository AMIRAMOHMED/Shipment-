package com.example.hyperdesigntask.home.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hyperdesigntask.data.model.ContainerDetails
import com.example.hyperdesigntask.databinding.ItemContainerBinding

class ContainerAdapter : RecyclerView.Adapter<ContainerAdapter.ContainerViewHolder>() {

    private val containers = mutableListOf<ContainerDetails>()

    class ContainerViewHolder(val binding: ItemContainerBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContainerViewHolder {
        val binding = ItemContainerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContainerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContainerViewHolder, position: Int) {
        val container = containers[position]
        holder.binding.container = container
        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int = containers.size

    fun setContainers(newContainers: List<ContainerDetails>) {
        containers.clear()
        containers.addAll(newContainers)
        notifyDataSetChanged()
    }
}