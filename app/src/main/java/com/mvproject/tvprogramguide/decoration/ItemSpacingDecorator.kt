package com.mvproject.tvprogramguide.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

/**
 * Decorator class for recyclerview items spacing
 */
class ItemSpacingDecorator constructor(
    private val spacing: Int
) :
    ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildViewHolder(view).bindingAdapterPosition
        val itemCount = state.itemCount
        setSpacingForDirection(outRect, position, itemCount)
    }

    private fun setSpacingForDirection(
        outRect: Rect,
        position: Int,
        itemCount: Int
    ) {
        outRect.apply {
            top = spacing
            left = spacing
            right = spacing
            bottom = if (position == itemCount - COUNT_ONE) spacing else COUNT_ZERO
        }
    }

    companion object {
        private const val COUNT_ONE = 1
        private const val COUNT_ZERO = 0
    }
}
