package com.webkul.mobikul.odoo.ui.signUpOnboarding.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.data.entity.address.VillageEntity
import com.webkul.mobikul.odoo.databinding.ItemSearchableSpinnerBinding

class VillageSpinnerAdapter(
    private var addressList: List<VillageEntity>,
    private val spinnerItemClickInterface: VillageRecyclerViewClickInterface
) :
    RecyclerView.Adapter<VillageSpinnerAdapter.VillageSpinnerViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VillageSpinnerAdapter.VillageSpinnerViewHolder {
        return VillageSpinnerViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_searchable_spinner, parent, false)
        )
    }

    override fun onBindViewHolder(holder: VillageSpinnerViewHolder, position: Int) {
        holder.onBind(position)
    }

    override fun getItemCount(): Int {
        return addressList.size
    }

    fun filterList(filteredList: List<VillageEntity>) {
        addressList = filteredList
        notifyDataSetChanged()
    }

    inner class VillageSpinnerViewHolder constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val binding: ItemSearchableSpinnerBinding = DataBindingUtil.bind(itemView)!!
        fun onBind(position: Int) {
            binding.tvItemValue.apply {
                text = addressList[position].name
                setOnClickListener {
                    spinnerItemClickInterface.villageOnItemClick(position, addressList)
                }
            }
        }
    }
}

interface VillageRecyclerViewClickInterface {
    fun villageOnItemClick(position: Int, addressList: List<VillageEntity>)
}