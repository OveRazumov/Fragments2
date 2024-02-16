package com.overazumov.userlist

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewDecoration(private val context: Context) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val itemCount = parent.adapter?.itemCount ?: 0

        if (position == 0) {
            outRect.top = context.resources.getDimensionPixelSize(R.dimen.medium_margin)
        }

        if (position == itemCount - 1) {
            outRect.bottom = context.resources.getDimensionPixelSize(R.dimen.medium_margin)
        }
    }
}