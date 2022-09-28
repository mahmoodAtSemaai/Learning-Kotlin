package com.webkul.mobikul.odoo.ui.home

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.webkul.mobikul.odoo.data.entity.ProductCategoryEntity
import com.webkul.mobikul.odoo.fragment.CategoryProductFragment
import com.webkul.mobikul.odoo.ui.category.CategoryProductFragmentV1

class CategoryProductAdapterV1(activity : AppCompatActivity, var list : List<ProductCategoryEntity>) : FragmentStateAdapter(activity) {
	
	override fun getItemCount() : Int = list.size
	override fun createFragment(position : Int) : Fragment = CategoryProductFragmentV1.newInstance(position, list[position].categoryId.toString())
	
}