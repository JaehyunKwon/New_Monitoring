package com.fmk.monitoring.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.fmk.monitoring.R
import com.fmk.monitoring.adapter.WeeklyAdapter
import com.fmk.monitoring.data.Day
import com.fmk.monitoring.data.Week
import com.fmk.monitoring.databinding.ActivityMainBinding
import com.fmk.monitoring.databinding.ViewWeekCalendarBinding
import com.fmk.monitoring.util.DebugUtil
import java.text.SimpleDateFormat
import java.util.*

class WeeklyActivity() : AppCompatActivity() {
    // lateinit var
    private lateinit var binding: ViewWeekCalendarBinding
    private lateinit var mainAdapter: WeeklyAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager

    private lateinit var mStrWeek: String

    private var mYear: Int = 0
    private var mMonth: Int = 0
    private var mDay: Int = 0
    private var mDayNum: Int = 0
    private var mWeek: Int = 0
    private var mCurPos: Int = 0
    private var mStartDayNum: Int = 0

    private lateinit var weeks: Vector<Week>

    private var mEnd = 0

    // init
    private var mCalendar = GregorianCalendar(Locale.KOREA)
    private var dayFormat = SimpleDateFormat("dd", Locale.KOREA)
    private var monthFormat = SimpleDateFormat("MM", Locale.KOREA)
    private var yearFormat = SimpleDateFormat("yyyy", Locale.KOREA)
    var cdf = SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ViewWeekCalendarBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val date = Date()
        setDays(date)
    }

    private fun setDays(date: Date) {
        val today = cdf.format(date)
        val startDate = "2018-12-10"
        mEnd = today.replace("-", "").toInt()
        initCalendar(startDate, today)
    }

    private fun calcDateBetweenEndAndStart(end: String, start: String): Int {
        val endDate = cdf.parse(end)
        val startDate = cdf.parse(start)
        val ymd = (start.split("-"))
        val mCal = GregorianCalendar()
        mCal.set(ymd[0].toInt(), ymd[1].toInt() + 1, ymd[2].toInt())
        mStartDayNum = mCal.get(Calendar.DAY_OF_WEEK)
        val diff = endDate.time - startDate.time
        val diffDays = diff / (24 * 60 * 60 * 1000)
        DebugUtil.printError("날짜차이", diffDays.toString())
        return diffDays.toInt() + 1
    }

    private fun initCalendar(startDate: String, today: String) {
        DebugUtil.printError("startDate", startDate)
        DebugUtil.printError("today", today)
        val diff = calcDateBetweenEndAndStart(today, startDate)
        linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.daysRecyclerView.layoutManager = linearLayoutManager
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding.daysRecyclerView)
        mYear = mCalendar.get(Calendar.YEAR)
        mMonth = mCalendar.get(Calendar.MONTH) + 1
        mDay = mCalendar.get(Calendar.DAY_OF_MONTH)
        mWeek = mCalendar.get(Calendar.WEEK_OF_MONTH)
        mDayNum = mCalendar.get(Calendar.DAY_OF_WEEK)
        mStrWeek = getStrWeek(mWeek)
        mCalendar.set(mYear, mMonth, mDay)

        val thisWeek = "${mYear}년 ${mMonth}월 $mStrWeek"
        binding.ymwTxtView.text = thisWeek
        val days = Vector<Day>()
        val calendar = Calendar.getInstance()
        for (i in -((mStartDayNum) + diff)..(7 - mDayNum)) {
            calendar.set(mYear, mMonth - 1, mDay + i)
            val date1 = calendar.time
            days.add(
                Day(
                    yearFormat.format(date1),
                    monthFormat.format(date1),
                    dayFormat.format(date1)
                )
            )
        }
        weeks = Vector()
        for (i in 0 until (days.size / 7)) {
            weeks.add(
                Week(
                    days[(7 * i) + 1],
                    days[(7 * i) + 2],
                    days[(7 * i) + 3],
                    days[(7 * i) + 4],
                    days[(7 * i) + 5],
                    days[(7 * i) + 6],
                    days[(7 * i) + 7]
                )
            )
        }
        mCurPos = weeks.size - 1
        mainAdapter = WeeklyAdapter(this, weeks, today, startDate)
        binding.daysRecyclerView.adapter = mainAdapter
        binding.daysRecyclerView.scrollToPosition(mCurPos)
        mainAdapter.notifyDataSetChanged()
        binding.daysRecyclerView.addOnScrollListener(scrollListener)

        val dayss = Vector<Day>()
        dayss.add(weeks[mCurPos].day1)
        dayss.add(weeks[mCurPos].day2)
        dayss.add(weeks[mCurPos].day3)
        dayss.add(weeks[mCurPos].day4)
        dayss.add(weeks[mCurPos].day5)
        dayss.add(weeks[mCurPos].day6)
        dayss.add(weeks[mCurPos].day7)
    }

    private fun getStrWeek(intWeek: Int): String {
        val strWeek: String
        when (intWeek) {
            1 -> {
                strWeek = "첫째주"
            }
            2 -> {
                strWeek = "둘째주"
            }
            3 -> {
                strWeek = "셋째주"
            }
            4 -> {
                strWeek = "넷째주"
            }
            5 -> {
                strWeek = "다섯째주"
            }
            else -> {
                strWeek = ""
            }
        }
        return strWeek
    }

    private var scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            DebugUtil.printError("newState ", "$newState")
            if (newState == 0) {
                mCurPos = linearLayoutManager.findFirstVisibleItemPosition()
                DebugUtil.printError("mCurPos", "$mCurPos")
                val curWeek = mainAdapter.todays(mCurPos, 0)
                val week = curWeek.split("-")
                mCalendar.set(
                    week[0].toInt(),
                    week[1].toInt(),
                    week[2].toInt()
                )
                mWeek = mCalendar.get(Calendar.WEEK_OF_MONTH)
                mStrWeek = getStrWeek(mWeek)

                val thisWeek = "${week[0].toInt()}년 ${week[1].toInt()}월 $mStrWeek"
                binding.ymwTxtView.text = thisWeek

                val dayss = Vector<Day>()
                dayss.add(weeks[mCurPos].day1)
                dayss.add(weeks[mCurPos].day2)
                dayss.add(weeks[mCurPos].day3)
                dayss.add(weeks[mCurPos].day4)
                dayss.add(weeks[mCurPos].day5)
                dayss.add(weeks[mCurPos].day6)
                dayss.add(weeks[mCurPos].day7)
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
        }
    }

}