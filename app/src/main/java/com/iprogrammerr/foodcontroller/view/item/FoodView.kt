package com.iprogrammerr.foodcontroller.view.item

import android.support.v7.widget.RecyclerView
import android.view.View
import com.iprogrammerr.foodcontroller.R
import com.iprogrammerr.foodcontroller.databinding.ItemFoodBinding
import com.iprogrammerr.foodcontroller.model.Potential
import com.iprogrammerr.foodcontroller.model.food.Food
import com.iprogrammerr.foodcontroller.view.EditDeletePopup
import com.iprogrammerr.foodcontroller.view.items.FoodWithActionTarget
import com.iprogrammerr.foodcontroller.view.items.WithActionTarget
import kotlin.math.roundToInt

class FoodView(
    private val binding: ItemFoodBinding,
    private val target: Potential<FoodWithActionTarget>
) : RecyclerView.ViewHolder(binding.root), Drawable<Food> {

    override fun draw(item: Food) {
        this.binding.name.text = item.name()
        this.binding.weight.text = "${item.weight()} g"
        val values = item.values()
        this.binding.values.text = String.format(
            this.binding.root.context.getString(R.string.values_template),
            values.calories(),
            values.protein().roundToInt()
        )
        if (this.target.hasValue()) {
            this.binding.more.visibility = View.VISIBLE
            this.binding.root.setOnClickListener {
                EditDeletePopup(
                    this.binding.root,
                    { this.target.value().hit(item, WithActionTarget.Action.EDIT) },
                    { this.target.value().hit(item, WithActionTarget.Action.DELETE) }
                ).show()
            }
        } else {
            this.binding.more.visibility = View.GONE
        }
    }
}