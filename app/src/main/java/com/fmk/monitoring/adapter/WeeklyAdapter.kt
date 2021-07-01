package com.fmk.monitoring.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fmk.monitoring.data.Day
import com.fmk.monitoring.data.Week
import com.fmk.monitoring.databinding.ItemDaysBinding
import java.util.*

class WeeklyAdapter() :
    RecyclerView.Adapter<WeeklyAdapter.WeeklyViewHolder>() {

    constructor(activity: Activity, weeks: Vector<Week>, today: String, startDay: String) : this() {
        this.activity = activity
        this.weeks = weeks
        this.today = today
        this.startDay = startDay
        this.iToday = today.replace("-", "").toInt()
        this.iStartDay = startDay.replace("-", "").toInt()
        this.context = activity.applicationContext
    }

    private lateinit var activity: Activity
    private lateinit var weeks: Vector<Week>
    private lateinit var context: Context
    private lateinit var today: String
    private lateinit var startDay: String
    private lateinit var txtViews: Array<TextView>
    private var iToday: Int = 0
    private var iStartDay: Int = 0

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): WeeklyViewHolder {
        val binding = ItemDaysBinding.inflate(LayoutInflater.from(context), viewGroup, false)
        return WeeklyViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: WeeklyViewHolder, position: Int) {
        val binding = viewHolder.binding
        val week = weeks[position]

        txtViews = arrayOf (
            binding.sunTxtView,
            binding.monTxtView,
            binding.tueTxtView,
            binding.wedTxtView,
            binding.thuTxtView,
            binding.friTxtView,
            binding.satTxtView
        )

        binding.sunTxtView.text = week.day1.day
        binding.monTxtView.text = week.day2.day
        binding.tueTxtView.text = week.day3.day
        binding.wedTxtView.text = week.day4.day
        binding.thuTxtView.text = week.day5.day
        binding.friTxtView.text = week.day6.day
        binding.satTxtView.text = week.day7.day

        //오늘 날짜를 크기를 크게
        for (i in 0 until txtViews.size) {
            val today = todays(position, i)
            if (today == this.today) {
                txtViews[i].scaleX = 1.75f
                txtViews [i].scaleY = 1.75f
            } else {
                txtViews[i].scaleX = 1.0f
                txtViews [i].scaleY = 1.0f
            }
        }
    }

    override fun getItemCount(): Int {
        return weeks.size
    }

    class WeeklyViewHolder : RecyclerView.ViewHolder {
        var binding: ItemDaysBinding
        constructor(binding: ItemDaysBinding) : super(binding.root) {
            this.binding = binding
        }

    }

    fun todays(pos1: Int, pos2: Int): String {
        val week = weeks[pos1]
        var day: Day? = null
        when (pos2) {
            0 -> {
                day = week.day1
            }
            1 -> {
                day = week.day2
            }
            2 -> {
                day = week.day3
            }
            3 -> {
                day = week.day4
            }
            4 -> {
                day = week.day5
            }
            5 -> {
                day = week.day6
            }
            6 -> {
                day = week.day7
            }
        }
        return "${day!!.year}-${day.month}-${day.day}"
    }

}