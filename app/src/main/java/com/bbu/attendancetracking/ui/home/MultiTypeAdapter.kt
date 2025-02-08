package com.bbu.attendancetracking.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bbu.attendancetracking.R
import com.bbu.attendancetracking.model.RecyclerViewItem

class MultiTypeAdapter(private var items: List<RecyclerViewItem>,

                       private val itemClickListener: (index: Int) -> Unit // Lambda function for item clicks

    ) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_CLASS_ITEM = 1
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is RecyclerViewItem.Header -> TYPE_HEADER
            is RecyclerViewItem.ClassItem -> TYPE_CLASS_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HEADER -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_header, parent, false)
                HeaderViewHolder(view)
            }
            TYPE_CLASS_ITEM -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.class_item, parent, false)
                ClassItemViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is RecyclerViewItem.Header -> (holder as HeaderViewHolder).bind(item)
            is RecyclerViewItem.ClassItem -> (holder as ClassItemViewHolder).bind(item)
        }
    }

    override fun getItemCount(): Int = items.size

    // Method to update the items
    fun updateItems(newItems: List<RecyclerViewItem>) {
        items = newItems
        notifyDataSetChanged()
    }


    // ViewHolder for header
    inner class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val headerTitle: TextView = itemView.findViewById(R.id.headerTitle)

        fun bind(header: RecyclerViewItem.Header) {
            headerTitle.text = header.title
        }
    }

    // ViewHolder for class items
    inner class ClassItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.title)
        private val des: TextView = itemView.findViewById(R.id.desc)
        private val lab: TextView = itemView.findViewById(R.id.lab)
        private val date: TextView = itemView.findViewById(R.id.date)


        init {
            // Set click listener for the entire item (root view of the item)
            itemView.setOnClickListener {
                itemClickListener(adapterPosition) // Invoke the lambda with the clicked item
            }
        }

        fun bind(classItem: RecyclerViewItem.ClassItem) {
            title.text = classItem.title
            des.text = classItem.desc
            lab.text = classItem.labNo
            date.text = classItem.scheduled
        }
    }
}
