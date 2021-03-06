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
import com.iprogrammerr.foodcontroller.databinding.FragmentMenuBinding
import com.iprogrammerr.foodcontroller.model.result.LifecycleCallback
import com.iprogrammerr.foodcontroller.view.RootView
import com.iprogrammerr.foodcontroller.view.dialog.ErrorDialog
import com.iprogrammerr.foodcontroller.view.dialog.TwoOptionsDialog
import com.iprogrammerr.foodcontroller.view.dialog.WeightDialog
import com.iprogrammerr.foodcontroller.viewmodel.MenuViewModel

private const val STARTED = "STARTED"

class MenuFragment : Fragment(), WeightDialog.Target, TwoOptionsDialog.Target {

    private lateinit var root: RootView
    private lateinit var binding: FragmentMenuBinding
    private val viewModel: MenuViewModel by lazy {
        ViewModelProviders.of(this).get(MenuViewModel::class.java)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        this.root = context as RootView
        this.arguments = this.arguments?.let { it } ?: Bundle()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.binding = DataBindingUtil.inflate(inflater, R.layout.fragment_menu, container, false)
        this.root.changeTitle(getString(R.string.app_name))
        this.binding.history.setOnClickListener { this.root.replace(YearsFragment(), true) }
        this.binding.base.setOnClickListener { this.root.replace(CategoriesFragment(), true) }
        this.binding.goals.setOnClickListener { this.root.replace(GoalsFragment(), true) }
        this.viewModel.dayStarted(LifecycleCallback(this) { r ->
            if (r.isSuccess()) {
                drawGreetings(r.value())
            } else {
                ErrorDialog.new(r.exception()).show(this.childFragmentManager)
            }
        })
        return this.binding.root
    }

    private fun drawGreetings(dayStarted: Boolean) {
        if (dayStarted) {
            this.binding.greetings.text = getString(R.string.day_greeting)
            this.binding.day.text = getString(R.string.back)
        } else {
            this.binding.greetings.text = getString(R.string.no_day_greeting)
            this.binding.day.text = getString(R.string.begin)
        }
        this.arguments?.putBoolean(STARTED, dayStarted)
        this.binding.day.setOnClickListener { startOrContinueDay() }
    }

    private fun startOrContinueDay() {
        this.viewModel.dayStarted(LifecycleCallback(this) { r ->
            if (r.isSuccess()) {
                if (!this.arguments!!.getBoolean(STARTED)) {
                    showWeightDialog()
                } else if (r.value()) {
                    this.root.replace(DayFragment.new(System.currentTimeMillis()), true)
                } else {
                    TwoOptionsDialog.new(
                        getString(R.string.day_ended),
                        getString(R.string.cancel),
                        getString(R.string.ok)
                    ).show(this.childFragmentManager)
                }
            } else {
                ErrorDialog.new(r.exception()).show(this.childFragmentManager)
            }
        })
    }

    private fun showWeightDialog() {
        this.viewModel.lastWeight(LifecycleCallback(this) { r ->
            if (r.isSuccess()) {
                WeightDialog.new(r.value()).show(this.childFragmentManager)
            } else {
                ErrorDialog.new(r.exception()).show(this.childFragmentManager)
            }
        })
    }

    override fun hit(weight: Double) {
        this.viewModel.createDay(weight, LifecycleCallback(this) { r ->
            if (r.isSuccess()) {
                this.root.replace(DayFragment.new(System.currentTimeMillis()), true)
            } else {
                ErrorDialog.new(r.exception()).show(this.childFragmentManager)
            }
        })
    }

    override fun hitLeft() {

    }

    override fun hitRight() {
        showWeightDialog()
    }
}
