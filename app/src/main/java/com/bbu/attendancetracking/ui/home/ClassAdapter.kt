package com.bbu.attendancetracking.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bbu.attendancetracking.R
import com.bbu.attendancetracking.data.model.ClassItem
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView

class ClassAdapter(private val classList: List<ClassItem>) :
    RecyclerView.Adapter<ClassAdapter.ClassViewHolder>() {

    class ClassViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.title)
        private val dateTextView: TextView = itemView.findViewById(R.id.date)
        private val labTextView: TextView = itemView.findViewById(R.id.lab)

        fun bind(classItem: ClassItem) {
            titleTextView.text = classItem.title
            dateTextView.text = classItem.date
            labTextView.text = classItem.lab
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.class_item, parent, false)

        val imageView = view.findViewById<ImageView>(R.id.scanQRIcon)

        imageView.setOnClickListener {
            // Your code to handle the click event
//            val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
            print("cliccked")

//            val bottomNavigationView = view.findViewById<BottomNavigationView>(R.id.bb)


//            bottomNavigationView.selectedItemId = R.id.scanQRIcon

//            Toast.makeText(parent.context, "Your message here", Toast.LENGTH_SHORT).show()




            // Set the selected tab programmatically by item ID
//            var bottomNavigationView = parent.findViewById<BottomNavigationView>(R.id.nav_view)
//
//            // Set the selected item programmatically
//
//            if (bottomNavigationView.isActivated) {
//                bottomNavigationView.selectedItemId = R.id.navigation_dashboard
//            }
        }


        return ClassViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClassViewHolder, position: Int) {
        holder.bind(classList[position])
    }

    override fun getItemCount(): Int = classList.size
}
