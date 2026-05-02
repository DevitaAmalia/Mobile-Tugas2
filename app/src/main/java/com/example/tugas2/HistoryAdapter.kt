package com.example.tugas2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tugas2.sqlite.WaterHistory

class HistoryAdapter(private val historyList: List<WaterHistory>) :
    RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    // ref komponen UI di item_history
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvInput: TextView = view.findViewById(R.id.tvHistoryInput)
        val tvTotal: TextView = view.findViewById(R.id.tvHistoryTotal)
        val tvTime: TextView = view.findViewById(R.id.tvHistoryTimestamp)
    }

    // layout CardView untuk setiap item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history, parent, false)
        return ViewHolder(view)
    }

    // isi data ke dalam komponen UI
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = historyList[position]
        holder.tvInput.text = "+ ${item.inputAmount} ml"
        holder.tvTotal.text = "Total: ${item.totalAfter} ml"
        holder.tvTime.text = item.timestamp
    }

    // beritahu RecyclerView jumlah data yang ada
    override fun getItemCount(): Int = historyList.size
}