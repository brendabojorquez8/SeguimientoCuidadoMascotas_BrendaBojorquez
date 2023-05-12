package brenda.bojorquez.seguimientocuidadomascotas.adapters

import brenda.bojorquez.seguimientocuidadomascotas.Datos
import brenda.bojorquez.seguimientocuidadomascotas.R
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class DatosAdaptador: BaseAdapter {
    var botones = ArrayList<Datos>()
    var contexto: Context? = null

    constructor(contexto: Context, productos:ArrayList<Datos>){
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
        var boton=botones[p0]
        var inflador= LayoutInflater.from(contexto)
        var vista = inflador.inflate(R.layout.cell_dato, null)


        val imagen = vista.findViewById(R.id.icono) as TextView
        val nombre = vista.findViewById(R.id.item) as TextView
        nombre.setText(boton.name)
        imagen.setText(boton.image)

        return vista
    }
}