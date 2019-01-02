package com.iprogrammerr.foodcontroller.model.food

import com.iprogrammerr.foodcontroller.database.Database
import kotlin.math.roundToInt

class DatabaseFood(
    private val id: Long,
    private val database: Database
) : Food {

    private val attributes: MutableMap<String, Any> = HashMap()

    constructor(id: Long, database: Database, name: String, weight: Int, calories: Int, protein: Int) : this(
        id,
        database
    ) {
        this.attributes["name"] = name
        this.attributes["weight"] = weight
        this.attributes["calories"] = calories
        this.attributes["protein"] = protein
    }

    override fun id() = this.id

    override fun name(): String {
        if (!this.attributes.contains("name")) {
            load()
        }
        return this.attributes["name"] as String
    }

    override fun weight(): Int {
        if (!this.attributes.contains("weight")) {
            load()
        }
        return this.attributes["weight"] as Int
    }

    override fun calories(): Int {
        if (!this.attributes.contains("calories")) {
            load()
        }
        return ((weight() / 100.0) * this.attributes["calories"] as Int).roundToInt()
    }

    override fun protein(): Int {
        if (!this.attributes.contains("protein")) {
            load()
        }
        return ((weight() / 100.0) * this.attributes["protein"] as Int).roundToInt()
    }

    private fun load() {
        this.database.query(
            "select * from food inner join food_definition on food.definition_id = food_definition.id where id = ${this.id}"
        ).use { rs ->
            val row = rs.next()
            this.attributes["name"] = row.long("name")
            this.attributes["weight"] = row.int("weight")
            this.attributes["calories"] = row.int("calories")
            this.attributes["protein"] = row.int("protein")
        }
    }
}