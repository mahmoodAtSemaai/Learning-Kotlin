package com.webkul.mobikul.odoo.ui.signUpOnboarding.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.data.entity.address.DistrictEntity
import com.webkul.mobikul.odoo.databinding.ItemSearchableSpinnerBinding

class DistrictSpinnerAdapter(
    private var addressList: List<DistrictEntity>,
    private val spinnerItemClickInterface: DistrictRecyclerViewClickInterface
) :
    RecyclerView.Adapter<DistrictSpinnerAdapter.DistrictSpinnerViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DistrictSpinnerAdapter.DistrictSpinnerViewHolder {
        return DistrictSpinnerViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_searchable_spinner, parent, false)
        )
    }

    override fun onBindViewHolder(holder: DistrictSpinnerViewHolder, position: Int) {
        holder.onBind(position)
    }

    override fun getItemCount(): Int {
        return addressList.size
    }

    fun filterList(filteredList: List<DistrictEntity>) {
        addressList = filteredList
        notifyDataSetChanged()
    }

    inner class DistrictSpinnerViewHolder constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val binding: ItemSearchableSpinnerBinding = DataBindingUtil.bind(itemView)!!
        fun onBind(position: Int) {
            binding.tvItemValue.apply {
                text = addressList[position].name
                setOnClickListener {
                    spinnerItemClickInterface.districtOnItemClick(position, addressList)
                }
            }
        }
    }
}

interface DistrictRecyclerViewClickInterface {
    fun districtOnItemClick(position: Int, addressList: List<DistrictEntity>)
}