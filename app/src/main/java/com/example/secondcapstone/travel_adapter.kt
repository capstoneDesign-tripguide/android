package com.example.secondcapstone

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class travel_adapter(val itemList: ArrayList<travel_items>, private val listener: OnItemClickListener):
        RecyclerView.Adapter<travel_adapter.travel_Viewholder>() { //어댑터 상속
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
    inner class travel_Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById<TextView>(R.id.item_name)
        val rating = itemView.findViewById<TextView>(R.id.item_rating)
        val addButton = itemView.findViewById<Button>(R.id.add)
        init {
            addButton.setOnClickListener {
                val position = adapterPosition
                Log.d("position","$position")
                listener.onItemClick(position)
            }
        }
    }

    //뷰가 생성될 때 호출되는 메소드
    //레이아웃을 인플레이트해서 리턴한다.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): travel_Viewholder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_travel_item_layout, parent, false)
        return travel_Viewholder(view)
    }

    //리스트의 수를 리턴
    override fun getItemCount(): Int {
        return itemList.count() //itemList는 class의 매개변수로 줌
    }

    //뷰가 바인드될 때 호출되는 메소드
    override fun onBindViewHolder(holder: travel_Viewholder, position: Int) {
        holder.name.text = itemList[position].name
        holder.rating.text = itemList[position].rating

    }
}