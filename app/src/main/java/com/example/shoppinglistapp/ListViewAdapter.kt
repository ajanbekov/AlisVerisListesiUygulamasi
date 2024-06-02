package com.example.shoppinglistapp

import android.widget.ArrayAdapter
import android.view.View
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

class ListViewAdapter(
    context: Context,
    private val items: ArrayList<String>,
    private val mainActivity: MainActivity,
    private val dbHelper: ShoppingListDbHelper
) :
    ArrayAdapter<String>(context, R.layout.list_row, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_row, parent, false)

        val number = view.findViewById<TextView>(R.id.number)
        number.text = "${position + 1}."

        val name = view.findViewById<TextView>(R.id.name)
        name.text = items[position]

        val duplicate = view.findViewById<ImageView>(R.id.copy)
        val remove = view.findViewById<ImageView>(R.id.remove)

        duplicate.setOnClickListener {
            addItem(items[position])
        }

        remove.setOnClickListener {
            removeItem(position)
        }

        return view
    }

    private fun removeItem(position: Int) {
        val item = items[position]
        items.removeAt(position)
        notifyDataSetChanged()
        dbHelper.deleteItem(item)
        mainActivity.makeToast("$item Listeden Kaldırıldı.")
    }

    private fun addItem(item: String) {
        items.add(item)
        notifyDataSetChanged()
        dbHelper.insertItem(item)
    }
}

