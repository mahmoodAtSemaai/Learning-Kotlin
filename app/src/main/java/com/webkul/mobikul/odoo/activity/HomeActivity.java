package com.webkul.mobikul.odoo.activity;

import static com.webkul.mobikul.odoo.BuildConfig.DEFAULT_BACK_PRESSED_TIME_TO_CLOSE;
import static com.webkul.mobikul.odoo.BuildConfig.DEFAULT_DAYS_UNTIL_PROMPT;
import static com.webkul.mobikul.odoo.BuildConfig.DEFAULT_LAUNCHES_UNTIL_PROMPT;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CALLING_ACTIVITY;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CUSTOMER_FRAG_TYPE;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.analytics.AnalyticsImpl;
import com.webkul.mobikul.odoo.constant.BundleConstant;
import com.webkul.mobikul.odoo.custom.MaterialSearchView;
import com.webkul.mobikul.odoo.databinding.ActivityHomeBinding;
import com.webkul.mobikul.odoo.dialog_frag.RateAppDialogFragm;
import com.webkul.mobikul.odoo.fragment.AccountFragment;
import com.webkul.mobikul.odoo.fragment.EmptyFragment;
import com.webkul.mobikul.odoo.fragment.HomeFragment;
import com.webkul.mobikul.odoo.fragment.NotificationFragment;
import com.webkul.mobikul.odoo.handler.home.FragmentNotifier;
import com.webkul.mobikul.odoo.handler.home.HomeActivityHandler;
import com.webkul.mobikul.odoo.helper.AppSharedPref;
import com.webkul.mobikul.odoo.helper.CustomerHelper;
import com.webkul.mobikul.odoo.helper.FragmentHelper;
import com.webkul.mobikul.odoo.helper.IntentHelper;
import com.webkul.mobikul.odoo.helper.SnackbarHelper;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;




public class HomeActivity extends BaseActivity implements OnTabSelectListener,/* OnTabReselectListener,*/FragmentManager.OnBackStackChangedListener, OnTabReselectListener {

    public static final int RC_ACCESS_FINE_LOCATION_NEW_ADDRESS = 1001; // RC for asking access permission for location
    public static final int RC_CHECK_LOCATION_SETTINGS = 1003;
    public static final int RC_PICK_IMAGE = 1004;
    public static final int RC_CAMERA = 1005;
    public static final int RC_SIGN_IN_SIGN_UP = 1006;
    @SuppressWarnings("unused")
    private static final String TAG = "HomeActivity";
    public ActivityHomeBinding mBinding;
    public SwipeRefreshLayout swipeRefreshLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private long mBackPressedTime;
    private String currentFragmentDisplayed = "";

    @Override
    public String getScreenTitle() {
        return TAG;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        setSupportActionBar(mBinding.toolbar);
        mDrawerToggle = setupDrawerToggle(mBinding.toolbar);
        mBinding.drawerLayout.addDrawerListener(mDrawerToggle);
        mBinding.bottomNavigation.setOnTabSelectListener(this);
        mBinding.bottomNavigation.setOnTabReselectListener(this);


        mSupportFragmentManager.addOnBackStackChangedListener(this);
        mBinding.setHandler(new HomeActivityHandler(this));
        AppSharedPref.setLaunchcount(this, AppSharedPref.getLaunchCount(this) + 1);
        mBinding.drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View view, float v) {

            }

            @Override
            public void onDrawerOpened(@NonNull View view) {
                AnalyticsImpl.INSTANCE.trackDrawerHamburgerSelected(currentFragmentDisplayed);
            }

            @Override
            public void onDrawerClosed(@NonNull View view) {

            }

