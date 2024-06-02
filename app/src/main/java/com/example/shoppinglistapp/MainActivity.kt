package com.example.shoppinglistapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import android.widget.Toast
import org.json.JSONArray
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import android.widget.*

class MainActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var items: ArrayList<String>
    private lateinit var adapter: ArrayAdapter<String>
    private var toast: Toast? = null
    private lateinit var input: EditText
    private lateinit var enter: ImageView
    private lateinit var dbHelper: ShoppingListDbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById(R.id.listview)
        input = findViewById(R.id.input)
        enter = findViewById(R.id.add)

        dbHelper = ShoppingListDbHelper(this)
        items = dbHelper.getAllItems()

        adapter = ListViewAdapter(this, items, this, dbHelper)
        listView.adapter = adapter

        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, i, _ ->
            val name = items[i]
            makeToast(name)
        }

        listView.setOnItemLongClickListener { _, _, i, _ ->

            val item = items[i]
            makeToast("$item Listeden Kaldırıldı.")
            removeItem(i)
            true
        }

        enter.setOnClickListener {
            val text = input.text.toString()
            if (text.isEmpty()) {
                makeToast("Bir Öğe Giriniz..")
            } else {
                addItem(text)
                input.setText("")
                makeToast("$text Listeye Eklendi.")
                dbHelper.insertItem(text)
            }
        }
    }

    private fun removeItem(position: Int) {
        val item = items[position]
        items.removeAt(position)
        adapter.notifyDataSetChanged()
        dbHelper.deleteItem(item)
        makeToast("$item Listeden Kaldırıldı.")
    }

    private fun addItem(item: String) {
        items.add(item)
        adapter.notifyDataSetChanged()
    }

    fun makeToast(s: String){
        toast?.cancel()
        toast = Toast.makeText(applicationContext, s, Toast.LENGTH_SHORT)
        toast?.show()
    }
}

