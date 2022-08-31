package com.webkul.mobikul.odoo.ui.cart

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.activity.NewDrawerActivity
import com.webkul.mobikul.odoo.constant.BundleConstant
import com.webkul.mobikul.odoo.core.extension.inTransaction
import com.webkul.mobikul.odoo.databinding.ActivityNewCartBinding
import com.webkul.mobikul.odoo.fragment.WishlistFragment
import com.webkul.mobikul.odoo.model.home.HomePageResponse

class NewCartActivity : AppCompatActivity() {

    private lateinit var binding : ActivityNewCartBinding

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_cart)
        setToolbar()
        setBackStackChangedListener()
        setUpCartFragment()
        setClickListeners()
    }

    private fun setBackStackChangedListener() {
        supportFragmentManager.addOnBackStackChangedListener {
            val currentFragment = supportFragmentManager.fragments.last()

            if(currentFragment is WishlistFragment){
                setToolbarTitle(getString(R.string.wishlist))
                //setIcon(this.getCompatDrawable(R.drawable.ic_cart_new)) //TODO: uncomment after withListIcon is fixed
                return@addOnBackStackChangedListener
            }
            //Else for CartFragment and EmptyCart Fragment
            else {
                setToolbarTitle(getString(R.string.bag))
                //setIcon(this.getCompatDrawable(R.drawable.ic_wishlist)) //TODO: uncomment after withListIcon is fixed
            }
        }
    }

    //TODO: Need it after withListIcon is fixed
    private fun setIcon(compatDrawable: Drawable?) {
        binding.ivWishList.setImageDrawable(compatDrawable)
    }


    private fun setClickListeners() {
        binding.apply {
            //TODO: handle click on wishlist icon later
            /**
            ivWishList.setOnClickListener {
                if(isWishListFragmentVisible())
                    supportFragmentManager.popBackStack()
                else
                    setUpWishListFragment()
            }
            **/

            ivDrawerCart.setOnClickListener {
                startActivity(Intent(this@NewCartActivity, NewDrawerActivity::class.java)
                    .putExtra(BundleConstant.BUNDLE_KEY_HOME_PAGE_RESPONSE, getHomePageResponse()))
            }
        }
    }

    private fun isWishListFragmentVisible(): Boolean {
        val fragment: WishlistFragment? =
            supportFragmentManager.findFragmentByTag(
                WishlistFragment::class.java.simpleName) as WishlistFragment?
        return fragment != null && fragment.isVisible
    }

    private fun setUpWishListFragment() {

        //TODO: uncomment after withListIcon is fixed
        //setIcon(this.getCompatDrawable(R.drawable.ic_cart_new))
        setToolbarTitle(getString(R.string.wishlist))
        supportFragmentManager.beginTransaction()
            .add(
                R.id.fl_fragment_container_cart,
                WishlistFragment.newInstance(),
                WishlistFragment::class.java.simpleName
            ).addToBackStack(WishlistFragment::class.java.simpleName).commit()
    }

    private fun setUpCartFragment() {
        //setIcon(this.getCompatDrawable(R.drawable.ic_wishlist)) //TODO: uncomment after withListIcon is fixed
        setToolbarTitle(getString(R.string.bag))
        supportFragmentManager.inTransaction {
            add(R.id.fl_fragment_container_cart,
                CartFragment.newInstance(),
                CartFragment::class.java.simpleName)
        }
    }

    fun setUpEmptyCartFragment(){
        supportFragmentManager.inTransaction {
            add(R.id.fl_fragment_container_cart,
                EmptyFragmentV1.newInstance(
                    getString(R.string.empty_bag),getString(R.string.add_item_to_your_bag_now),
                    R.drawable.ic_empty_cart),
                EmptyFragmentV1::class.java.simpleName)
            }
    }


    fun getHomePageResponse(): HomePageResponse? = intent.getParcelableExtra(BundleConstant.BUNDLE_KEY_HOME_PAGE_RESPONSE)


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setToolbar() {
        window.statusBarColor = ContextCompat.getColor(this, R.color.background_appbar_color)
        setSupportActionBar(binding.tbCart)
        supportActionBar?.setDisplayShowTitleEnabled(false);
        setToolbarBackButtonVisibility(true)
        setToolbarTitle(getString(R.string.bag))
    }

    private fun setToolbarBackButtonVisibility(isVisible: Boolean) {
        supportActionBar?.setDisplayHomeAsUpEnabled(isVisible);
        supportActionBar?.setDisplayShowHomeEnabled(isVisible);
        supportActionBar?.setHomeButtonEnabled(true)
    }

    private fun setToolbarTitle(title: String) {
        binding.tvTitle.text = title
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


}
