package com.plcoding.stockmarketapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CompanyInfoDto(
    @SerializedName("Symbol")
    val symbol: String?,
    @SerializedName("Name")
    val name: String?,
    @SerializedName("Description")
    val description: String?,
    @SerializedName("Country")
    val country: String?,
    @SerializedName("Industry")
    val industry: String?,
)
