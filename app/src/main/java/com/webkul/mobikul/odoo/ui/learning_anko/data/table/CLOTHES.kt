package com.webkul.mobikul.odoo.ui.learning_anko.data.table

object CLOTHES : BASE_TABLE {
	override val TABLE_NAME = "cloth_table"
	const val ID = "cloth_id"
	const val PERSON_ID = "person_id"
	const val NAME = "cloth_name"
	const val COLOR = "cloth_color"
	const val SIZE = "cloth_size"


	val TABLE_CREATION_QUERY by lazy {
		("CREATE TABLE " + TABLE_NAME + "("
				+ ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ NAME + " TEXT,"
				+ SIZE + " INTEGER unsigned,"
				+ COLOR + " TEXT,"
				+ PERSON_ID + "INTEGER FOREIGN KEY REFERENCES " + PERSON.TABLE_NAME + "(" + PERSON.ID + ")"
				+ ")")
	}
}