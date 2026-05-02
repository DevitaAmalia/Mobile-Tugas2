package com.example.tugas2.sqlite

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.text.SimpleDateFormat
import java.util.*

class MyDB(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "WaterIntake.db"
        private const val DATABASE_VERSION = 2

        const val TABLE_NAME = "history_table"
        const val COL_ID = "id"
        const val COL_INPUT = "input_intake"
        const val COL_TOTAL = "total_intake"
        const val COL_TIMESTAMP = "timestamp"
        const val COL_DATE = "date"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = ("CREATE TABLE $TABLE_NAME (" +
                "$COL_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COL_INPUT INTEGER, " +
                "$COL_TOTAL INTEGER, " +
                "$COL_TIMESTAMP TEXT, "+
                "$COL_DATE TEXT)")
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // simpan data (create)
    fun addData(input: Int, total: Int, time: String): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()

        val dateOnly = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        contentValues.put(COL_INPUT, input)
        contentValues.put(COL_TOTAL, total)
        contentValues.put(COL_TIMESTAMP, time)
        contentValues.put(COL_DATE, dateOnly)

        return db.insert(TABLE_NAME, null, contentValues)
    }

    // ambil data (read)
    fun getAllData(): Cursor {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_NAME ORDER BY $COL_ID DESC", null)
    }

    // ambil data untuk total intake
    fun getCurrentTotal(): Int {
        val db = this.readableDatabase
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        val cursor = db.rawQuery(
            "SELECT SUM(input_intake) FROM history_table WHERE date = ?",
            arrayOf(today)
        )

        var total = 0
        if (cursor.moveToFirst()) {
            total = cursor.getInt(0)
        }
        cursor.close()
        return total
    }
}