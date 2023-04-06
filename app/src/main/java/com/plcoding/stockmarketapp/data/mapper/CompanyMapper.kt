@file:Suppress("UNREACHABLE_CODE")

package com.plcoding.stockmarketapp.data.mapper

import com.plcoding.stockmarketapp.data.local.entity.CompanyListingEntity
import com.plcoding.stockmarketapp.data.remote.dto.CompanyInfoDto
import com.plcoding.stockmarketapp.domain.model.CompanyInfo
import com.plcoding.stockmarketapp.domain.model.CompanyListing

fun CompanyListingEntity.toCompanyListing(): CompanyListing {
    return CompanyListing(
        name = name.orEmpty(),
        symbol = symbol.orEmpty(),
        exchange = exchange.orEmpty()
    )
}


fun CompanyListing.toCompanyListingEntity(): CompanyListingEntity {
    return CompanyListingEntity(
        name = name.orEmpty(),
        symbol = symbol.orEmpty(),
        exchange = exchange.orEmpty()
    )
}

fun CompanyInfoDto.toCompanyInfo(): CompanyInfo {
    return CompanyInfo(
        symbol = symbol.orEmpty(),
        name = name.orEmpty(),
        description = description.orEmpty(),
        country = country.orEmpty(),
        industry = industry.orEmpty()
    )
}