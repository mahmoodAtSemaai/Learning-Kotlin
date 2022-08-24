package com.webkul.mobikul.odoo.ui.signUpOnboarding.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.data.entity.address.StateEntity
import com.webkul.mobikul.odoo.databinding.ItemSearchableSpinnerBinding

class ProvinceSpinnerAdapter(
    private var addressList: List<StateEntity>,
    private val spinnerItemClickInterface: ProvinceRecyclerViewClickInterface
) :
    RecyclerView.Adapter<ProvinceSpinnerAdapter.ProvinceSpinnerViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProvinceSpinnerAdapter.ProvinceSpinnerViewHolder {
        return ProvinceSpinnerViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_searchable_spinner, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ProvinceSpinnerViewHolder, position: Int) {
        holder.onBind(position)
    }

    override fun getItemCount(): Int {
        return addressList.size
    }

    fun filterList(filteredList: List<StateEntity>) {
        addressList = filteredList
        notifyDataSetChanged()
    }

    inner class ProvinceSpinnerViewHolder constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val binding: ItemSearchableSpinnerBinding = DataBindingUtil.bind(itemView)!!
        fun onBind(position: Int) {
            binding.tvItemValue.apply {
                text = addressList[position].name
                setOnClickListener {
                    spinnerItemClickInterface.provinceOnItemClick(position, addressList)
                }
            }
        }
    }
}

interface ProvinceRecyclerViewClickInterface {
    fun provinceOnItemClick(position: Int, addressList: List<StateEntity>)
}