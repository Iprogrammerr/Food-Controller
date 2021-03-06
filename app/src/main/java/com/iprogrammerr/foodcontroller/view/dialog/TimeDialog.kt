package com.iprogrammerr.foodcontroller.view.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Build
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.widget.TimePicker
import com.iprogrammerr.foodcontroller.R
import com.iprogrammerr.foodcontroller.databinding.DialogTimeBinding
import java.util.*

class TimeDialog : DialogFragment() {

    private lateinit var target: Target
    private val calendar by lazy {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = (this.arguments as Bundle).getLong(TIME)
        calendar
    }

    companion object {

        private const val TIME = "TIME"
        private const val TWENTY_FOUR = "TWENTY_FOUR"

        fun new(time: Long, twentyFour: Boolean): TimeDialog {
            val dialog = TimeDialog()
            val args = Bundle()
            args.putLong(TIME, time)
            args.putBoolean(TWENTY_FOUR, twentyFour)
            dialog.arguments = args
            return dialog
        }

        fun new(time: Long) = new(time, true)

        fun tag() = this::class.java.simpleName as String
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (this.parentFragment == null) {
            this.target = context as Target
        } else {
            this.target = this.parentFragment as Target
        }
    }

    @Suppress("DEPRECATION")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = AlertDialog.Builder(this.context).create()
        val binding: DialogTimeBinding = DataBindingUtil.inflate(
            LayoutInflater.from(this.context),
            R.layout.dialog_time,
            null,
            false
        )
        val args = this.arguments as Bundle
        binding.time.setIs24HourView(args.getBoolean(TWENTY_FOUR))
        val hour = if (binding.time.is24HourView) {
            this.calendar[Calendar.HOUR_OF_DAY]
        } else {
            this.calendar[Calendar.HOUR]
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.time.hour = hour
            binding.time.minute = this.calendar[Calendar.MINUTE]
        } else {
            binding.time.currentHour = hour
            binding.time.currentMinute = this.calendar[Calendar.MINUTE]
        }
        binding.cancel.setOnClickListener { dialog.dismiss() }
        binding.ok.setOnClickListener {
            dialog.dismiss()
            this.target.hit(time(binding.time))
        }
        dialog.setView(binding.root)
        return dialog
    }

    @Suppress("DEPRECATION")
    private fun time(picker: TimePicker): Long {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (picker.is24HourView) {
                this.calendar[Calendar.HOUR_OF_DAY] = picker.hour
            } else {
                this.calendar[Calendar.HOUR] = picker.hour
            }
            this.calendar[Calendar.MINUTE] = picker.minute
        } else {
            if (picker.is24HourView) {
                this.calendar[Calendar.HOUR_OF_DAY] = picker.currentHour
            } else {
                this.calendar[Calendar.HOUR] = picker.currentHour
            }
            this.calendar[Calendar.MINUTE] = picker.currentMinute
        }
        return this.calendar.timeInMillis
    }

    fun show(manager: FragmentManager) {
        if (manager.findFragmentByTag(tag()) == null) {
            show(manager, tag())
        }
    }

    interface Target {
        fun hit(time: Long)
    }
}