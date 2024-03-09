package com.example.secondcapstone

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class plan_adpater(val itemList: ArrayList<plan_items>):
    RecyclerView.Adapter<plan_adpater.plan_Viewholder>() { //어댑터 상속

    inner class plan_Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val day = itemView.findViewById<TextView>(R.id.day)
        val date = itemView.findViewById<TextView>(R.id.date)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): plan_adpater.plan_Viewholder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.plan_list_layout, parent, false)
        return plan_Viewholder(view)
    }

    override fun onBindViewHolder(holder: plan_Viewholder, position: Int) {
        //return itemList.count() //itemList는 class의 매개변수로 줌
        holder.day.text = itemList[position].day
        holder.date.text = itemList[position].date

    }

    override fun getItemCount(): Int {
        return itemList.count()

    }

}