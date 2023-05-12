package brenda.bojorquez.seguimientocuidadomascotas.adapters

import brenda.bojorquez.seguimientocuidadomascotas.*
import brenda.bojorquez.seguimientocuidadomascotas.freccard.CardiacadActivity
import brenda.bojorquez.seguimientocuidadomascotas.frecres.RespiradActivity
import brenda.bojorquez.seguimientocuidadomascotas.historial.EnfermedadesActivity
import brenda.bojorquez.seguimientocuidadomascotas.historial.PadecimientosActivity
import brenda.bojorquez.seguimientocuidadomascotas.historial.VacunasActivity
import brenda.bojorquez.seguimientocuidadomascotas.GaleriaActivity
import brenda.bojorquez.seguimientocuidadomascotas.GastosActivity
import brenda.bojorquez.seguimientocuidadomascotas.InfogenActivity
import brenda.bojorquez.seguimientocuidadomascotas.DuenoperfilActivity
import brenda.bojorquez.seguimientocuidadomascotas.PesodActivity
import brenda.bojorquez.seguimientocuidadomascotas.temp.TemperaturadActivity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

class BotonesAdaptador: BaseAdapter {
    var botones = ArrayList<BotonesMenu>()
    var contexto: Context? = null

    constructor(contexto: Context, productos:ArrayList<BotonesMenu>){
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
        val shape  = vista.findViewById(R.id.shape) as LinearLayout

        imagen.setImageResource(boton.image)
        nombre.setText(boton.name)

        shape.setOnClickListener{
            if(boton.name.equals("Perfil dueño")){
                var intento = Intent(contexto, DuenoperfilActivity::class.java)
                contexto!!.startActivity(intento)
            }

            if(boton.name.equals("Signos vitales")){
                var intento = Intent(contexto, SignosvActivity::class.java)
                intento.putExtra("nombre",  boton.mascota.nombre)
                intento.putExtra("image",  boton.mascota.image)
                intento.putExtra("edad", boton.mascota.edad)
                intento.putExtra("uri", boton.mascota.imageUri.toString())
                contexto!!.startActivity(intento)
            }
            if(boton.name.equals("Comportamiento")){
                var intento = Intent(contexto, ComportActivity::class.java)
                intento.putExtra("nombre",  boton.mascota.nombre)
                intento.putExtra("image",  boton.mascota.image)
                intento.putExtra("edad", boton.mascota.edad)
                intento.putExtra("uri", boton.mascota.imageUri.toString())
                contexto!!.startActivity(intento)
            }
            if(boton.name.equals("Historial Clinico")){
                var intento = Intent(contexto, HistorialcActivity::class.java)
                intento.putExtra("nombre",  boton.mascota.nombre)
                intento.putExtra("image",  boton.mascota.image)
                intento.putExtra("edad", boton.mascota.edad)
                intento.putExtra("uri", boton.mascota.imageUri.toString())
                contexto!!.startActivity(intento)
            }
            if(boton.name.equals("Información General")){
                var intento = Intent(contexto, InfogenActivity::class.java)
                intento.putExtra("nombre",  boton.mascota.nombre)
                intento.putExtra("image",  boton.mascota.image)
                intento.putExtra("edad", boton.mascota.edad)
                intento.putExtra("uri", boton.mascota.imageUri.toString())
                contexto!!.startActivity(intento)
            }

            if(boton.name.equals("Frecuencia cardiaca")){
                var intentoC = Intent(contexto, CardiacadActivity::class.java)
                intentoC.putExtra("nombre",  boton.mascota.nombre)
                intentoC.putExtra("image",  boton.mascota.image)
                intentoC.putExtra("edad", boton.mascota.edad)
                intentoC.putExtra("uri", boton.mascota.imageUri.toString())
                contexto!!.startActivity(intentoC)
            }
            if(boton.name.equals("Frecuencia respiratoria")){
                var intento = Intent(contexto, RespiradActivity::class.java)
                intento.putExtra("nombre",  boton.mascota.nombre)
                intento.putExtra("image",  boton.mascota.image)
                intento.putExtra("edad", boton.mascota.edad)
                intento.putExtra("uri", boton.mascota.imageUri.toString())
                contexto!!.startActivity(intento)
            }
            if(boton.name.equals("Temperatura")){
                var intento = Intent(contexto, TemperaturadActivity::class.java)
                intento.putExtra("nombre",  boton.mascota.nombre)
                intento.putExtra("image",  boton.mascota.image)
                intento.putExtra("edad", boton.mascota.edad)
                intento.putExtra("uri", boton.mascota.imageUri.toString())
                contexto!!.startActivity(intento)
            }
            if(boton.name.equals("Peso")){
                var intento = Intent(contexto, PesodActivity::class.java)
                intento.putExtra("nombre",  boton.mascota.nombre)
                intento.putExtra("image",  boton.mascota.image)
                intento.putExtra("edad", boton.mascota.edad)
                intento.putExtra("uri", boton.mascota.imageUri.toString())
                contexto!!.startActivity(intento)
            }

            if(boton.name.equals("Vacunación")){
                var intentoC = Intent(contexto, VacunasActivity::class.java)
                intentoC.putExtra("nombre",  boton.mascota.nombre)
                intentoC.putExtra("image",  boton.mascota.image)
                intentoC.putExtra("edad", boton.mascota.edad)
                intentoC.putExtra("uri", boton.mascota.imageUri.toString())
                contexto!!.startActivity(intentoC)
            }
            if(boton.name.equals("Padecimientos")){
                var intento = Intent(contexto, PadecimientosActivity::class.java)
                intento.putExtra("nombre",  boton.mascota.nombre)
                intento.putExtra("image",  boton.mascota.image)
                intento.putExtra("edad", boton.mascota.edad)
                intento.putExtra("uri", boton.mascota.imageUri.toString())
                contexto!!.startActivity(intento)
            }
            if(boton.name.equals("Enfermedades")){
                var intento = Intent(contexto, EnfermedadesActivity::class.java)
                intento.putExtra("nombre",  boton.mascota.nombre)
                intento.putExtra("image",  boton.mascota.image)
                intento.putExtra("edad", boton.mascota.edad)
                intento.putExtra("uri", boton.mascota.imageUri.toString())
                contexto!!.startActivity(intento)
            }

            if(boton.name.equals("Directorio veterinarios")){
                var intento = Intent(contexto, VeterinariaActivity::class.java)
                intento.putExtra("nombre",  boton.mascota.nombre)
                intento.putExtra("image",  boton.mascota.image)
                intento.putExtra("edad", boton.mascota.edad)
                intento.putExtra("uri", boton.mascota.imageUri.toString())
                contexto!!.startActivity(intento)
            }

            if(boton.name.equals("Artículos")){
                var intento = Intent(contexto, ArticulosActivity::class.java)
                intento.putExtra("nombre",  boton.mascota.nombre)
                intento.putExtra("image",  boton.mascota.image)
                intento.putExtra("edad", boton.mascota.edad)
                intento.putExtra("uri", boton.mascota.imageUri.toString())
                contexto!!.startActivity(intento)
            }

            if(boton.name.equals("Galeria")){
                var intento = Intent(contexto, GaleriaActivity::class.java)
                intento.putExtra("nombre",  boton.mascota.nombre)
                intento.putExtra("image",  boton.mascota.image)
                intento.putExtra("edad", boton.mascota.edad)
                intento.putExtra("uri", boton.mascota.toString())
                contexto!!.startActivity(intento)
            }

            if(boton.name.equals("Gastos")){
                var intento = Intent(contexto, GastosActivity::class.java)
                intento.putExtra("nombre",  boton.mascota.nombre)
                intento.putExtra("image",  boton.mascota.image)
                intento.putExtra("edad", boton.mascota.edad)
                intento.putExtra("uri", boton.mascota.toString())
                contexto!!.startActivity(intento)
            }

            if(boton.name.equals("Calendario")){
                var intento = Intent(contexto, CalendarioActivity::class.java)
                intento.putExtra("nombre",  boton.mascota.nombre)
                intento.putExtra("image",  boton.mascota.image)
                intento.putExtra("edad", boton.mascota.edad)
                intento.putExtra("uri", boton.mascota.toString())
                contexto!!.startActivity(intento)
            }
        }
        return vista
    }
}