package brenda.bojorquez.seguimientocuidadomascotas.adapters

import brenda.bojorquez.seguimientocuidadomascotas.MedicionDatos
import brenda.bojorquez.seguimientocuidadomascotas.R
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.TextView

class ManualAdaptador : BaseAdapter {
    var botones = ArrayList<MedicionDatos>()
    var contexto: Context? = null

    constructor(contexto: Context, productos: ArrayList<MedicionDatos>) {
        this.botones = productos
        this.contexto = contexto
    }

    override fun getCount(): Int {
        return botones.size
    }

    override fun getItem(p0: Int): Any {
        return botones[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var boton = botones[p0]
        var inflador = LayoutInflater.from(contexto)
        var vista = inflador.inflate(R.layout.cell_freccar, null)

        val fecha = vista.findViewById(R.id.fecha) as TextView
        val gridDatos: GridView = vista.findViewById(R.id.gridDato)
        var adapter = DatosMAdaptador(vista.context, boton.listaDatos)

        gridDatos.adapter = adapter

        fecha.setText(boton.fecha)

        return vista
    }
}