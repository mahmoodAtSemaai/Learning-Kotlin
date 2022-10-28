package com.webkul.mobikul.odoo.ui.learning_anko.data.table

object PERSON : BASE_TABLE {
	override val TABLE_NAME = "person_table"
	const val ID = "_id"
	const val PERSON_NAME = "name"
	const val AGE = "age"
	const val GENDER = "gender"


	val TABLE_CREATION_QUERY by lazy {
		("CREATE TABLE " + TABLE_NAME + "("
				+ ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ PERSON_NAME + " TEXT,"
				+ AGE + " INTEGER unsigned,"
				+ GENDER + " TEXT"
				+ ")")
	}

}

/*
CREATE DATABASE db;

CREATE TABLE person_table
(
PersonID int,
LastName varchar(255),
FirstName varchar(255),
Address varchar(255),
City varchar(255)
);

describe person_table;

INSERT INTO person_table (PersonID, LastName, FirstName, Address, City)
VALUES ('Cardinal', 'Stavanger', 'Norway', 'Some random address', 'Some Random city');
*/