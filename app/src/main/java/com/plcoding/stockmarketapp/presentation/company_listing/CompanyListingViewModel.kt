package com.plcoding.stockmarketapp.presentation.company_listing

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.stockmarketapp.domain.repositer_interfaces.StockRepository
import com.plcoding.stockmarketapp.presentation.company_listing.CompanyListingEvent.*
import com.plcoding.stockmarketapp.util.Resource.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompanyListingViewModel @Inject constructor(
    private val repository: StockRepository
) : ViewModel() {
     var state by mutableStateOf(CompanyListingState())
    private var searchJob: Job? = null
    init {
        getCompanyListing()
    }
     fun onEvent(event: CompanyListingEvent) {
        when (event) {
            is Refresh -> {
                getCompanyListing(fetchFromRemote = true)
            }
            is OnSearchQueryChange -> {
                handelSearchQueryEvent(event)
            }
        }
    }

    private fun handelSearchQueryEvent(event: OnSearchQueryChange) {
        state = state.copy(searchQuery = event.query)
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500L)
            getCompanyListing()
        }
    }

    private fun getCompanyListing(
        query: String = state.searchQuery.lowercase(),
        fetchFromRemote: Boolean = false
    ) {
        viewModelScope.launch {
            repository.getCompanyListings(fetchFromRemote, query)
                .collect { result ->
                    when (result) {
                        is Success -> {
                            result.data?.let { listing ->
                                state = state.copy(
                                    companies = listing
                                )
                            }
                        }
                        is Error -> Unit
                        is Loading -> {
                            state = state.copy(isLoading = state.isLoading)
                        }
                    }

                }
        }
    }
}