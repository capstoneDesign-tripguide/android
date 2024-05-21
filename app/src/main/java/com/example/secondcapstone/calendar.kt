package com.example.secondcapstone

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.MotionEvent
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnRangeSelectedListener
import com.prolificinteractive.materialcalendarview.format.ArrayWeekDayFormatter
import com.prolificinteractive.materialcalendarview.format.MonthArrayTitleFormatter
import com.prolificinteractive.materialcalendarview.format.TitleFormatter
import java.security.AccessController.getContext
import java.util.Calendar
import org.threeten.bp.DayOfWeek
import org.threeten.bp.temporal.ChronoUnit

class calendar : AppCompatActivity() {
    //lateinit은 전역 변수 느낌
    private lateinit var dateList: List<String>
    private var how_long_user_travel: Int = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.calendar)

        val destination = findViewById<Button>(R.id.search_travel_spot)
        destination.setOnClickListener {
            var intent = Intent(this, select_spot::class.java)
            startActivity(intent)
        }

        val closeBtn = findViewById<Button>(R.id.closeButton) //선택 완료 버튼
        closeBtn.setOnClickListener {
            var intent = Intent(this, travel_list::class.java)
            startActivity(intent)
        }



        val travelDate = findViewById<TextView>(R.id.travelDate)

        val calendarView =
            findViewById<com.prolificinteractive.materialcalendarview.MaterialCalendarView>(R.id.calendarview)
        //val travel_spot = findViewById<TextView>(R.id.travel_spot)
        val get_travel_spot = intent.getStringExtra("travel_spot")
        //travel_spot.text = get_travel_spot

        // 월, 요일을 한글로 보이게 설정 (MonthArrayTitleFormatter의 작동을 확인하려면 밑의 setTitleFormatter()를 지운다)
        calendarView.setTitleFormatter(MonthArrayTitleFormatter(resources.getTextArray(R.array.custom_months)))
        calendarView.setWeekDayFormatter(ArrayWeekDayFormatter(resources.getTextArray(R.array.custom_weekdays)))

        // 좌우 화살표 사이 연, 월의 폰트 스타일 설정
        calendarView.setHeaderTextAppearance(R.style.CalendarWidgetHeader)

        //mcv를 range모드로 설정했으므로 선택 날짜 범위의 리스트를 사용할 수 있다. 여기서 리스트 이름은 dates
        var startDay: String? = null
        var endDay: String? = null
        calendarView.setOnRangeSelectedListener(object : OnRangeSelectedListener {
            override fun onRangeSelected(widget: MaterialCalendarView, dates: List<CalendarDay>) {
//                //dates 리스트는 이런식으로 생김: [CalendarDay{2024-3-20}, CalendarDay{2024-3-21}]
//                startDay = dates[0].date.toString()
//                endDay = dates[dates.size - 1].date.toString()
////                Log.e(ContentValues.TAG, "시작일 : $startDay, 종료일 : $endDay")
////                Log.e(ContentValues.TAG, "$dates[0]")
////                Log.d(ContentValues.TAG, "$dates")
//
//                //리스트의 각 요소는 연도-월-일 순으로 나오는데, 연도는 제외하고 싶음
//                //따라서 연도(4)와 첫 '-'을 뺴기 위해 substring(5) 사용
//                dateList = dates.map { it.date.toString().substring(5) }.toList()
//                Log.d(ContentValues.TAG, "$dateList")
//                travelDate.text = "출발일: ${startDay}\n 도착일: ${endDay}"

                if (dates.isNotEmpty()) {
                    startDay = dates[0].date.toString()
                    endDay = dates[dates.size - 1].date.toString()

                    dateList = dates.map { it.date.toString().substring(5) }.toList()
                    travelDate.text = "출발일: ${startDay}\n 도착일: ${endDay}"

                    // 선택된 날짜 범위의 일수를 계산
                    val startDate = dates.first().date
                    val endDate = dates.last().date
                    how_long_user_travel = ChronoUnit.DAYS.between(startDate, endDate).toInt() + 1
                    Log.d("how_long_user_travel", "여행 기간: $how_long_user_travel 일")
                }
            }
        })

// 데코레이터 적용
        val sundayDecorator = SundayDecorator()
        val saturdayDecorator = SaturdayDecorator()
        calendarView.addDecorators(DayDecorator(this), sundayDecorator, saturdayDecorator)

// 좌우 화살표 가운데의 연/월이 보이는 방식 커스텀. 변경 전: 12월 2023 -> 변경 후:2023년 12월
        calendarView.setTitleFormatter { day ->
            val inputText = day.date
            val calendarHeaderElements = inputText.toString().split("-")
            val calendarHeaderBuilder = StringBuilder()

            calendarHeaderBuilder.append("${calendarHeaderElements[0]}년 ${calendarHeaderElements[1]}월")

            calendarHeaderBuilder.toString()
        }

        //시작일, 종료일 데이터 전달
        closeBtn.setOnClickListener {
            if (startDay != null && endDay != null){
                //calendar_nextActivity로 이동
                var intent = Intent(this, autoGenerate::class.java)
                Log.d("secondList", "$dateList")

                //list라서 putStringArrayListExtra 함수를 사용하고, ArrayList함수로 변형해서 전달해야 함
                //받을때도 getStringArrayListExtra 함수 사용
                intent.putStringArrayListExtra("dateList",ArrayList(dateList))
                intent.putExtra("how_long_user_travel", how_long_user_travel)
                startActivity(intent)
                finish() }
            else{
                Toast.makeText(this, "날짜를 선택해주세요.", Toast.LENGTH_LONG).show()
            }
        }
    }

    // 선택된 요일의 background를 설정하는 Decorator 클래스
    class DayDecorator(context: Context) : DayViewDecorator {
        private val drawable: Drawable =
            ContextCompat.getDrawable(context, R.drawable.calendar_selector)!!

        // true를 리턴 시 모든 요일에 내가 설정한 드로어블이 적용된다
        override fun shouldDecorate(day: CalendarDay): Boolean {
            return true
        }

        // DayViewFacade는 선택한 날짜를 의미함. 즉 전체 날짜 중(shouldDecorate) 선택한 날짜에만 drawable 적용
        override fun decorate(view: DayViewFacade) {
            view.setSelectionDrawable(drawable)
            view.addSpan(StyleSpan(Typeface.BOLD))   // 달력 안의 모든 숫자들이 볼드 처리됨
        }
    }

    class SundayDecorator: DayViewDecorator {
        override fun shouldDecorate(day: CalendarDay?): Boolean {
            //DayOfWeek.MONDAY로 해야 일요일이 선택됨. 음 이유는 모르겠다
            //DayOfWeek은 java.util이 아닌 import org.threeten.bp.DayOfWeek에서 불러와야 함!
            val sunday = day?.date?.with(DayOfWeek.MONDAY)?.dayOfMonth
            return sunday == day?.day
        }
        override fun decorate(view: DayViewFacade?) {
            view?.addSpan(object: ForegroundColorSpan(Color.RED){})
        }
    }

    class SaturdayDecorator: DayViewDecorator {
        override fun shouldDecorate(day: CalendarDay?): Boolean {
            //마찬가지로 SUNDAY로 해야 함
            val sunday = day?.date?.with(DayOfWeek.SUNDAY)?.dayOfMonth
            return sunday == day?.day
        }
        override fun decorate(view: DayViewFacade?) {
            view?.addSpan(object: ForegroundColorSpan(Color.BLUE){})
        }
    }
}