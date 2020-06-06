package com.pawthunder.currencyexample.ui.common

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.pawthunder.currencyexample.util.AppExecutors

/**
 * Parent class for RecyclerView.Adapter's.
 */
abstract class DataBoundListAdapter<T, V : ViewDataBinding>(
    appExecutors: AppExecutors,
    diffCallback: DiffUtil.ItemCallback<T>
) : ListAdapter<T, DataBoundHolder<V>>(
    AsyncDifferConfig.Builder<T>(diffCallback)
        .setBackgroundThreadExecutor(appExecutors.diskIO())
        .build()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataBoundHolder(createBinding(parent))


    override fun onBindViewHolder(holder: DataBoundHolder<V>, position: Int) {
        bind(holder.binding, getItem(position), position)
        holder.binding.executePendingBindings()
    }

    /**
     * Layout with ViewDataBinding is created and assigned to RecyclerView.
     * @param parent Parent View holding RecyclerView.
     * @return [ViewDataBinding] object associated with layout.
     */
    protected abstract fun createBinding(parent: ViewGroup): V

    /**
     * New item was bound to layout. Layout should be reinitialized to reflect data of item.
     * @param binding [ViewDataBinding] object which should be reinitialized with new item
     * @param item New item bound to layout.
     * @param position Position of item in adapter's list of items.
     */
    protected abstract fun bind(binding: V, item: T, position: Int)
}
