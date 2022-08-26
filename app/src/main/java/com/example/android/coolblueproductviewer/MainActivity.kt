package com.example.android.coolblueproductviewer

import android.os.Bundle
import android.widget.SearchView.OnQueryTextListener
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asFlow
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.android.coolblueproductviewer.databinding.ActivityMainBinding
import com.example.android.coolblueproductviewer.productviewer.OverviewViewModel
import com.example.android.coolblueproductviewer.productviewer.ProductListAdapter
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: OverviewViewModel by lazy {
        ViewModelProvider(this)[OverviewViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setupRecycledView()
        setupSearchBar()
    }

    @OptIn(FlowPreview::class)
    private fun setupRecycledView() {
        val productGridAdapter = ProductListAdapter()
        binding.productsGrid.adapter = productGridAdapter
        binding.productsGrid.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        viewModel.filterString.asFlow()
            .debounce(QUERY_THROTTLE_DURATION)
            .distinctUntilChanged()
            .onEach { newFilterString ->
                viewModel.applyApiFilter(newFilterString)
                productGridAdapter.refresh()
            }.launchIn(viewModel.viewModelScope)

        binding.swipeRefresh.setOnRefreshListener {
            productGridAdapter.refresh()
        }

        viewModel.productsPager.observe(this) { pagingData ->
            if (pagingData != null) {
                lifecycleScope.launch {
                    productGridAdapter.submitData(pagingData)
                }
            }
        }

        productGridAdapter.addLoadStateListener { loadStates ->
            viewModel.updateLoadingStatus(loadStates)
        }
    }

    private fun setupSearchBar() {
        binding.searchBar.setOnQueryTextListener(
            object : OnQueryTextListener {

                override fun onQueryTextSubmit(query: String?): Boolean {
                    viewModel.updateFilter(query)
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    viewModel.updateFilter(newText)
                    return false
                }
            }
        )
    }

    companion object {

        private const val QUERY_THROTTLE_DURATION = 300L
    }
}
