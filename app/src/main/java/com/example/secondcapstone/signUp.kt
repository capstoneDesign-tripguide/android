package com.example.secondcapstone

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.DatePicker
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar

class signUp : AppCompatActivity() {

    private lateinit var editTextBirthday: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        editTextBirthday = findViewById(R.id.editText_birthday)

        // EditText를 클릭하면 DatePickerDialog를 표시
        editTextBirthday.setOnClickListener {
            showDatePickerDialog()
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this,
            { _: DatePicker, selectedYear: Int, monthOfYear: Int, dayOfMonth: Int ->
                // 날짜가 설정되었을 때 수행할 작업
                // selectedYear, monthOfYear, dayOfMonth 값을 사용할 수 있습니다.
                val selectedDate = "$selectedYear-${monthOfYear + 1}-$dayOfMonth"
                editTextBirthday.setText(selectedDate)
            }, year, month, day)

        // DatePickerDialog를 표시
        datePickerDialog.show()
    }
}
