package com.webkul.mobikul.odoo.ui.cart

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.activity.NewHomeActivity
import com.webkul.mobikul.odoo.constant.BundleConstant
import com.webkul.mobikul.odoo.core.extension.getCompatDrawable
import com.webkul.mobikul.odoo.core.extension.makeGone
import com.webkul.mobikul.odoo.core.extension.makeVisible
import com.webkul.mobikul.odoo.core.platform.BindingBaseFragment
import com.webkul.mobikul.odoo.databinding.FragmentEmptyV1Binding
import com.webkul.mobikul.odoo.model.home.HomePageResponse
import dagger.hilt.android.AndroidEntryPoint

class EmptyFragmentV1 : BindingBaseFragment<FragmentEmptyV1Binding>() {

    override val layoutId: Int
        get() = R.layout.fragment_empty_v1


    companion object {
        @JvmStatic
        fun newInstance(
            title: String,
            description: String,
            drawable: Int,
            hideButton : Boolean
        ) =
            EmptyFragmentV1().apply {
                val bundle = Bundle()
                bundle.apply {
                    putString(BundleConstant.BUNDLE_KEY_EMPTY_FRAGMENT_TITLE_ID, title)
                    putString(BundleConstant.BUNDLE_KEY_EMPTY_FRAGMENT_SUBTITLE_ID, description)
                    putInt(BundleConstant.BUNDLE_KEY_EMPTY_FRAGMENT_DRAWABLE_ID, drawable)
                    putBoolean(BundleConstant.BUNDLE_KEY_EMPTY_FRAGMENT_HIDE_CONTINUE_SHOPPING_BTN, hideButton)
                }
                arguments = bundle
            }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getArgs(arguments)
        setClickListeners()
    }

    private fun setClickListeners() {
        binding.apply {
            btnRedirectToHome.setOnClickListener {
                startActivity(Intent(requireActivity(), NewHomeActivity::class.java)
                    .putExtra(BundleConstant.BUNDLE_KEY_HOME_PAGE_RESPONSE, (requireActivity() as NewCartActivity).getHomePageResponse()))
            }
        }
    }

    private fun getArgs(arguments: Bundle?) {
        binding.apply {
            tvEmptyCartTitle.text = arguments?.getString(BundleConstant.BUNDLE_KEY_EMPTY_FRAGMENT_TITLE_ID)
            tvEmptyCartDescription.text = arguments?.getString(BundleConstant.BUNDLE_KEY_EMPTY_FRAGMENT_SUBTITLE_ID)
            ivEmptyCart.setImageDrawable(requireContext().getCompatDrawable(arguments?.getInt(BundleConstant.BUNDLE_KEY_EMPTY_FRAGMENT_DRAWABLE_ID)!!))
            val hideButton = arguments.getBoolean(BundleConstant.BUNDLE_KEY_EMPTY_FRAGMENT_HIDE_CONTINUE_SHOPPING_BTN)
            if(hideButton)
                btnRedirectToHome.makeGone()
            else
                btnRedirectToHome.makeVisible()
        }
    }


}