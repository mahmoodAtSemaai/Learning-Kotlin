package com.webkul.mobikul.odoo.ui.onboarding

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.PagerAdapter
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.databinding.FragmentOnboardingBinding
import com.webkul.mobikul.odoo.data.entity.OnboardingData

class OnboardingPagerAdapter(val context: Context, val list: MutableList<OnboardingData>) :
        PagerAdapter() {

    override fun getCount(): Int = list.size

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = LayoutInflater.from(context)
        val binding: FragmentOnboardingBinding = DataBindingUtil.bind(
                inflater.inflate(
                        R.layout.fragment_onboarding, container, false
                )
        )!!
        binding.ivOnboarding.setImageResource(context.resources.getIdentifier(list[position].drawableName, "drawable", "com.semaai.toko"))
        binding.tvHeading.text = list[position].titleText
        binding.tvDescription.text = list[position].descriptionText
        container.addView(binding.root)
        return binding.root
    }


    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }

}