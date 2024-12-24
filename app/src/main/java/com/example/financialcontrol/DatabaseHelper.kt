package com.example.financialcontrol

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.time.LocalDate
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
        const val USERS_GENDER_ID_COL = "genderId"
        const val USERS_BIRTHDAY_COL = "birthday"
        const val USERS_EMAIL_COL = "email"

        const val GENDERS_TABLE_NAME = "Genders"
        const val GENDERS_ID_COL = "id"
        const val GENDERS_GENDER_COL = "gender"

        const val TRANSACTIONS_TABLE_NAME = "Transactions"
        const val TRANSACTIONS_ID_COL = "id"
        const val TRANSACTIONS_DATETIME_COL = "datetime"
        const val TRANSACTIONS_AMOUNT_COL = "amount"
        const val TRANSACTIONS_ID_TYPE_COL = "idType"
        const val TRANSACTIONS_IS_DELETED_COL = "isDeleted"

        const val TYPES_OF_TRANSACTIONS_TABLE_NAME = "TypesOfTransactions"
        const val TYPES_OF_TRANSACTIONS_ID_COL = "id"
        const val TYPES_OF_TRANSACTIONS_TYPE_COL = "type"
    }

    override fun onCreate(db: SQLiteDatabase)
    {
        var query = ("""
            CREATE TABLE $USERS_TABLE_NAME 
            (
                $USERS_ID_COL INTEGER PRIMARY KEY AUTOINCREMENT,
                $USERS_NAME_COL TEXT,
                $USERS_GENDER_ID_COL INTEGER,
                $USERS_BIRTHDAY_COL DATE,
                $USERS_EMAIL_COL TEXT,
                FOREIGN KEY ($USERS_GENDER_ID_COL)
                REFERENCES $GENDERS_TABLE_NAME ($GENDERS_ID_COL)
            );
        """.trimIndent())
        db.execSQL(query)

        query = ("""
            CREATE TABLE $GENDERS_TABLE_NAME
            (
                $GENDERS_ID_COL INTEGER PRIMARY KEY AUTOINCREMENT,
                $GENDERS_GENDER_COL TEXT
            ); 
        """.trimIndent())
        db.execSQL(query)

        query = ("""
            CREATE TABLE $TRANSACTIONS_TABLE_NAME 
            (
                $TRANSACTIONS_ID_COL INTEGER PRIMARY KEY AUTOINCREMENT,
                $TRANSACTIONS_DATETIME_COL DATETIME,
                $TRANSACTIONS_AMOUNT_COL REAL,
                $TRANSACTIONS_ID_TYPE_COL INTEGER,
                $TRANSACTIONS_IS_DELETED_COL INTEGER,
                FOREIGN KEY ($TRANSACTIONS_ID_TYPE_COL)
                REFERENCES $TYPES_OF_TRANSACTIONS_TABLE_NAME ($TYPES_OF_TRANSACTIONS_TABLE_NAME)
            );
        """.trimIndent())
        db.execSQL(query)

        query = ("""
            CREATE TABLE $TYPES_OF_TRANSACTIONS_TABLE_NAME
            (
                $TYPES_OF_TRANSACTIONS_ID_COL INTEGER PRIMARY KEY AUTOINCREMENT,
                $TYPES_OF_TRANSACTIONS_TYPE_COL TEXT
            ); 
        """.trimIndent())
        db.execSQL(query)

        val valuesGenders = ContentValues()
        valuesGenders.put(GENDERS_GENDER_COL, "Woman")
        db.insert(GENDERS_TABLE_NAME, null, valuesGenders)
        valuesGenders.put(GENDERS_GENDER_COL, "Man")
        db.insert(GENDERS_TABLE_NAME, null, valuesGenders)

        val valuesTypesOfTransactions = ContentValues()
        valuesTypesOfTransactions.put(TYPES_OF_TRANSACTIONS_TYPE_COL, "Deposit")
        db.insert(TYPES_OF_TRANSACTIONS_TABLE_NAME, null, valuesTypesOfTransactions)
        valuesTypesOfTransactions.put(TYPES_OF_TRANSACTIONS_TYPE_COL, "Withdraw")
        db.insert(TYPES_OF_TRANSACTIONS_TABLE_NAME, null, valuesTypesOfTransactions)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int)
    {
        db.execSQL("DROP TABLE IF EXISTS $USERS_TABLE_NAME")
        db.execSQL("DROP TABLE IF EXISTS $TRANSACTIONS_TABLE_NAME")
        onCreate(db)
    }

    fun selectGenderInGendersById(id: Int): String
    {
        val db = this.readableDatabase
        val query = """
            SELECT $GENDERS_GENDER_COL FROM $GENDERS_TABLE_NAME 
            WHERE $GENDERS_ID_COL = $id
        """.trimIndent()

        val cursor = db.rawQuery(query, null)
        var gender: String
        if(!cursor.moveToFirst())
        {
            gender = ""
        }
        gender = cursor.getString(0)
        cursor.close()
        return gender
    }

    fun selectIdInGendersByGender(gender: String): Cursor
    {
        val db = this.readableDatabase
        val query = """
            SELECT $GENDERS_ID_COL FROM $GENDERS_TABLE_NAME 
            WHERE $GENDERS_GENDER_COL = "$gender"
        """.trimIndent()
        return db.rawQuery(query, null)
    }

    fun selectTypeOfTransactionInTypesOfTransactionsById(id: Int): String
    {
        val db = this.readableDatabase
        val query = """
            SELECT $TYPES_OF_TRANSACTIONS_TYPE_COL FROM $TYPES_OF_TRANSACTIONS_TABLE_NAME
            WHERE $TYPES_OF_TRANSACTIONS_ID_COL = $id
        """.trimIndent()

        val cursor = db.rawQuery(query, null)
        var type: String
        if(!cursor.moveToFirst())
        {
            type = ""
        }
        type = cursor.getString(0)
        cursor.close()
        return type
    }

    fun selectIdInTypesOfTransactionsByTypeOfTransaction(typeOfTransaction: String): Int
    {
        val db = this.readableDatabase
        val query = """
            SELECT $TYPES_OF_TRANSACTIONS_ID_COL FROM $TYPES_OF_TRANSACTIONS_TABLE_NAME 
            WHERE $TYPES_OF_TRANSACTIONS_TYPE_COL = "$typeOfTransaction"
        """.trimIndent()
        val cursor = db.rawQuery(query, null)
        var id: Int
        if(!cursor.moveToFirst())
        {
            id = -1
        }
        id = cursor.getInt(0)
        cursor.close()
        return id
    }

    fun addUser(name : String, email : String, gender: Int, birthday: LocalDate  )
    {
        val values = ContentValues()

        values.put(USERS_NAME_COL, name)
        values.put(USERS_GENDER_ID_COL, gender)
        values.put(USERS_BIRTHDAY_COL, birthday.toString())
        values.put(USERS_EMAIL_COL, email)

        val db = this.writableDatabase

        db.insert(USERS_TABLE_NAME, null, values)
        db.close()
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
    fun addGender(gender: String)
    {
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(GENDERS_GENDER_COL, gender)

        db.insert(GENDERS_TABLE_NAME, null, values)
        db.close()
    }
    fun addTypesOfTransactions(types: String)
    {
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(TYPES_OF_TRANSACTIONS_TYPE_COL, types)

        db.insert(TYPES_OF_TRANSACTIONS_TABLE_NAME, null, values)
        db.close()
    }

    fun updateUser(id: Int, name : String, email : String, gender: Int, birthday: LocalDate  )
    {
        val values = ContentValues()

        values.put(USERS_NAME_COL, name)
        values.put(USERS_GENDER_ID_COL, gender)
        values.put(USERS_BIRTHDAY_COL, birthday.toString())
        values.put(USERS_EMAIL_COL, email)

        val db = this.writableDatabase

        db.update(USERS_TABLE_NAME, values, "id=?", arrayOf(id.toString()))
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

    fun getUsers(): Cursor
    {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $USERS_TABLE_NAME", null)
    }
    fun getTransactions(): Cursor
    {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $TRANSACTIONS_TABLE_NAME", null)
    }
}

