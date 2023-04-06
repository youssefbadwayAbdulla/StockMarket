package com.plcoding.stockmarketapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.plcoding.stockmarketapp.data.local.entity.CompanyListingEntity

@Dao
interface StockDao {
    @Insert
    suspend fun insertCompanyListing(
        companyListingEntity: List<CompanyListingEntity>
    )

    @Query("DELETE FROM company_listing_entity")
    suspend fun clearCompanyListing()

    @Query(
        """
    SELECT *
    FROM company_listing_entity
    WHERE LOWER(name) LIKE '%' ||LOWER(:query) ||'%' OR 
    UPPER(:query)==symbol
"""
    )
    suspend fun searchCompanyListing(query: String): List<CompanyListingEntity>

}