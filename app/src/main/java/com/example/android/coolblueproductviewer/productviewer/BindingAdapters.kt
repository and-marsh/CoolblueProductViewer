package com.example.android.coolblueproductviewer.productviewer

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.android.coolblueproductviewer.R
import com.example.android.coolblueproductviewer.entities.RequestStatus
import com.example.android.coolblueproductviewer.utils.dp
import com.example.android.coolblueproductviewer.utils.gone
import com.example.android.coolblueproductviewer.utils.visible
import com.google.android.material.chip.Chip

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.run {
        Glide.with(imgView.context)
            .load(toUri().buildUpon().scheme("https").build())
            .apply(
                RequestOptions()
                    .override(200.dp, 300.dp)
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_broken_image)
            )
            .into(imgView)
    }
}

@BindingAdapter("refreshingStatus")
fun bindStatus(swipeRefreshLayout: SwipeRefreshLayout, status: RequestStatus?) {
    if (status != null) {
        swipeRefreshLayout.isRefreshing = status == RequestStatus.LOADING
    }
}

@BindingAdapter("coolblueApiStatus")
fun bindStatus(statusImageView: ImageView, status: RequestStatus?) {
    with(statusImageView) {

        when (status) {
            RequestStatus.LOADING -> {
                visible()
                setImageResource(R.drawable.loading_animation)
            }

            RequestStatus.ERROR -> {
                visible()
                setImageResource(R.drawable.ic_connection_error)
            }

            RequestStatus.DONE -> gone()

            else -> Unit
        }
    }
}

@BindingAdapter("coolblueApiStatus")
fun bindStatus(statusImageView: View, status: RequestStatus?) {
    when (status) {
        RequestStatus.ERROR -> statusImageView.gone()
        RequestStatus.DONE -> statusImageView.visible()
        else -> Unit
    }
}

@BindingAdapter("uspsFormatted")
fun TextView.setUSPsFormatted(item: List<String>) {
    text = item.joinToString(separator = "\n") { "- $it" }
}

@BindingAdapter("chipFormatted")
fun setChipFormatted(chip: Chip, item: String?) {
    if (item != null) {
        chip.text = item
        chip.visible()
    } else chip.gone()
}
