package com.example.financialcontrol

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.icu.util.LocaleData
import java.time.LocalDateTime

class DatabaseHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION)
{
    companion object
    {
        private const val DATABASE_NAME = "FinancialDatabase"
        private const val DATABASE_VERSION = 1

        const val USERS_TABLE_NAME = "Users"
        const val USERS_ID_COL = "id"
        const val USERS_NAME_COL = "name"
        const val USERS_EMAIL_COL = "email"

        const val TRANSACTIONS_TABLE_NAME = "Transactions"
        const val TRANSACTIONS_ID_COL = "id"
        const val TRANSACTIONS_DATETIME_COL = "datetime"
        const val TRANSACTIONS_AMOUNT_COL = "amount"
        const val TRANSACTIONS_ID_TYPE_COL = "idType"
        const val TRANSACTIONS_IS_DELETED_COL = "isDeleted"
    }

    override fun onCreate(db: SQLiteDatabase)
    {
        var query = ("CREATE TABLE " + USERS_TABLE_NAME + " ("
            + USERS_ID_COL + " INTEGER PRIMARY KEY, " +
            USERS_NAME_COL + " TEXT," +
            USERS_EMAIL_COL + " TEXT" + ")")

        db.execSQL(query)


        query = ("CREATE TABLE " + TRANSACTIONS_TABLE_NAME + " ("
                + TRANSACTIONS_ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TRANSACTIONS_DATETIME_COL + " DATETIME, " +
                TRANSACTIONS_AMOUNT_COL + " REAL, " +
                TRANSACTIONS_ID_TYPE_COL + " INTEGER, " +
                TRANSACTIONS_IS_DELETED_COL + " INTEGER" + ")")

        db.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int)
    {
        db.execSQL("DROP TABLE IF EXISTS $USERS_TABLE_NAME")
        db.execSQL("DROP TABLE IF EXISTS $TRANSACTIONS_TABLE_NAME")
        onCreate(db)
    }

    fun addUser(name : String, email : String )
    {
        val values = ContentValues()

        values.put(USERS_NAME_COL, name)
        values.put(USERS_EMAIL_COL, email)

        val db = this.writableDatabase

        db.insert(USERS_TABLE_NAME, null, values)
        db.close()
    }

    fun getUsers(): Cursor
    {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $USERS_TABLE_NAME", null)
    }

    fun addTransaction(datetime: LocalDateTime, amount: Double, idType: Int)
    {
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(TRANSACTIONS_DATETIME_COL, datetime.toString())
        values.put(TRANSACTIONS_AMOUNT_COL, amount)
        values.put(TRANSACTIONS_ID_TYPE_COL, idType)
        values.put(TRANSACTIONS_IS_DELETED_COL, 0)

        db.insert(TRANSACTIONS_TABLE_NAME, null, values)
        db.close()
    }

    fun setIsDeletedTransaction(id: Int)
    {
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(TRANSACTIONS_IS_DELETED_COL, 1)

        db.update(TRANSACTIONS_TABLE_NAME, values, "id=?", arrayOf(id.toString()))
        db.close()
    }

    fun getTransactions(): Cursor
    {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $TRANSACTIONS_TABLE_NAME", null)
    }
}

