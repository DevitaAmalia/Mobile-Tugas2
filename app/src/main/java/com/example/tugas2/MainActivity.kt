package com.example.tugas2

import androidx.activity.enableEdgeToEdge
import android.os.Bundle
import android.text.InputType
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private var totalIntake: Int = 0 // input intake pengguna
    private var targetIntake: Int = 2000 // default target

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

        tvTarget.text = getString(R.string.target_label, targetIntake) // ambil string dari resource

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
                totalIntake += inputStr.toInt()
                updateUI(tvCurrent, progressBar)
                etInput.text.clear()

                if (totalIntake >= targetIntake) {
                    Toast.makeText(this, getString(R.string.toast_goal_reached), Toast.LENGTH_LONG).show()
                }
            }
        }
    }
    private fun updateUI(tv: TextView, pb: ProgressBar) { // sinkron UI
        tv.text = getString(R.string.current_intake_format, totalIntake)
        val progress = if (targetIntake > 0) (totalIntake * 100) / targetIntake else 0
        pb.progress = progress
    }
}