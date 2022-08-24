package com.webkul.mobikul.odoo.ui.signUpOnboarding.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.data.entity.CustomerGroupEntity
import com.webkul.mobikul.odoo.databinding.ItemUserOnboardingCategoryBinding

class CustomerGroupAdapter(
    private val customerGroupEntityList: List<CustomerGroupEntity>,
    private val customerGroupRvClickInterface: CustomerGroupRecyclerViewClickInterface
) :
    RecyclerView.Adapter<CustomerGroupAdapter.CustomerGroupViewHolder>() {
    val EMPTY_ITEM_VALUE = -1
    var selectedItemPos = EMPTY_ITEM_VALUE
    var lastItemSelectedPos = EMPTY_ITEM_VALUE

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CustomerGroupAdapter.CustomerGroupViewHolder {
        return CustomerGroupViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_user_onboarding_category, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CustomerGroupViewHolder, position: Int) {
        if (position == selectedItemPos)
            holder.selectedBg()
        else
            holder.defaultBg()
        holder.onBind(position)
    }

    override fun getItemCount(): Int {
        return customerGroupEntityList.size
    }

    inner class CustomerGroupViewHolder constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val binding: ItemUserOnboardingCategoryBinding = DataBindingUtil.bind(itemView)!!

        init {
            binding.btnCategory.setOnClickListener {
                selectedItemPos = adapterPosition
                lastItemSelectedPos = if (lastItemSelectedPos == EMPTY_ITEM_VALUE)
                    selectedItemPos
                else {
                    notifyItemChanged(lastItemSelectedPos)
                    selectedItemPos
                }
                //TODO handle with MVI
                notifyItemChanged(selectedItemPos)
                customerGroupRvClickInterface.onItemClick(adapterPosition, customerGroupEntityList)
            }
        }

        fun onBind(position: Int) {
            binding.btnCategory.text = customerGroupEntityList[position].name
        }

        fun defaultBg() {
            binding.btnCategory.apply {
                background = ContextCompat.getDrawable(context, R.drawable.btn_category_disable)
                setTextColor(ContextCompat.getColor(context, R.color.colorDarkGrey))
            }
        }

        fun selectedBg() {
            binding.btnCategory.apply {
                background = ContextCompat.getDrawable(context, R.drawable.btn_enabled)
                setTextColor(ContextCompat.getColor(context, R.color.background_orange))
            }
        }
    }
}

interface CustomerGroupRecyclerViewClickInterface {
    fun onItemClick(position: Int, customerGroupsList: List<CustomerGroupEntity>)
}