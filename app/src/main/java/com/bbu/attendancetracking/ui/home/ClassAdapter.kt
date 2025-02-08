package com.bbu.attendancetracking.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bbu.attendancetracking.R
import com.bbu.attendancetracking.model.ClassItem

class ClassAdapter(private val classList: List<ClassItem>) :
    RecyclerView.Adapter<ClassAdapter.ClassViewHolder>() {

    class ClassViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.title)
        private val dateTextView: TextView = itemView.findViewById(R.id.date)
        private val labTextView: TextView = itemView.findViewById(R.id.lab)

        fun bind(classItem: ClassItem) {
            titleTextView.text = classItem.title
            dateTextView.text = classItem.date
            labTextView.text = classItem.labNo
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.class_item, parent, false)

//        val imageView = view.findViewById<ImageView>(R.id.scanQRIcon)




        return ClassViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClassViewHolder, position: Int) {
        holder.bind(classList[position])
    }

    override fun getItemCount(): Int = classList.size
}
