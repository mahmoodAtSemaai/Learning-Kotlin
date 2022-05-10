package com.webkul.mobikul.odoo.adapter.home

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.webkul.mobikul.odoo.fragment.CategoryProductFragment
import com.webkul.mobikul.odoo.model.generic.FeaturedCategoryData

class CategoryProductAdapter( activity : AppCompatActivity ,  var list : List<FeaturedCategoryData>) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = list.size

    override fun createFragment(position: Int): Fragment =
      CategoryProductFragment.newInstance(position , list[position].categoryId)

}