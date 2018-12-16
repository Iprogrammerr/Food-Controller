package com.iprogrammerr.foodcontroller.pool

import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

class ObjectsPoolThatCanAddAndRetrieve(private val item: Any) : TypeSafeMatcher<ObjectsPool>() {

    override fun describeTo(description: Description) {
        description.appendText(this.javaClass.simpleName)
    }

    override fun matchesSafely(item: ObjectsPool): Boolean {
        var matched: Boolean
        try {
            item.add(this.item)
            matched = item.single(this.item.javaClass).equals(this.item)
        } catch (e: Exception) {
            e.printStackTrace()
            matched = false
        }
        return matched
    }
}