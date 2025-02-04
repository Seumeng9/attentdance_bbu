package com.bbu.attendancetracking.ui.attendance.report

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bbu.attendancetracking.R
import com.bbu.attendancetracking.model.ClassItem
import com.bbu.attendancetracking.ui.attendance.AttendanceFragment
import com.bbu.attendancetracking.ui.attendance.AttendancePagerAdapter
import com.bbu.attendancetracking.ui.attendance.calendar.CalendarViewModel

class ClassReportAttendanceAdapter(
    private var classList: List<ClassItem>,
    private  var calendarViewModel: CalendarViewModel,
    private val viewPager: ViewPager2
) :
    RecyclerView.Adapter<ClassReportAttendanceAdapter.ClassViewHolder>() {

     inner class ClassViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.title)
         private val desTextView: TextView = itemView.findViewById(R.id.desc)
        private val dateTextView: TextView = itemView.findViewById(R.id.date)
        private val labTextView: TextView = itemView.findViewById(R.id.lab)

         val adapter = (viewPager.adapter as AttendancePagerAdapter)

         init {
             itemView.setOnClickListener{
                 val bundle = Bundle().apply {
                     putString("selectedDate", classList[adapterPosition].scheduled) // Example data
                     putInt("classId", classList[adapterPosition].classId)
                     putString("classTitle", classList[adapterPosition].title)
                 }

                 calendarViewModel.classId = classList[adapterPosition].classId

                 adapter.calendarArgs = bundle // Set arguments for CalendarFragment

                 viewPager.setCurrentItem(1, true)
             }
         }

         fun bind(classItem: ClassItem) {
            titleTextView.text = classItem.title
            desTextView.text = classItem.description
            dateTextView.text = classItem.scheduled
            labTextView.text = classItem.labNo
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.class_item, parent, false)

//        val imageView = view.findViewById<ImageView>(R.id.scanQRIcon)





        return ClassViewHolder(view)
    }

    fun updateData(newList: List<ClassItem>) {
        this.classList = newList
        notifyDataSetChanged()  // Notify RecyclerView to refresh
    }

    override fun onBindViewHolder(holder: ClassViewHolder, position: Int) {
        holder.bind(classList[position])
    }

    override fun getItemCount(): Int = classList.size
}