            @Override
            public void onDrawerStateChanged(int i) {

            }
        });

        long firstLaunchDate = AppSharedPref.getFirstLaunchDate(this);
        if (firstLaunchDate == 0) {
            firstLaunchDate = System.currentTimeMillis();
            AppSharedPref.setFirstLaunchDate(this, firstLaunchDate);
        }
        if (AppSharedPref.getLaunchCount(this) >= DEFAULT_LAUNCHES_UNTIL_PROMPT) {
            if (System.currentTimeMillis() >= AppSharedPref.getFirstLaunchDate(this) + (DEFAULT_DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)) {
                RateAppDialogFragm.newInstance().show(mSupportFragmentManager, RateAppDialogFragm.class.getSimpleName());
            }
        }


    }

    public ActionBarDrawerToggle setupDrawerToggle(Toolbar toolbar) {
        return new ActionBarDrawerToggle(this, mBinding.drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_item_search) {
            AnalyticsImpl.INSTANCE.trackSearchSelected(currentFragmentDisplayed);
            mBinding.bottomNavigation.selectTabWithId(R.id.navigation_search);
        } else if (item.getItemId() == R.id.menu_item_bag) {
            AnalyticsImpl.INSTANCE.trackShoppingCartSelected(currentFragmentDisplayed);
            IntentHelper.goToBag(this);
        } else if (item.getItemId() == R.id.menu_item_wishlist) {
            AnalyticsImpl.INSTANCE.trackWishlistSelected(currentFragmentDisplayed);
            Intent intent = new Intent(this, CustomerBaseActivity.class);
            intent.putExtra(BUNDLE_KEY_CUSTOMER_FRAG_TYPE, CustomerHelper.CustomerFragType.TYPE_WISHLIST);
            startActivity(intent);

        } else if(item.getItemId() == R.id.menu_item_chat){
            showChatHistory();
        }
        return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(@IdRes int tabId) {
        /*CLOSE SEARCH IF OPEN AS BOTTOM BAR IS VISIBLE FOR BOTTOM NAVIGATION*/
        updateAccount();
        mBinding.searchView.closeSearch();
        if (tabId == R.id.navigation_home) {
            Log.i(TAG, "onTabSelected: ");
            showHomeFrag();
        } else if (tabId == R.id.navigation_notification) {
            showNotificationFrag();

        } else if (tabId == R.id.navigation_search) {
            mBinding.searchView.openSearch();

        } else if (tabId == R.id.navigation_account) {
            if (AppSharedPref.isLoggedIn(this)) {
                showAccountFrag();
            } else {
                Intent i = new Intent(this, SignInSignUpActivity.class);
                i.putExtra(BUNDLE_KEY_CALLING_ACTIVITY, HomeActivity.class.getSimpleName());
                startActivityForResult(i, RC_SIGN_IN_SIGN_UP);
            }
        }
    }


    public void updateAccount() {
        Log.i(TAG, "updateAccount: ");
        boolean isEmailVerified = AppSharedPref.isEmailVerified(this);
        boolean isLoggedIn = AppSharedPref.isLoggedIn(this);
        mBinding.bottomNavigation.getTabWithId(R.id.navigation_account).setActivated(isLoggedIn && !isEmailVerified);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    public void showHomeFrag() {
        if (!mSupportFragmentManager.popBackStackImmediate(HomeFragment.class.getSimpleName(), 0)) { // fragment not in back stack, create it.
        }
    }



    private void showNotificationFrag() {
        if (!mSupportFragmentManager.popBackStackImmediate(NotificationFragment.class.getSimpleName(), 0)) { //fragment not in back stack, create it.
            FragmentHelper.replaceFragment(R.id.container, this, NotificationFragment.newInstance(), NotificationFragment.class.getSimpleName(), true, true);
        }
    }

    private void showAccountFrag() {
        if (!mSupportFragmentManager.popBackStackImmediate(AccountFragment.class.getSimpleName(), 0)) { //fragment not in back stack, create it.
            FragmentHelper.replaceFragment(R.id.container, this, AccountFragment.newInstance(), AccountFragment.class.getSimpleName(), true, true);
        }
    }


    @SuppressWarnings("RestrictedApi")
    @Override
    public void onBackPressed() {
        if (mBinding.drawerLayout != null && mBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            mBinding.drawerLayout.closeDrawers();
            return;
        }

        /*Prevent last fragment i.e. HomeFragment from popping.*/
        if (mSupportFragmentManager.getBackStackEntryCount() == 0) {
            return;
        }


        if (mBinding.searchView.isOpen()) {
            mBinding.searchView.closeSearch();
            mBinding.bottomNavigation.selectTabWithId(R.id.navigation_home);
            return;
        }

        String tag = mSupportFragmentManager.getBackStackEntryAt(mSupportFragmentManager.getBackStackEntryCount() - 1).getName();
        Fragment fragment = mSupportFragmentManager.findFragmentByTag(tag);
        if (fragment != null) {
            if (fragment.isAdded() && fragment instanceof AccountFragment || fragment instanceof NotificationFragment || fragment instanceof EmptyFragment) {
                mBinding.bottomNavigation.selectTabWithId(R.id.navigation_home);
                return;
            }
        }

        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - mBackPressedTime > DEFAULT_BACK_PRESSED_TIME_TO_CLOSE) {
            mBackPressedTime = currentTimeMillis;
            Toast.makeText(this, getResources().getString(R.string.press_back_to_exit), Toast.LENGTH_SHORT).show();
        } else {
            finish();
        }
    }

    @Override
    public void onBackStackChanged() {
        Log.i(TAG, "onBackStackChanged: ");
        if (mSupportFragmentManager.getBackStackEntryCount() == 0) {
            return;
        }

        String tag = mSupportFragmentManager.getBackStackEntryAt(mSupportFragmentManager.getBackStackEntryCount() - 1).getName();
        Fragment fragment = mSupportFragmentManager.findFragmentByTag(tag);
        Log.i(TAG, "onBackStackChanged: " + mSupportFragmentManager.getFragments().size());
        Log.i(TAG, "onBackStackChanged: " + mSupportFragmentManager.getFragments());
//        Log.i(TAG, "onBackStackChanged: "+ mSupportFragmentManager.getBackStackEntryCount());
        if (fragment == null) {
            return;
        }
        if (fragment instanceof HomeFragment) {
            setTitle(getString(R.string.store_name));
        } else if (fragment instanceof NotificationFragment) {
            setTitle(getString(R.string.notification));
        } else if (fragment instanceof AccountFragment) {
            setTitle(getString(R.string.account));
        }
    }

    @Override
    public void onTabReSelected(@IdRes int tabId) {
        /*commenting onTabReSelected as there is some problem on reselecting search view , the view don't behave properly in real device.. */
//        onTabSelected(tabId);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case RC_CAMERA:
                if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Fragment fragment = mSupportFragmentManager.findFragmentByTag(AccountFragment.class.getSimpleName());
                    if (fragment != null && fragment.isAdded()) {
                        ((AccountFragment) fragment).binding.getHandler().startPickImage();
                    }
                } else {
                    SnackbarHelper.getSnackbar(this, getString(R.string.error_permision_required_to_change_profile_image), Snackbar.LENGTH_SHORT, SnackbarHelper.SnackbarType.TYPE_WARNING).show();
                }
                break;
            case RC_PICK_IMAGE:
                if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Fragment fragment = mSupportFragmentManager.findFragmentByTag(AccountFragment.class.getSimpleName());
                    if (fragment != null && fragment.isAdded()) {
                        ((AccountFragment) fragment).binding.getHandler().pickImageFromFileSystemIntent();
                    }
                } else {
                    SnackbarHelper.getSnackbar(this, getString(R.string.error_permision_required_to_change_profile_image), Snackbar.LENGTH_SHORT, SnackbarHelper.SnackbarType.TYPE_WARNING).show();
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RC_PICK_IMAGE:
                switch (resultCode) {
                    case RESULT_OK:
                        if (data != null) {
                            Fragment fragment = mSupportFragmentManager.findFragmentByTag(AccountFragment.class.getSimpleName());
                            if (fragment != null && fragment.isAdded()) {
                                try {
                                    Uri imageData;
                                    if (data.getExtras() != null && data.getExtras().get("data") instanceof Bitmap) {
                                        imageData = getImageUriFromBitmap((Bitmap) data.getExtras().get("data"));
                                    } else {
                                        imageData = data.getData();
                                    }
                                    CropImage.activity(imageData)
                                            .setGuidelines(CropImageView.Guidelines.ON)
                                            .setAspectRatio(1, 1)
                                            .setInitialCropWindowPaddingRatio(0f)
                                            .start(this);


//
//                                    Uri imageUri = data.getData();
//                                    InputStream imageStream = getContentResolver().openInputStream(imageUri);
//                                    Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
//
//                                    selectedImage = ImageHelper.resizeBitmap(selectedImage, 1200);
//
//                                    ((AccountFragment) fragment).uploadFile(ImageHelper.getImageUri(getApplicationContext(), selectedImage));

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Log.i(TAG, "onActivityResult: catch " + data.getData());
                                    ((AccountFragment) fragment).uploadFile(data.getData(), false);
                                }
                            }
                        } else {
                            SnackbarHelper.getSnackbar(this, getString(R.string.error_in_changing_profile_image), Snackbar.LENGTH_SHORT, SnackbarHelper.SnackbarType.TYPE_WARNING).show();
                        }
                        break;
                }
                break;
            case RC_CAMERA:
                switch (resultCode) {
                    case RESULT_OK:
                        Fragment fragment = mSupportFragmentManager.findFragmentByTag(AccountFragment.class.getSimpleName());
                        if (fragment != null && fragment.isAdded()) {
                            Uri imageData;
                            if (data.getExtras() != null && data.getExtras().get("data") instanceof Bitmap) {
                                imageData = getImageUriFromBitmap((Bitmap) data.getExtras().get("data"));
                            } else {
                                imageData = data.getData();
                            }

                            CropImage.activity(imageData)
                                    .setGuidelines(CropImageView.Guidelines.ON)
                                    .setAspectRatio(1, 1)
                                    .setInitialCropWindowPaddingRatio(0f)
                                    .start(this);
//                            Bitmap tempBitmap = Bitmap.createScaledBitmap((Bitmap) data.getExtras().get("data"),1200,1200,false);
//                            tempBitmap = ImageHelper.resizeBitmap(tempBitmap,1200);
//
//                            ((AccountFragment) fragment).uploadFile(ImageHelper.getImageUri(getApplicationContext(), tempBitmap), false);
//                            ((AccountFragment) fragment).uploadFile(ImageHelper.getImageUri(getApplicationContext(), (Bitmap) data.getExtras().get("data")));
                        }
                        break;
                }
                break;
            case CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE:
                CropImage.activity(CropImage.getPickImageResultUri(this, data))
                        .setGuidelines(CropImageView.Guidelines.ON)
                        //.setAspectRatio(1, 1)
                        .setInitialCropWindowPaddingRatio(0f)
                        .start(this);

                break;
            case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Fragment fragment = mSupportFragmentManager.findFragmentByTag(AccountFragment.class.getSimpleName());
                    if (fragment != null && fragment.isAdded()) {
                        Uri imageData;
//                        if (data.getExtras() != null && data.getExtras().get("data") instanceof Bitmap) {
//                            imageData = getImageUriFromBitmap((Bitmap) data.getExtras().get("data"));
//                        } else {
//                            imageData = CropImage.getActivityResult(data).getUri();
//                        }
                        ((AccountFragment) fragment).uploadFile(CropImage.getActivityResult(data).getUri(), true);
//                            ((AccountFragment) fragment).uploadFile(ImageHelper.getImageUri(getApplicationContext(), (Bitmap) data.getExtras().get("data")));
                    }
                }
                break;

            case RC_SIGN_IN_SIGN_UP:
                switch (resultCode) {
                    case RESULT_CANCELED:
                        mBinding.bottomNavigation.selectTabWithId(R.id.navigation_home);
                        break;
                }
            case MaterialSearchView.RC_CAMERA_SEARCH:
                switch (resultCode) {
                    case RESULT_OK:
                        mBinding.searchView.setQuery(data.getStringExtra(BundleConstant.CAMERA_SEARCH_HELPER), true);
                        break;
                }
                break;

            case MaterialSearchView.RC_MATERIAL_SEARCH_VOICE:
                switch (resultCode) {
                    case RESULT_OK:
                        ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        if (matches != null && matches.size() > 0) {
                            String searchWrd = matches.get(0);
                            if (!TextUtils.isEmpty(searchWrd)) {
                                mBinding.searchView.setQuery(searchWrd, false);
                            }
                        }
                        break;
                }
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        mBinding.searchView.closeSearch();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateAccount();
        if (AppSharedPref.isLoggedIn(this)) {
            mBinding.bottomNavigation.getTabWithId(R.id.navigation_account).setTitle(getString(R.string.account));
        } else {
            mBinding.bottomNavigation.getTabWithId(R.id.navigation_account).setTitle(getString(R.string.login));
        }
        if (!mBinding.searchView.isOpen() && mBinding.bottomNavigation.getTabWithId(R.id.navigation_search).isSelected()) {
            mBinding.bottomNavigation.selectTabWithId(R.id.navigation_home);
        }
    }

    private Uri getImageUriFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFragmentNotifier(FragmentNotifier.HomeActivityFragments currentFragment) {
        switch (currentFragment) {
            case HOME_FRAGMENT:
                currentFragmentDisplayed = getString(R.string.home);
                break;
            case NOTIFICATION_FRAGMENT:
                currentFragmentDisplayed = getString(R.string.notification);
                break;
            case ACCOUNT_FRAGMENT:
                currentFragmentDisplayed = getString(R.string.account);
        }
    }


    public void showChatHistory() {
        Intent intent = new Intent(this, ChatHistoryActivity.class);
        this.startActivity(intent);

    }

}
