package com.example.a2do

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.BaseAdapter
import android.widget.SearchView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.molde_notas.view.*

class MainActivity (var adapter:NotasAdapter?=null): AppCompatActivity() {
    var listadeNotas = ArrayList<Notas>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setIcon(R.mipmap.ic_logo)
        cargarQuery("%")
    }
    override fun onResume() {


        super.onResume()
        cargarQuery("%")
    }



    fun cargarQuery (titulo:String){



        var baseDatos = DBManager(this)
        val columnas = arrayOf("ID","Titulo", "Descripcion")
        val selectionArgs = arrayOf(titulo)

        var cursor = baseDatos.query(columnas,"Titulo like ?",selectionArgs,"Titulo")

        listadeNotas.clear()

        if (cursor.moveToFirst()){
            do{
                val ID = cursor.getInt(cursor.getColumnIndex("ID"))
                val titulo = cursor.getString(cursor.getColumnIndex("Titulo"))
                val descripcion = cursor.getString(cursor.getColumnIndex("Descripcion"))


                listadeNotas.add(Notas(ID, titulo, descripcion))
            }while (cursor.moveToNext())

        }

        adapter = NotasAdapter(this,listadeNotas)

        listview.adapter=adapter
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu,menu)
        val buscar = menu!!.findItem(R.id.app_bar_search).actionView as SearchView
        val manejador = getSystemService(Context.SEARCH_SERVICE)as SearchManager

        buscar.setSearchableInfo(manejador.getSearchableInfo(componentName))
        buscar.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                Toast.makeText(applicationContext,query, Toast.LENGTH_LONG).show()
                cargarQuery("%"+query+"%")
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item!!.itemId){
            R.id.menuAgregar->{
                var intent=Intent(this,addActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    inner class NotasAdapter(contexto:Context,var listadeNotas: ArrayList<Notas>):BaseAdapter() {
        var contexto:Context?=contexto
        override fun getCount(): Int {
            return listadeNotas.size
        }

        override fun getItem(position: Int): Any {
            return listadeNotas[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val nota= listadeNotas[position]

            val inflater=contexto!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE)as LayoutInflater
            val miVista=inflater.inflate(R.layout.molde_notas,null)
            miVista.textViwTitulo.text=nota.titulo
            miVista.textViewDescripcion.text=nota.descripcion



            miVista.imageViewDelete.setOnClickListener{
                val dbManager = DBManager(this.contexto!!)
                val selectionArgs = arrayOf(nota.notasID.toString())

                dbManager.borrar("ID=?",selectionArgs)

                cargarQuery("%")
            }


            miVista.imageEdit.setOnClickListener{
                val intent = Intent(this@MainActivity,addActivity::class.java)
                intent.putExtra("ID",nota.notasID)
                intent.putExtra("Titulo",nota.titulo)
                intent.putExtra("Descripcion",nota.descripcion)

                startActivity(intent)

            }
            return miVista


        }
    }
}