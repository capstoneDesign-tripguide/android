package com.example.secondcapstone

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
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

        val testBtn = findViewById<Button>(R.id.testbtn)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)

        testBtn.setOnClickListener { //드로어 레이아웃
            if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                drawerLayout.closeDrawer(GravityCompat.END)
            } else {
                drawerLayout.openDrawer(GravityCompat.END)
            }
        }



        alarm.visibility = View.GONE //처음에 문구는 보이지 않음

        //비밀번호 텍스트 마스킹
        input_password.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            // 텍스트 변경 전에 호출되는 TextWatcher의 필수 멤버 함수.
            // 여기선 텍스트 변경 전에 할 일이 없으니까 패스
            }


            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // 텍스트 변경 시 호출되는 TextWatcher의 필수 멤버 함수
                val maskedText = maskPassword(s) //마스킹된 문자열
                input_password.removeTextChangedListener(this) // setText 함수가 무한 호출될 수 있으므로 텍스트 와처 제거
                //setText를 호출하면 또 다시 TextWatcher의 메서드가 호출될 수 있기 때문이다.
                input_password.setText(maskedText) // 마스킹된 텍스트 설정
                input_password.setSelection(maskedText.length) // 커서를 마지막 위치로 이동
                input_password.addTextChangedListener(this) // 텍스트 변경 리스너 다시 추가
            }
            override fun afterTextChanged(p0: Editable?) {
                // 텍스트 변경 후에 호출되는 TextWatcher의 필수 멤버 함수.
                // 여기선 텍스트 변경 전에 할 일이 없으니까 패스
            }
        })

        //비밀번호 확인 텎스트 마스킹
        check_password.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {  }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val maskedText = maskPassword(p0)
                check_password.removeTextChangedListener(this)
                check_password.setText(maskedText)
                check_password.setSelection(maskedText.length)
                check_password.addTextChangedListener(this)
            }

            override fun afterTextChanged(p0: Editable?) {  }

        })

        //회원가입 버튼 클릭 이벤트 처리
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
            else{ //정보를 모두 정상적으로 입력 시 서버로 데이터 전송
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

    //텍스트 마스킹 함수. 최근 입력한 문자만 표시하고, 나머지 문자는 *로 마스킹한 채로 반환한다.
        private fun maskPassword(s: CharSequence?): String { // CharSequence 객체 s를 매개변수로 받음
            // 최근에 입력된 문자만 표시하고 나머지는 특수문자로 마스킹하기
            val maskedText = StringBuilder()
            s?.let {
                for (i in 0 until it.length - 1) {
                    maskedText.append("*")
                }
                if (it.isNotEmpty()) {
                    maskedText.append(it[it.length - 1]) // 마지막 문자는 표시
                }
            }
            return maskedText.toString()
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
