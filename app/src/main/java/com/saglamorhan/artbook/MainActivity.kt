package com.saglamorhan.artbook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val artNameList = ArrayList<String>()
        val artIdList = ArrayList<Int>()

        val arrayAdapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,artNameList)
        listView.adapter = arrayAdapter

        try {

            val database = this.openOrCreateDatabase("Arts", MODE_PRIVATE,null)

            val cursor = database.rawQuery("SELECT * FROM arts",null)
            val artNameIx = cursor.getColumnIndex("artname")
            val idIx =  cursor.getColumnIndex("id")

            while (cursor.moveToNext()){
                artNameList.add(cursor.getString(artNameIx))
                artIdList.add(cursor.getInt(idIx))

            }
            //listView i guncelle
            arrayAdapter.notifyDataSetChanged()
            cursor.close()


        }catch (e: Exception){
            e.printStackTrace()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        //Inflater
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.add_art,menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {


        if (item.itemId == R.id.add_art_item){

            val intent = Intent(this,MainActivity2::class.java)
            startActivity(intent)

        }
        return super.onOptionsItemSelected(item)
    }
}