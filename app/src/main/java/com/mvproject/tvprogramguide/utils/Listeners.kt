package com.mvproject.tvprogramguide.utils

/**
 * Click listener definition for adapters
 */
fun interface OnClickListener<T> {
    fun onItemClick(item: T)
}

interface OnItemClickListener<T> {
    fun onItemClick(item: T)
    fun onDeleteClick(item: T)
}