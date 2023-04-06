package com.plcoding.stockmarketapp.di

import com.plcoding.stockmarketapp.data.cvs.CSVParser
import com.plcoding.stockmarketapp.data.cvs.CompanyListingParser
import com.plcoding.stockmarketapp.data.cvs.IntraDayInfoParser
import com.plcoding.stockmarketapp.data.repositeries.StockRepositoryIMP
import com.plcoding.stockmarketapp.domain.model.CompanyListing
import com.plcoding.stockmarketapp.domain.model.IntraDayInfo
import com.plcoding.stockmarketapp.domain.repositer_interfaces.StockRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindCompanyListingsParser(
        companyListingParser: CompanyListingParser
    ): CSVParser<CompanyListing>

    @Binds
    @Singleton
    abstract fun bindStockRepository(
        stockRepositoryIMP: StockRepositoryIMP
    ): StockRepository

    @Binds
    @Singleton
    abstract fun bindIntraDayInfoParser(
        intraDayInfoParser: IntraDayInfoParser
    ): CSVParser<IntraDayInfo>

}