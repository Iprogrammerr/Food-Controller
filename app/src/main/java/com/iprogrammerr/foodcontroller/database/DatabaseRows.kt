package com.iprogrammerr.foodcontroller.database

import android.database.Cursor

class DatabaseRows(private val cursor: Cursor) : Rows {

    override fun current(): Row {
        return DatabaseRow(this.cursor)
    }

    override fun next(): Row {
        this.cursor.moveToNext()
        return DatabaseRow(this.cursor)
    }

    override fun hasNext() = !this.cursor.isLast

    override fun close() {
        this.cursor.close()
    }
}