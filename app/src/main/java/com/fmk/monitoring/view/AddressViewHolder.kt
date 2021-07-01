package com.fmk.monitoring.view

import androidx.recyclerview.widget.RecyclerView
import com.fmk.monitoring.data.Address
import com.fmk.monitoring.databinding.ItemAddrBinding

class AddressViewHolder : RecyclerView.ViewHolder {
    var binding: ItemAddrBinding
    constructor(binding: ItemAddrBinding) : super(binding.root) {
        this.binding = binding
    }

    fun bind(item: Address) {
        binding.txtAptName.text = item.apt
        binding.txtAddress.text = item.addr
    }

}
