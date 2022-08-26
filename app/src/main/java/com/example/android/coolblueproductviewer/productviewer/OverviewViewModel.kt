package com.example.android.coolblueproductviewer.productviewer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState.Error
import androidx.paging.LoadState.Loading
import androidx.paging.LoadState.NotLoading
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.liveData
import com.example.android.coolblueproductviewer.entities.RequestStatus
import com.example.android.coolblueproductviewer.network.CoolblueApiService
import com.example.android.coolblueproductviewer.network.CoolblueProductsPagingSource
import timber.log.Timber

class OverviewViewModel : ViewModel() {

    private val networkService = CoolblueApiService()

    private val _loadStatus = MutableLiveData<RequestStatus>()
    val loadStatus: LiveData<RequestStatus>
        get() = _loadStatus

    private val _filterString = MutableLiveData<String?>()
    val filterString: LiveData<String?>
        get() = _filterString

    val productsPager = Pager(
        config = PagingConfig(pageSize = DEFAULT_PAGE_SIZE)
    ) {
        CoolblueProductsPagingSource(networkService)
    }.liveData.cachedIn(viewModelScope)

    fun updateFilter(newFilter: String? = null) {
        Timber.d("updateFilter: $newFilter")

        _filterString.value = newFilter
    }

    fun updateLoadingStatus(loadStates: CombinedLoadStates) {
        _loadStatus.value = when (loadStates.refresh) {
            is NotLoading -> RequestStatus.DONE
            is Loading -> RequestStatus.LOADING
            is Error -> RequestStatus.ERROR
        }
    }

    fun applyApiFilter(newFilter: String?) {
        networkService.updateFilter(newFilter)
    }

    companion object {

        private const val DEFAULT_PAGE_SIZE = 20
    }
}
