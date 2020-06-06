package com.pawthunder.currencyexample.ui.common

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 * [RecyclerView] adapter holder containing [ViewDataBinding] of layout.
 * @constructor Constructor create simple holder with [ViewDataBinding].
 * @property binding [ViewDataBinding] object assigned to the holder.
 */
class DataBoundHolder<out T : ViewDataBinding> constructor(val binding: T) :
    RecyclerView.ViewHolder(binding.root)
