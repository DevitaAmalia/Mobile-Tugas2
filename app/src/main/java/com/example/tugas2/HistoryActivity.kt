package com.example.tugas2

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tugas2.sqlite.MyDB
import com.example.tugas2.sqlite.WaterHistory

class HistoryActivity : AppCompatActivity() {

    private lateinit var dbHelper: MyDB
    private lateinit var adapter: HistoryAdapter
    private val historyList = mutableListOf<WaterHistory>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        dbHelper = MyDB(this)
        val rvHistory: RecyclerView = findViewById(R.id.rvHistory)
        val btnBack: ImageButton = findViewById(R.id.btnBack)

        // ambil data
        loadData()

        // RecyclerView
        adapter = HistoryAdapter(historyList)
        rvHistory.layoutManager = LinearLayoutManager(this)
        rvHistory.adapter = adapter

        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun loadData() {
        val cursor = dbHelper.getAllData()
        if (cursor.moveToFirst()) {
            do {
                val item = WaterHistory(
                    cursor.getInt(cursor.getColumnIndexOrThrow(MyDB.COL_ID)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(MyDB.COL_INPUT)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(MyDB.COL_TOTAL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(MyDB.COL_TIMESTAMP))
                )
                historyList.add(item)
            } while (cursor.moveToNext())
        }
        cursor.close()
    }

}