package com.example.secondcapstone

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.style.StyleSpan
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnRangeSelectedListener
import com.prolificinteractive.materialcalendarview.format.ArrayWeekDayFormatter
import com.prolificinteractive.materialcalendarview.format.MonthArrayTitleFormatter
import com.prolificinteractive.materialcalendarview.format.TitleFormatter



class directGenerate : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.direct_generate)
        val closeBtn = findViewById<Button>(R.id.closeButton)


        val travelDate = findViewById<TextView>(R.id.travelDate)

        val calendarView =
            findViewById<com.prolificinteractive.materialcalendarview.MaterialCalendarView>(R.id.calendarview)

//        //시작 요일이 월요일이 되도록 설정 -> 오류 때문에 일단 패스
//        calendarView.state().edit()
//            .setFirstDayOfWeek(DayOfWeek.of(Calendar.MONDAY))
//            .commit()
        // 월, 요일을 한글로 보이게 설정 (MonthArrayTitleFormatter의 작동을 확인하려면 밑의 setTitleFormatter()를 지운다)
        calendarView.setTitleFormatter(MonthArrayTitleFormatter(resources.getTextArray(R.array.custom_months)))
        calendarView.setWeekDayFormatter(ArrayWeekDayFormatter(resources.getTextArray(R.array.custom_weekdays)))
// 좌우 화살표 사이 연, 월의 폰트 스타일 설정
        calendarView.setHeaderTextAppearance(R.style.CalendarWidgetHeader)

        //날짜 범위 선택 리스너
        var startDay: String? = null
        var endDay: String? = null
        calendarView.setOnRangeSelectedListener(object : OnRangeSelectedListener {
            override fun onRangeSelected(widget: MaterialCalendarView, dates: List<CalendarDay>) {
                startDay = dates[0].date.toString()
                endDay = dates[dates.size - 1].date.toString()
                Log.e(TAG, "시작일 : $startDay, 종료일 : $endDay")
                Log.e(TAG, "$dates[0]")
                travelDate.text = "출발일: ${startDay}\n 도착일: ${endDay}"
            }
        })

// 일자 선택 시 내가 정의한 드로어블이 적용되도록 한다
                calendarView.addDecorators(DayDecorator(this))

// 좌우 화살표 가운데의 연/월이 보이는 방식 커스텀
            calendarView.setTitleFormatter(object : TitleFormatter {
                override fun format(day: CalendarDay): CharSequence {
                    // CalendarDay라는 클래스는 LocalDate 클래스를 기반으로 만들어진 클래스다
                    // 때문에 MaterialCalendarView에서 연/월 보여주기를 커스텀하려면 CalendarDay 객체의 getDate()로 연/월을 구한 다음 LocalDate 객체에 넣어서
                    // LocalDate로 변환하는 처리가 필요하다
                    val inputText: org.threeten.bp.LocalDate = day.date
                    val calendarHeaderElements = inputText.toString().split("-")
                    val calendarHeaderBuilder = StringBuilder()
                    calendarHeaderBuilder.append(calendarHeaderElements[0])
                        .append(" ")
                        .append(calendarHeaderElements[1])
                    return calendarHeaderBuilder.toString()
                }
            })
        closeBtn.setOnClickListener {
            var intent = Intent(this, calendar_nextActivity::class.java)
            intent.putExtra("startDay", "$startDay")
            intent.putExtra("endDay","${endDay}")
            startActivity(intent)
        }
        }

        /* 선택된 요일의 background를 설정하는 Decorator 클래스 */
        class DayDecorator(context: Context) : DayViewDecorator {

            private val drawable: Drawable =
                ContextCompat.getDrawable(context, R.drawable.calendar_selector)!!

            // true를 리턴 시 모든 요일에 내가 설정한 드로어블이 적용된다
            override fun shouldDecorate(day: CalendarDay): Boolean {
                return true
            }

            // 일자 선택 시 내가 정의한 드로어블이 적용되도록 한다
            override fun decorate(view: DayViewFacade) {
                view.setSelectionDrawable(drawable)
                view.addSpan(StyleSpan(Typeface.BOLD))   // 달력 안의 모든 숫자들이 볼드 처리됨
            }
        }
    }
