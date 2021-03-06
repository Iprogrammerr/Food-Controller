package com.iprogrammerr.foodcontroller.viewmodel

import android.arch.lifecycle.ViewModel
import com.iprogrammerr.foodcontroller.model.Asynchronous
import com.iprogrammerr.foodcontroller.model.food.FoodDefinition
import com.iprogrammerr.foodcontroller.model.food.FoodDefinitions
import com.iprogrammerr.foodcontroller.model.result.Callback
import com.iprogrammerr.foodcontroller.model.scalar.StartsWithFilter
import com.iprogrammerr.foodcontroller.model.scalar.StickyScalar
import com.iprogrammerr.foodcontroller.ObjectsPool

class FoodViewModel(private val asynchronous: Asynchronous, private val food: FoodDefinitions) : ViewModel() {

    private val all = StickyScalar { this.food.all() }

    constructor() : this(ObjectsPool.single(Asynchronous::class.java), ObjectsPool.single(FoodDefinitions::class.java))

    fun filtered(criteria: String, callback: Callback<List<FoodDefinition>>) {
        this.asynchronous.execute({
            StartsWithFilter(this.all.value(), criteria).value()
        }, callback)
    }

    fun all(callback: Callback<List<FoodDefinition>>) {
        this.asynchronous.execute({ this.all.value() }, callback)
    }

    fun refresh() {
        this.all.unstick()
    }
}