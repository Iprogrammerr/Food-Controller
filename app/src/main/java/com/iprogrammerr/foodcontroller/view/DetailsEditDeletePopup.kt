package com.iprogrammerr.foodcontroller.view

import android.support.v7.widget.PopupMenu
import android.view.ContextThemeWrapper
import android.view.Gravity
import android.view.View
import com.iprogrammerr.foodcontroller.R

class DetailsEditDeletePopup(
    private val anchor: View,
    private val details: () -> Unit,
    private val edit: () -> Unit,
    private val delete: () -> Unit
) : Component {

    override fun show() {
        val popup = PopupMenu(
            ContextThemeWrapper(this.anchor.context, R.style.PopUpStyle), this.anchor
        )
        popup.gravity = Gravity.END
        popup.menuInflater.inflate(R.menu.details_edit_delete_drop_down, popup.menu)
        popup.setOnMenuItemClickListener { i ->
            when (i.itemId) {
                R.id.details -> this.details()
                R.id.edit -> this.edit()
                else -> this.delete()
            }
            true
        }
        popup.show()
    }
}