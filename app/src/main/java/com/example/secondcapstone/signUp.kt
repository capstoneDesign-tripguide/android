package com.example.secondcapstone

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar

class signUp : AppCompatActivity() {

    private lateinit var editTextBirthday: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        val input_userid = findViewById<EditText>(R.id.USERID) //아이디
        val input_password = findViewById<EditText>(R.id.USERPASSWORD) //비밀번호
        val check_password = findViewById<EditText>(R.id.CHECK_PASSWORD)//비밀번호 확인
        val input_username = findViewById<EditText>(R.id.USERNAME) // 이름
        val input_nickname = findViewById<EditText>(R.id.USERNICKNAME) //닉네임
        val input_email = findViewById<EditText>(R.id.USEREAMAIL) //이메일
        val SIGNUP = findViewById<Button>(R.id.SIGNUP) //회원가입 버튼
        val alarm = findViewById<TextView>(R.id.alarm) //문구



        alarm.visibility = View.GONE //처음에 문구는 보이지 않음

        SIGNUP.setOnClickListener {
            val ID = input_userid.text.toString()
            val PASSWORD = input_password.text.toString()
            val CHECK_PASSWORD = check_password.text.toString()
            val NAME = input_username.text.toString()
            val NICKNAME = input_nickname.text.toString()
            val EMAIL = input_email.text.toString()
            Log.d("test0130", "${ID}, ${PASSWORD}, ${CHECK_PASSWORD}, ${NAME}, ${NICKNAME}, ${EMAIL}")

            if (ID == "") {
                Log.d("test0130", "ID is empty.")
                alarm.text = "아이디를 입력해 주세요."
                alarm.visibility = View.VISIBLE
            }
            else if(PASSWORD == "") {
                Log.d("test0130", "PASSWORD is empty.")
                alarm.text = "비밀번호를 입력해 주세요."
                alarm.visibility = View.VISIBLE
            }
            else if (PASSWORD != CHECK_PASSWORD){
                Log.d("test0130", "passwords are not matched")
                alarm.text = "비밀번호를 확인해 주세요."
                alarm.visibility = View.VISIBLE
            }
            else if (NAME == ""){
                Log.d("test0130", "NAME is empty.")
                alarm.text = "비밀번호를 입력해 주세요."
                alarm.visibility = View.VISIBLE
            }
            else if (NICKNAME == ""){
                Log.d("test0130", "NICKNAME is empty.")
                alarm.text = "닉네임을 입력해 주세요."
                alarm.visibility = View.VISIBLE
            }
            else if (EMAIL == ""){
                Log.d("test0130", "EMAIL is empty.")
                alarm.text = "이메일을 입력해 주세요."
                alarm.visibility = View.VISIBLE
            }
            else{
                if(alarm.visibility == 1){
                    alarm.visibility = View.GONE
                }
                Log.d("test0130", "good")

                //데이터 클래스로 만들어서 서버로 보내기
                val SignupDAta = SignupData(ID, PASSWORD, NAME)
            }

        }


//        editTextBirthday = findViewById(R.id.editText_birthday)
//
//        // EditText를 클릭하면 DatePickerDialog를 표시
//        editTextBirthday.setOnClickListener {
//            showDatePickerDialog()
        }
    }

//    private fun showDatePickerDialog() { //캘린더 모듈인데 나중에 쓰자
//        val calendar = Calendar.getInstance()
//        val year = calendar.get(Calendar.YEAR)
//        val month = calendar.get(Calendar.MONTH)
//        val day = calendar.get(Calendar.DAY_OF_MONTH)
//
//        val datePickerDialog = DatePickerDialog(this,
//            { _: DatePicker, selectedYear: Int, monthOfYear: Int, dayOfMonth: Int ->
//                // 날짜가 설정되었을 때 수행할 작업
//                // selectedYear, monthOfYear, dayOfMonth 값을 사용할 수 있습니다.
//                val selectedDate = "$selectedYear-${monthOfYear + 1}-$dayOfMonth"
//                editTextBirthday.setText(selectedDate)
//            }, year, month, day)
//
//        // DatePickerDialog를 표시
//        datePickerDialog.show()
//    }
//  }
