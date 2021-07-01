package com.fmk.monitoring.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fmk.monitoring.data.Address
import com.fmk.monitoring.databinding.ItemAddrBinding
import com.fmk.monitoring.view.AddressViewHolder

class AddressAdapter (val context: Context, val addrList: List<Address>) : RecyclerView.Adapter<AddressViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val binding = ItemAddrBinding.inflate(LayoutInflater.from(context), parent, false)
        return AddressViewHolder(binding);
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        val item = addrList[position]
        holder.apply {
            bind(item)
        }
    }

    override fun getItemCount(): Int {
        Log.e("getItemCount", "itemList.size ==> " + addrList.size)
        return addrList.size
    }

}