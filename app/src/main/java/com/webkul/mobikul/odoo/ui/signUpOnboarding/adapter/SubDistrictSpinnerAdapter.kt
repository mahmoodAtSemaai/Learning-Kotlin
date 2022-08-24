package com.webkul.mobikul.odoo.ui.signUpOnboarding.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.data.entity.address.SubDistrictEntity
import com.webkul.mobikul.odoo.databinding.ItemSearchableSpinnerBinding

class SubDistrictSpinnerAdapter(
    private var addressList: List<SubDistrictEntity>,
    private val spinnerItemClickInterface: SubDistrictRecyclerViewClickInterface
) :
    RecyclerView.Adapter<SubDistrictSpinnerAdapter.SubDistrictSpinnerViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SubDistrictSpinnerAdapter.SubDistrictSpinnerViewHolder {
        return SubDistrictSpinnerViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_searchable_spinner, parent, false)
        )
    }

    override fun onBindViewHolder(holder: SubDistrictSpinnerViewHolder, position: Int) {
        holder.onBind(position)
    }

    override fun getItemCount(): Int {
        return addressList.size
    }

    fun filterList(filteredList: List<SubDistrictEntity>) {
        addressList = filteredList
        notifyDataSetChanged()
    }

    inner class SubDistrictSpinnerViewHolder constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val binding: ItemSearchableSpinnerBinding = DataBindingUtil.bind(itemView)!!
        fun onBind(position: Int) {
            binding.tvItemValue.apply {
                text = addressList[position].name
                setOnClickListener {
                    spinnerItemClickInterface.subDistrictOnItemClick(position, addressList)
                }
            }
        }
    }
}

interface SubDistrictRecyclerViewClickInterface {
    fun subDistrictOnItemClick(position: Int, addressList: List<SubDistrictEntity>)
}