package com.iprogrammerr.foodcontroller.model.food

import com.iprogrammerr.foodcontroller.model.NutritionalValues
import com.iprogrammerr.foodcontroller.model.scalar.StickyScalar
import kotlin.math.roundToInt

class ConstantFood(
    private val id: Long,
    private val name: String,
    private val weight: Int,
    private val calories: Int,
    private val protein: Double,
    private val definitionId: Long
) : Food {

    private val values = StickyScalar {
        object : NutritionalValues {

            override fun calories() =
                ((weight() / 100.0) * this@ConstantFood.calories).roundToInt()

            override fun protein() =
                (weight() / 100.0) * this@ConstantFood.protein
        }
    }

    override fun id() = this.id

    override fun name() = this.name

    override fun weight() = this.weight

    override fun values() = this.values.value()

    override fun definitionId() = this.definitionId
}