package com.example.financialcontrol

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION)
{
    companion object
    {
        private const val DATABASE_NAME = "FinancialDatabase"
        private const val DATABASE_VERSION = 1

        const val TABLE_NAME = "Users"
        const val ID_COL = "id"
        const val NAME_COL = "name"
        const val EMAIL_COL = "email"


    }

    override fun onCreate(db: SQLiteDatabase)
    {
        val query = ("CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY, " +
                NAME_COL + " TEXT," +
                EMAIL_COL + " TEXT" + ")")

        db.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int)
    {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addUser(name : String, email : String )
    {
        val values = ContentValues()

        values.put(NAME_COL, name)
        values.put(EMAIL_COL, email)

        val db = this.writableDatabase

        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun getUsers(): Cursor
    {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_NAME", null)
    }
}

