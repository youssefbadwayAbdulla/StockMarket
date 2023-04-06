package com.plcoding.stockmarketapp.presentation.company_info

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.stockmarketapp.domain.model.CompanyInfo
import com.plcoding.stockmarketapp.domain.model.IntraDayInfo
import com.plcoding.stockmarketapp.domain.repositer_interfaces.StockRepository
import com.plcoding.stockmarketapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompanyInfoViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: StockRepository
) : ViewModel() {
     var state by mutableStateOf(CompanyInfoState())

    init {
        handelCompanyInfo()
    }

    private fun handelCompanyInfo() {
        viewModelScope.launch {
            val symbol = savedStateHandle.get<String>("symbol") ?: return@launch
            state = state.copy(isLoading = true)
            val companyInfoResult = async { repository.getCompanyInfo(symbol) }
            val intraDayInfo = async { repository.getIntraDayInfo(symbol) }
            handelCompanyInfoResult(companyInfoResult)
            handelIntraDayInfo(intraDayInfo)
        }
    }

    private suspend fun handelIntraDayInfo(intraDayInfo: Deferred<Resource<List<IntraDayInfo>>>) {
        when (val result = intraDayInfo.await()) {
            is Resource.Success -> {
                state = state.copy(
                    stockIntraDay = result.data.orEmpty(),
                    isLoading = false,
                    error = null
                )
            }
            is Resource.Error -> {
                state = state.copy(
                    error = result.message,
                    isLoading = false,
                    company = null
                )
            }
            else -> Unit
        }
    }

    private suspend fun handelCompanyInfoResult(companyInfoResult: Deferred<Resource<CompanyInfo>>) {
        when (val result = companyInfoResult.await()) {
            is Resource.Success -> {
                state = state.copy(
                    company = result.data,
                    isLoading = false,
                    error = null
                )
            }
            is Resource.Error -> {
                state = state.copy(
                    error = result.message,
                    isLoading = false,
                    company = null
                )
            }
            else -> Unit
        }
    }
}