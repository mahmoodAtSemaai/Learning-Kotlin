package com.webkul.mobikul.odoo.ui.learning_anko.data.sqlite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.webkul.mobikul.odoo.database.SaveData
import com.webkul.mobikul.odoo.ui.learning_anko.data.table.CLOTHES
import com.webkul.mobikul.odoo.ui.learning_anko.data.table.PERSON
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ManagedSqliteOpenHelper @Inject constructor(@ApplicationContext context: Context) :
	SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

	companion object {
		private const val DB_NAME = "db"
		private const val DB_VERSION = 1
	}

	override fun onCreate(p0: SQLiteDatabase?) {
		use {
			execSQL(PERSON.TABLE_CREATION_QUERY)
			execSQL(CLOTHES.TABLE_CREATION_QUERY)
		}
	}

	override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
		use {

		}
	}

	fun <T> use(f: SQLiteDatabase.() -> T): T {
		try {
			return readableDatabase.f()
		} finally {
			close()
		}
	}


}