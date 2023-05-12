package brenda.bojorquez.seguimientocuidadomascotas.adapters

import brenda.bojorquez.seguimientocuidadomascotas.R
import brenda.bojorquez.seguimientocuidadomascotas.VacunasMuestra
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class VacunasAdaptador: BaseAdapter {
    var botones = ArrayList<VacunasMuestra>()
    var contexto: Context? = null

    constructor(contexto: Context, productos:ArrayList<VacunasMuestra>){
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
        var vista = inflador.inflate(R.layout.cell_perfil, null)

        val imagen = vista.findViewById(R.id.icono) as ImageView
        val nombre = vista.findViewById(R.id.item) as TextView
        val fecha  = vista.findViewById(R.id.item2) as TextView

        imagen.setImageResource(boton.image)
        nombre.setText(boton.name)
        fecha.setText(boton.fecha)


        return vista
    }
}