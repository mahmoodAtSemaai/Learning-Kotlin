package com.webkul.mobikul.odoo.data.entity

import com.webkul.mobikul.odoo.model.generic.CountryData

data class CountryEntity(
        val countries: List<CountryData>
) {

    fun getCountryNameList(): List<String> {
        val countryNames: MutableList<String> = ArrayList()
        for (eachCountryData in countries) {
            countryNames.add(eachCountryData.name)
        }
        return countryNames
    }
}
