package com.example.shoppinglistapp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues
import android.database.Cursor

class ShoppingListDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "shoppingList.db"
        private const val DATABASE_VERSION = 1

        const val TABLE_NAME = "shopping_list"
        const val COLUMN_ID = "id"
        const val COLUMN_ITEM_NAME = "item_name"

        private const val SQL_CREATE_ENTRIES =
            "CREATE TABLE $TABLE_NAME (" +
                    "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$COLUMN_ITEM_NAME TEXT)"

        private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS $TABLE_NAME"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    fun insertItem(item: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_ITEM_NAME, item)
        }
        db.insert(TABLE_NAME, null, values)
    }

    fun deleteItem(item: String) {
        val db = writableDatabase
        db.delete(TABLE_NAME, "$COLUMN_ITEM_NAME = ?", arrayOf(item))
    }

    fun getAllItems(): ArrayList<String> {
        val db = readableDatabase
        val cursor: Cursor = db.query(
            TABLE_NAME,
            arrayOf(COLUMN_ITEM_NAME),
            null, null, null, null, null
        )

        val items = ArrayList<String>()
        with(cursor) {
            while (moveToNext()) {
                items.add(getString(getColumnIndexOrThrow(COLUMN_ITEM_NAME)))
            }
        }
        cursor.close()
        return items
    }
}

