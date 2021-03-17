package com.example.a2do

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add.*
import java.lang.Exception

class addActivity : AppCompatActivity() {
    var id = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setIcon(R.mipmap.ic_logo)
        try{
            val bundle: Bundle? = intent.extras
            id = bundle!!.getInt("ID",0)

            if(id!=0){
                editTextTitulo.setText(bundle.getString("Titulo"))
                editTextDescripcion.setText(bundle.getString("Descripcion"))


            }
        }catch(e: Exception){}

    }
    fun btnAdd(view: View){
        val baseDatos = DBManager(this)

        val values = ContentValues()
        values.put("Titulo",editTextTitulo.text.toString())
        values.put("Descripcion",editTextDescripcion.text.toString())




        if(id == 0){
            val ID = baseDatos.insert(values)
            if(ID>0) {
                Toast.makeText(this, "El registro se ingreso con exito", Toast.LENGTH_LONG).show()
                finish()
            }else{
                Toast.makeText(this,"El registro no se realizo!", Toast.LENGTH_LONG).show()
            }
        }else{
            val selectionArgs = arrayOf(id.toString())

            val ID = baseDatos.actualizar(values,"ID=?",selectionArgs)

            if(ID>0){
                Toast.makeText(this,"El registro se realizo con exito", Toast.LENGTH_LONG).show()
                finish()
            }else{
                Toast.makeText(this,"El registro no se realizo con exito", Toast.LENGTH_LONG).show()
            }
        }
    }
}