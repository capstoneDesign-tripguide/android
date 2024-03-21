package com.example.secondcapstone

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class plan_adpater(val itemList: ArrayList<plan_items>, private val listener: OnItemClickListener):

    RecyclerView.Adapter<plan_adpater.plan_Viewholder>() { //어댑터 상속

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
    inner class plan_Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val day = itemView.findViewById<TextView>(R.id.day)
        val date = itemView.findViewById<TextView>(R.id.date)
        val addBtn = itemView.findViewById<Button>(R.id.add)

        init { // 클릭 이벤트를 init때 생성해야 함 이유는 모르겠다..
            addBtn.setOnClickListener { //선택 완료 버튼 클릭 이벤트 처리
                val clickedPosition = adapterPosition
                listener.onItemClick(clickedPosition)
            }

        }


    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): plan_adpater.plan_Viewholder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.plan_list_layout, parent, false)
        val button = view.findViewById<Button>(R.id.add)


        return plan_Viewholder(view)
    }

    override fun onBindViewHolder(holder: plan_Viewholder, position: Int) {
        //return itemList.count() //itemList는 class의 매개변수로 줌
        holder.day.text = itemList[position].day
        holder.date.text = itemList[position].date
        // TextView의 ID 동적 할당



    }

    override fun getItemCount(): Int {
        return itemList.count()

    }


}