package com.iprogrammerr.foodcontroller.view.fragment

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.iprogrammerr.foodcontroller.R
import com.iprogrammerr.foodcontroller.databinding.FragmentGoalsBinding
import com.iprogrammerr.foodcontroller.model.result.LifecycleCallback
import com.iprogrammerr.foodcontroller.model.scalar.DoubleFromView
import com.iprogrammerr.foodcontroller.model.scalar.IntFromView
import com.iprogrammerr.foodcontroller.view.RootView
import com.iprogrammerr.foodcontroller.view.dialog.ErrorDialog
import com.iprogrammerr.foodcontroller.view.dialog.InformationDialog
import com.iprogrammerr.foodcontroller.viewmodel.GoalsViewModel

class GoalsFragment : Fragment() {

    private lateinit var root: RootView
    private lateinit var binding: FragmentGoalsBinding
    private val viewModel by lazy {
        ViewModelProviders.of(this).get(GoalsViewModel::class.java)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        this.root = context as RootView
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        this.binding = DataBindingUtil.inflate(inflater, R.layout.fragment_goals, container, false)
        this.binding.weightInput.setText(
            this.viewModel.weightFormat.formatted(this.viewModel.goals().weight().value())
        )
        this.binding.caloriesInput.setText(this.viewModel.goals().calories().value().toString())
        this.binding.proteinInput.setText(this.viewModel.goals().protein().value().toString())
        this.binding.save.setOnClickListener { save() }
        this.root.changeTitle(getString(R.string.goals))
        return this.binding.root
    }

    private fun save() {
        val weight = DoubleFromView(this.binding.weightInput).value()
        val calories = IntFromView(this.binding.caloriesInput).value()
        val protein = IntFromView(this.binding.proteinInput).value()
        when {
            weight < 1 -> InformationDialog.new(getString(R.string.weight_invalid)).show(
                this.childFragmentManager)
            calories < 1 -> InformationDialog.new(getString(R.string.calories_invalid)).show(
                this.childFragmentManager)
            protein < 1 -> InformationDialog.new(getString(R.string.protein_invalid)).show(
                this.childFragmentManager)
            else -> saveIf(weight, calories, protein)
        }
    }

    private fun saveIf(weight: Double, calories: Int, protein: Int) {
        val changed = weight != this.viewModel.goals().weight().value()
            || calories != this.viewModel.goals().calories().value()
            || protein != this.viewModel.goals().protein().value()
        if (changed) {
            this.viewModel.update({ g ->
                g.weight().change(weight)
                g.calories().change(calories)
                g.protein().change(protein)
            }, LifecycleCallback(this) { r ->
                if (r.isSuccess()) {
                    InformationDialog.new(getString(R.string.save_success))
                        .show(this.childFragmentManager)
                } else {
                    ErrorDialog.new(r.exception())
                        .show(this.childFragmentManager)
                }
            })
        } else {
            InformationDialog.new(getString(R.string.no_change)).show(this.childFragmentManager)
        }
    }
}