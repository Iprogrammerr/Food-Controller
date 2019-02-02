package com.iprogrammerr.foodcontroller.viewmodel

import android.arch.lifecycle.ViewModel
import com.iprogrammerr.foodcontroller.ObjectsPool
import com.iprogrammerr.foodcontroller.model.Asynchronous
import com.iprogrammerr.foodcontroller.model.day.Days
import com.iprogrammerr.foodcontroller.model.day.Weight
import com.iprogrammerr.foodcontroller.model.goals.Goals
import com.iprogrammerr.foodcontroller.model.result.Callback
import com.iprogrammerr.foodcontroller.model.scalar.StickableScalar
import com.iprogrammerr.foodcontroller.model.scalar.StickyScalar

class MenuViewModel(
    private val asynchronous: Asynchronous,
    private val days: Days,
    private val weight: Weight,
    private val goals: Goals
) : ViewModel() {

    private val started: StickableScalar<Boolean> =
        StickyScalar { this.days.exists(System.currentTimeMillis()) }
    private val lastWeight: StickableScalar<Double> =
        StickyScalar { this.weight.value() }

    constructor() : this(
        ObjectsPool.single(Asynchronous::class.java),
        ObjectsPool.single(Days::class.java),
        ObjectsPool.single(Weight::class.java),
        ObjectsPool.single(Goals::class.java)
    )

    fun dayStarted(callback: Callback<Boolean>) {
        this.asynchronous.execute({ this.started.value() }, callback)
    }

    fun lastWeight(callback: Callback<Double>) {
        this.asynchronous.execute({ this.lastWeight.value() }, callback)
    }

    fun createDay(weight: Double, callback: Callback<Boolean>) {
        this.asynchronous.execute({
            this.days.create(weight, this.goals.calories().value(), this.goals.protein().value())
            true
        }, callback)
    }

    fun refresh() {
        println("Refresh!")
        this.started.unstick()
        this.lastWeight.unstick()
    }
}