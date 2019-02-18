package com.ar.ciu.ciuar

import android.graphics.Canvas
import android.graphics.Paint
import android.text.*
import android.text.style.LeadingMarginSpan


internal class MyLeadingMarginSpan2(private val lines: Int, private val margin: Int) : LeadingMarginSpan.LeadingMarginSpan2 {

    /* Возвращает значение, на которе должен быть добавлен отступ */
    override fun getLeadingMargin(first: Boolean): Int {
        return if (first) { margin } else { 0 }
    }
    override
    fun drawLeadingMargin(c: Canvas, p: Paint, x: Int, dir: Int,
                          top: Int, baseline: Int, bottom: Int, text: CharSequence,
                          start: Int, end: Int, first: Boolean, layout: Layout) {
    }

    override fun getLeadingMarginLineCount(): Int {
        return lines
    }
};