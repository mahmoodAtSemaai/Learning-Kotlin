package com.webkul.mobikul.odoo.helper;

import android.content.Context;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import com.webkul.mobikul.odoo.R;


/**
 * Created by shubham.agarwal on 7/4/17.
 */

public class FragmentHelper {
    @SuppressWarnings("unused")
    private static final String TAG = "FragmentHelper";


    @SuppressWarnings("RestrictedApi")
    public static void replaceFragment(@IdRes int containerViewId, @Nullable Context context, @NonNull Fragment fragment, @NonNull String tag, boolean addToBackStack, boolean setCustomAnimations) {
        if (context == null) {
            return;
        }
        Log.i(TAG, "replaceFragment: ");
        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(containerViewId, fragment, tag);
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(tag);
        }
        if (setCustomAnimations) {
            fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        }
        fragmentTransaction.commit();
        printFragmentEntryName(fragmentManager);
    }

    @SuppressWarnings("RestrictedApi")
    public static void popFromBackStack(@Nullable Context context) {
        if (context == null) {
            return;
        }
        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
        fragmentManager.popBackStack();

//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(containerViewId, fragment, tag);
//        if (addToBackStack) {
//            fragmentTransaction.addToBackStack(tag);
//        }
//        if (setCustomAnimations) {
//            fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
//        }
//        fragmentTransaction.commit();
        printFragmentEntryName(fragmentManager);
    }


    @SuppressWarnings("RestrictedApi")
    public static void addFragment(@IdRes int containerViewId, @Nullable Context context, @NonNull Fragment fragment, @NonNull String tag, boolean addToBackStack, boolean setCustomAnimations) {
        if (context == null) {
            return;
        }
        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(containerViewId, fragment, tag);
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(tag);
        }
        if (setCustomAnimations) {
            fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        }
        fragmentTransaction.commit();
        printFragmentEntryName(fragmentManager);
    }

    @SuppressWarnings({"RestrictedApi", "unused", "WeakerAccess"})
    public static void printFragmentEntryName(FragmentManager fragmentManager) {
        try {
            if (fragmentManager.getFragments() != null) {
                Log.i(TAG, "printFragmentEntryName: " + fragmentManager.getFragments().size());
                for (int i = 0; i < fragmentManager.getFragments().size(); i++) {
                    Log.i(TAG, String.format("printFragmentEntryName getBackStackEntryAt %d : %s", i, fragmentManager.getBackStackEntryAt(i).getName()));
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "printFragmentEntryName: " + e.getMessage());
        }
    }
}
