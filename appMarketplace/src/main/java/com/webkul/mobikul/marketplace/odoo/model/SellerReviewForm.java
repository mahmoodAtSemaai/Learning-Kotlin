package com.webkul.mobikul.marketplace.odoo.model;

import android.content.Context;

import androidx.core.content.ContextCompat;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import android.graphics.Color;
import androidx.fragment.app.Fragment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.webkul.mobikul.marketplace.odoo.R;
import com.webkul.mobikul.marketplace.odoo.activity.SellerProfileActivity;
import com.webkul.mobikul.marketplace.odoo.fragment.AddSellerReviewFragment;
import com.webkul.mobikul.odoo.BR;
import com.webkul.mobikul.odoo.connection.ApplicationSingleton;

/**
 * Created by aastha.gupta on 5/3/18 in Mobikul_Odoo_Application.
 */

public class SellerReviewForm extends BaseObservable {

    private final Context mContext;
    private boolean displayError;
    @SerializedName("customer")
    @Expose
    private String customer;
    @SerializedName("customer_image")
    @Expose
    private String customerImage;
    @SerializedName("rating")
    @Expose
    private int rating;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("create_date")
    @Expose
    private String createDate;
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("dislikes")
    @Expose
    private String dislikes;
    @SerializedName("likes")
    @Expose
    private String likes;

    private String ratingText;
    private int ratingTextColor;
    private int colors[];

    public SellerReviewForm(Context context) {
        mContext = context;
        colors= new int[]{mContext.getResources().getColor(R.color.text_color_primary), Color.RED, Color.parseColor("#8a6d3b"), Color.BLUE, Color.BLUE, Color.GREEN};
    }

    public String getCustomerImage() {
        return customerImage;
    }

    public void setCustomerImage(String customerImage) {
        this.customerImage = customerImage;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
        notifyPropertyChanged(BR.ratingText);
        notifyPropertyChanged(BR.ratingTextColor);
    }


    public String getCreateDate() {
        return createDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDislikes() {
        return dislikes;
    }

    public void setDislikes(String dislikes) {
        this.dislikes = dislikes;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    @Bindable
    public String getEmail() {
        if (email == null) {
            return "";
        }
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(BR.email);
    }

//    @Bindable({"displayError", "email"})
//    public String getEmailError() {
//        if (!isDisplayError()) {
//            return "";
//        }
//        if (getEmail().isEmpty()) {
//            return String.format("%s %s", mContext.getString(R.string.email), mContext.getResources().getString(R.string.error_is_required));
//        }
//
//        if (!Patterns.EMAIL_ADDRESS.matcher(getEmail()).matches()) {
//            return mContext.getResources().getString(R.string.error_enter_valid_email);
//        }
//        return "";
//    }

    /*name*/
    @Bindable
    public String getCustomer() {
        if (customer == null) {
            return "";
        }
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
        notifyPropertyChanged(BR.customer);
    }


//    @Bindable({"displayError", "customer"})
//    public String getCustomerError() {
//        if (!isDisplayError()) {
//            return "";
//        }
//
//        if (getCustomer().isEmpty()) {
//            return mContext.getString(R.string.your_name) + " " + mContext.getResources().getString(R.string.error_is_required);
//        }
//
//        return "";
//    }


//    public void setDislikes(int dislikes) {
//        this.dislikes = dislikes;
//    }
//
//    public void setLikes(int likes) {
//        this.likes = likes;
//    }

    /*title*/
    @Bindable
    public String getTitle() {
        if (title == null) {
            return "";
        }
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);
    }

    @Bindable({"displayError", "title"})
    public String getTitleError() {
        if (!isDisplayError()) {
            return "";
        }

        if (getTitle().isEmpty()) {
            return String.format("%s %s", mContext.getString(R.string.write_review_title), mContext.getResources().getString(R.string.error_is_required));
        }

        return "";
    }


    /*msg*/

    @Bindable
    public String getMsg() {
        if (msg == null) {
            return "";
        }
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
        notifyPropertyChanged(BR.msg);
    }


    @Bindable({"displayError", "msg"})
    public String getMsgError() {
        if (!isDisplayError()) {
            return "";
        }

        if (getMsg().isEmpty()) {
            return String.format("%s %s", mContext.getString(R.string.write_your_review), mContext.getResources().getString(R.string.error_is_required));
        }
        return "";
    }

    @Bindable
    public boolean isDisplayError() {
        return displayError;
    }

    public void setDisplayError(boolean displayError) {
        this.displayError = displayError;
        notifyPropertyChanged(BR.displayError);
    }

    public boolean isFormValidated() {
        setDisplayError(true);
        Fragment fragment = ((SellerProfileActivity) mContext).getSupportFragmentManager().findFragmentByTag(AddSellerReviewFragment.class.getSimpleName());
        if (fragment != null && fragment.isAdded()) {
            AddSellerReviewFragment addProductReviewFragment = (AddSellerReviewFragment) fragment;
//            if (!getEmailError().isEmpty()) {
//                addProductReviewFragment.mBinding.emailEt.requestFocus();
//                return false;
//            }
//            if (!getCustomerError().isEmpty()) {
//                addProductReviewFragment.mBinding.nameEt.requestFocus();
//                return false;
//            }
            if (!getMsgError().isEmpty()) {
                addProductReviewFragment.mBinding.descriptionEt.requestFocus();
                return false;
            }

            if (!getTitleError().isEmpty()) {
                addProductReviewFragment.mBinding.titleEt.requestFocus();
                return false;
            }
//            if(getRating() == 0) {
//                CustomToast.makeText(mContext, mContext.getString(R.string.please_add_your_rating), Toast.LENGTH_SHORT, R.style.GenericStyleableToast).show();
//                return false;
//            }
            setDisplayError(false);
            return true;
        }

        return false;
    }

    @Bindable
    public String getRatingText() {
        if (rating != 0) {
            ratingText = ApplicationSingleton.getInstance().getRatingStatus().get(rating - 1).get(1);
        } else {
            ratingText = "not rated";
        }
        return ratingText;
    }

    public void setRatingText(String ratingText) {
        if (rating != 0) {
            this.ratingText = ApplicationSingleton.getInstance().getRatingStatus().get(rating - 1).get(1);
        } else {
            this.ratingText = "not rated";
        }
    }

    @Bindable
    public int getRatingTextColor() {
        ratingTextColor = colors[rating];
        return ratingTextColor;
    }

    public void setRatingTextColor(int ratingTextColor) {
        this.ratingTextColor = colors[rating];
    }

}
