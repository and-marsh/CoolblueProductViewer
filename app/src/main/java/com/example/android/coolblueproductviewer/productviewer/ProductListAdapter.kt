package com.example.android.coolblueproductviewer.productviewer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.android.coolblueproductviewer.databinding.ListViewItemBinding
import com.example.android.coolblueproductviewer.entities.Product
import com.example.android.coolblueproductviewer.productviewer.ProductListAdapter.ProductViewHolder

class ProductListAdapter : PagingDataAdapter<Product, ProductViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder =
        ProductViewHolder(ListViewItemBinding.inflate(LayoutInflater.from(parent.context)))

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        getItem(position)?.run(holder::bind)
    }

    class ProductViewHolder(
        private var binding: ListViewItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            binding.product = product
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Product>() {

        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean =
            oldItem.productId == newItem.productId
    }
}
