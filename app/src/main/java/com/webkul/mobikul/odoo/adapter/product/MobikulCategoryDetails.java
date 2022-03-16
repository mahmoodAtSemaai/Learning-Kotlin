package com.webkul.mobikul.odoo.adapter.product;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MobikulCategoryDetails {

    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("crops")
    @Expose
    private ArrayList<String> crops = new ArrayList<>();
    @SerializedName("organic")
    @Expose
    private boolean organic;
    @SerializedName("weight")
    @Expose
    private double weight;
    @SerializedName("additional_information")
    @Expose
    private String additionalInformation;
    @SerializedName("mrp")
    @Expose
    private double mrp;
    @SerializedName("planting_method")
    @Expose
    private String plantingMethod;
    @SerializedName("plant_spacing")
    @Expose
    private String plantSpacing;
    @SerializedName("active_ingredients")
    @Expose
    private String activeIngredients;
    @SerializedName("dosage")
    @Expose
    private String dosage;
    @SerializedName("application_method")
    @Expose
    private String applicationMethod;
    @SerializedName("frequency_of_applications")
    @Expose
    private String frequencyOfApplication;
    @SerializedName("pests_and_diseases")
    @Expose
    private String pestsAndDiseases;
    @SerializedName("duration_of_effect")
    @Expose
    private String durationOfEffect;

    public MobikulCategoryDetails() {
    }

    public MobikulCategoryDetails(String category, ArrayList<String> crops, boolean organic, double weight, String additionalInformation, double mrp, String plantingMethod, String plantSpacing, String activeIngredients, String dosage, String applicationMethod, String frequencyOfApplication, String pestsAndDiseases, String durationOfEffect) {
        this.category = category;
        this.crops = crops;
        this.organic = organic;
        this.weight = weight;
        this.additionalInformation = additionalInformation;
        this.mrp = mrp;
        this.plantingMethod = plantingMethod;
        this.plantSpacing = plantSpacing;
        this.activeIngredients = activeIngredients;
        this.dosage = dosage;
        this.applicationMethod = applicationMethod;
        this.frequencyOfApplication = frequencyOfApplication;
        this.pestsAndDiseases = pestsAndDiseases;
        this.durationOfEffect = durationOfEffect;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public ArrayList<String> getCrops() {
        return crops;
    }

    public void setCrops(ArrayList<String> crops) {
        this.crops = crops;
    }

    public boolean isOrganic() {
        return organic;
    }

    public void setOrganic(boolean organic) {
        this.organic = organic;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    public double getMrp() {
        return mrp;
    }

    public void setMrp(double mrp) {
        this.mrp = mrp;
    }

    public String getPlantingMethod() {
        return plantingMethod;
    }

    public void setPlantingMethod(String plantingMethod) {
        this.plantingMethod = plantingMethod;
    }

    public String getPlantSpacing() {
        return plantSpacing;
    }

    public void setPlantSpacing(String plantSpacing) {
        this.plantSpacing = plantSpacing;
    }

    public String getActiveIngredients() {
        return activeIngredients;
    }

    public void setActiveIngredients(String activeIngredients) {
        this.activeIngredients = activeIngredients;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getApplicationMethod() {
        return applicationMethod;
    }

    public void setApplicationMethod(String applicationMethod) {
        this.applicationMethod = applicationMethod;
    }

    public String getFrequencyOfApplication() {
        return frequencyOfApplication;
    }

    public void setFrequencyOfApplication(String frequencyOfApplication) {
        this.frequencyOfApplication = frequencyOfApplication;
    }

    public String getPestsAndDiseases() {
        return pestsAndDiseases;
    }

    public void setPestsAndDiseases(String pestsAndDiseases) {
        this.pestsAndDiseases = pestsAndDiseases;
    }

    public String getDurationOfEffect() {
        return durationOfEffect;
    }

    public void setDurationOfEffect(String durationOfEffect) {
        this.durationOfEffect = durationOfEffect;
    }
}
