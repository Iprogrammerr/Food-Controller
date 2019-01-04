package com.iprogrammerr.foodcontroller.viewmodel

import android.arch.lifecycle.ViewModel
import com.iprogrammerr.foodcontroller.model.Asynchronous
import com.iprogrammerr.foodcontroller.model.StickyScalar
import com.iprogrammerr.foodcontroller.model.category.Categories
import com.iprogrammerr.foodcontroller.model.category.Category
import com.iprogrammerr.foodcontroller.model.result.Callback
import com.iprogrammerr.foodcontroller.pool.ObjectsPool

class CategoriesViewModel(private val asynchronous: Asynchronous, private val source: Categories) : ViewModel() {

    constructor() : this(ObjectsPool.single(Asynchronous::class.java), ObjectsPool.single(Categories::class.java))

    private val categories = StickyScalar { this.source.all() }

    fun categories(callback: Callback<List<Category>>) {
        this.asynchronous.execute({ this.categories.value() }, callback)
    }
}