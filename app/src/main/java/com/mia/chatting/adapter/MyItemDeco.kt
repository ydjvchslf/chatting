package com.mia.chatting.adapter

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.mia.chatting.App
import com.mia.chatting.R


class MyItemDeco(private val dividerHeight: Int): RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val itemPosition = parent.getChildAdapterPosition(view)

        parent.adapter?.let { adapter ->
            if (itemPosition != RecyclerView.NO_POSITION && itemPosition < adapter.itemCount - 1) {
                outRect.bottom = dividerHeight
            } else {
                outRect.bottom = 0
            }
        }
    }

    @SuppressLint("ResourceAsColor")
    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val dividerPaint = Paint()
        dividerPaint.color = ContextCompat.getColor(App.context(), R.color.light_grey)

        val childCount = parent.childCount
        for (i in 0 until childCount - 1) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams

            val dividerTop = child.bottom + params.bottomMargin
            val dividerBottom = dividerTop + dividerHeight

            canvas.drawRect(
                child.left.toFloat() + 50, // left margin
                dividerTop.toFloat(),
                child.right.toFloat() - 50, // right margin
                dividerBottom.toFloat(),
                dividerPaint
            )
        }
    }
}