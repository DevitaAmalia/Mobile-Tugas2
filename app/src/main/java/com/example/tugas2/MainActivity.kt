package com.example.tugas2

import com.example.tugas2.sqlite.MyDB
import androidx.activity.enableEdgeToEdge
import android.os.Bundle
import android.text.InputType
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.content.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private var totalIntake: Int = 0 // input intake pengguna
    private var targetIntake: Int = 2000 // default target
    private lateinit var dbHelper: MyDB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val tvCurrent : TextView = findViewById(R.id.tvCurrentIntake)
        val tvTarget : TextView= findViewById(R.id.tvTargetDisplay)
        val etInput : EditText = findViewById(R.id.etInputWater)
        val btnAdd : Button = findViewById(R.id.btnCalculate)
        val btnEditTarget: ImageButton = findViewById(R.id.btnEditTarget)
        val progressBar : ProgressBar= findViewById(R.id.progressBar)
        val tvSeeHistory : TextView = findViewById(R.id.tvSeeHistory)

        dbHelper = MyDB(this)
        targetIntake = loadGoal()
        totalIntake = dbHelper.getCurrentTotal()

        tvTarget.text = getString(R.string.target_label, targetIntake) // ambil string dari resource
        tvSeeHistory.paintFlags = tvSeeHistory.paintFlags or android.graphics.Paint.UNDERLINE_TEXT_FLAG

        updateUI(tvCurrent, progressBar)

        // pindah halaman
        tvSeeHistory.setOnClickListener {
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
        }

        // edit target
        btnEditTarget.setOnClickListener {
            val builder = AlertDialog.Builder(this) // untuk edit target pakai alert dialog
            builder.setTitle(getString(R.string.dialog_target_title))

            val inputTarget = EditText(this)
            inputTarget.inputType = InputType.TYPE_CLASS_NUMBER
            inputTarget.hint = getString(R.string.hint_set_target)
            builder.setView(inputTarget)

            builder.setPositiveButton(getString(R.string.btn_save)) { _, _ ->
                val newTargetStr = inputTarget.text.toString()
                if (newTargetStr.isNotEmpty()) {
                    targetIntake = newTargetStr.toInt()
                    saveGoal(targetIntake)
                    tvTarget.text = getString(R.string.target_label, targetIntake) // ubah tampilan target
                    updateUI(tvCurrent, progressBar)
                    Toast.makeText(this, getString(R.string.toast_target_changed, targetIntake), Toast.LENGTH_SHORT).show()
                }
            }
            builder.setNegativeButton(getString(R.string.btn_cancel)) { dialog, _ -> dialog.cancel() }
            builder.show()
        }

        // tambah intake
        btnAdd.setOnClickListener {
            val inputStr = etInput.text.toString()
            if (inputStr.isNotEmpty()) {
                val inputVal = inputStr.toInt()
                totalIntake += inputVal

                val timestamp = SimpleDateFormat("EEEE, dd MMM yyyy - HH:mm", Locale.getDefault()).format(Date())
                dbHelper.addData(inputVal, totalIntake, timestamp)

                if (totalIntake >= targetIntake) {
                    Toast.makeText(this, getString(R.string.toast_goal_reached), Toast.LENGTH_LONG).show()
                }

                updateUI(tvCurrent, progressBar)
                etInput.text.clear()
            }
        }

    }
    private fun updateUI(tv: TextView, pb: ProgressBar) { // sinkron UI
        tv.text = getString(R.string.current_intake_format, totalIntake)
        val progress = if (targetIntake > 0) (totalIntake * 100) / targetIntake else 0
        pb.progress = progress
    }

    private fun saveGoal(goal: Int) {
        val sharedPref = getSharedPreferences("WaterPrefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putInt("goal_key", goal)
            apply()
        }
    }

    private fun loadGoal(): Int {
        val sharedPref = getSharedPreferences("WaterPrefs", Context.MODE_PRIVATE)
        return sharedPref.getInt("goal_key", 2000)
    }
}