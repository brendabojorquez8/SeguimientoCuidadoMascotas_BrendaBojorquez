package brenda.bojorquez.seguimientocuidadomascotas.adapters

import brenda.bojorquez.seguimientocuidadomascotas.R
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class DatosMAdaptador : BaseAdapter {
    var botones = ArrayList<String>()
    var contexto: Context? = null

    constructor(contexto: Context, productos: ArrayList<String>) {
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
        var vista = inflador.inflate(R.layout.cell_dato_signos, null)

        val frec = vista.findViewById(R.id.frec) as TextView
        frec.setText(boton.toString())
        return vista
    }
}