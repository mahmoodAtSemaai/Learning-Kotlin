package com.webkul.mobikul.odoo.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.adapter.home.NavDrawerCategoryStartRvAdapter
import com.webkul.mobikul.odoo.constant.BundleConstant
import com.webkul.mobikul.odoo.database.SqlLiteDbHelper
//import com.webkul.mobikul.odoo.databinding.ActivityNewDrawerBinding
import com.webkul.mobikul.odoo.handler.home.HomeActivityHandler
import com.webkul.mobikul.odoo.helper.AppSharedPref
import com.webkul.mobikul.odoo.model.home.HomePageResponse

class NewDrawerActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_drawer)

        val homePageResponse =
            intent.getParcelableExtra<HomePageResponse>(BundleConstant.BUNDLE_KEY_HOME_PAGE_RESPONSE)

        val navController =
            Navigation.findNavController(this@NewDrawerActivity, R.id.drawer_fragment)
        val bundle = Bundle()
        bundle.putParcelable("home_response_data", homePageResponse)
        navController.setGraph(R.navigation.drawer_layout_navigation, bundle)
    }

    override fun onPause() {
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
        super.onPause()
    }

    override fun getScreenTitle(): String = TAG

}



/*
---------------------------------Previous Code----------------------------------------

DESCRIPTION

The below code was written when Navigation Component was not set up in Drawer Layout
for the Profile section


    private lateinit var binding: ActivityNewDrawerBinding
    private val TAG = "NewDrawerActivity"


    private fun language_settings() {
        val lang_pref= AppSharedPref.getLanguageCode(this@NewDrawerActivity)

        binding.englishOption.setOnClickListener{
            if(lang_pref!="en_US"){
                HomeActivityHandler(this@NewDrawerActivity).onClickLanguageIcon("en_US")
            }
        }

        binding.indOption.setOnClickListener{
            if(lang_pref!="id_ID"){
                HomeActivityHandler(this@NewDrawerActivity).onClickLanguageIcon("id_ID")

            }
        }


        Log.d("NewDrawerActivity" , lang_pref)
        if(lang_pref.equals("en_US")){
            binding.englishOption.setBackgroundColor(resources.getColor(R.color.background_orange))
            binding.englishOption.setTextColor(resources.getColor(R.color.white))
            binding.indOption.setBackgroundColor(resources.getColor(R.color.gray))
            binding.indOption.setTextColor(resources.getColor(R.color.black))
        }
        else{
            binding.englishOption.setBackgroundColor(resources.getColor(R.color.gray))
            binding.englishOption.setTextColor(resources.getColor(R.color.black))
            binding.indOption.setBackgroundColor(resources.getColor(R.color.background_orange))
            binding.indOption.setTextColor(resources.getColor(R.color.white))
        }
    }


        binding =DataBindingUtil.setContentView(this@NewDrawerActivity, R.layout.activity_new_drawer)
        binding.handler = HomeActivityHandler(this@NewDrawerActivity)
        AppSharedPref.setLaunchcount(this, AppSharedPref.getLaunchCount(this) + 1)

        setUpDrawer()

        //Manage language settings
        language_settings()

        binding.drawerCloseBtn.setOnClickListener{
            overridePendingTransition(R.anim.enter_from_left,R.anim.exit_to_left)
            super.onBackPressed()
        }


            private fun setUpDrawer() {
        val homePageResponse =
            intent.getParcelableExtra<HomePageResponse>(BundleConstant.BUNDLE_KEY_HOME_PAGE_RESPONSE)
        if (homePageResponse != null) {
            binding.categoryRv.adapter =
                NavDrawerCategoryStartRvAdapter(
                    this@NewDrawerActivity,
                    homePageResponse.categories[0].children,
                    ""
                )
        }
        if (homePageResponse?.languageMap!!.size > 0) {
            binding.languageData =
                homePageResponse.languageMap
        } else {
            val sqlLiteDbHelper = SqlLiteDbHelper(this@NewDrawerActivity)
            if (sqlLiteDbHelper.homeScreenData != null) {
                if (sqlLiteDbHelper.homeScreenData.languageMap != null) {
                    binding.languageData =
                        sqlLiteDbHelper.homeScreenData.languageMap
                }
            }
        }
    }

 */