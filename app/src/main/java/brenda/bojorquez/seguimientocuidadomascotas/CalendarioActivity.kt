package brenda.bojorquez.seguimientocuidadomascotas

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

class CalendarioActivity : AppCompatActivity() {
    var botonesMenuInfo=ArrayList<VacunasMuestra>()
    var adapter: VeterinariasAdaptador? =null
    lateinit var mascota: Mascota

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendario)
        val btn_back: ImageView = findViewById(R.id.back) as ImageView
        val calendarView: CalendarView = findViewById(R.id.calendarView) as CalendarView

        val bundle = intent.extras
        if(bundle != null){

            var imagenS: String = bundle.getString("uri").toString()
            val imagenUri = Uri.parse(imagenS)
            mascota = Mascota(bundle.getString("nombre").toString(), bundle.getInt("image"), imagenUri, bundle.getString("edad").toString() )        }

        cargarBotones()
        adapter = VeterinariasAdaptador(this, botonesMenuInfo)

        var gridBotones: GridView = findViewById(R.id.gridGaleria)

        gridBotones.adapter = adapter

        btn_back.setOnClickListener {
            var intento = Intent(this, InfogenActivity::class.java)
            intento.putExtra("nombre",  mascota.nombre)
            intento.putExtra("image",  mascota.image)
            intento.putExtra("edad", mascota.edad)
            intento.putExtra("uri", mascota.imageUri.toString())
            this.startActivity(intento)
            finish()
        }


    }
    fun cargarBotones(){
        botonesMenuInfo.add(VacunasMuestra("Timón", R.drawable.timon,"Consulta de control\n25 de marzo, 3:40pm", mascota))
        botonesMenuInfo.add(VacunasMuestra("Odin", R.drawable.odin,"Consulta de control\n31 de marzo, 5:40pm", mascota))
    }

    class VeterinariasAdaptador: BaseAdapter {
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
            var vista = inflador.inflate(R.layout.cell_veterinarias, null)

            val imagen = vista.findViewById(R.id.icono) as ImageView
            val nombre = vista.findViewById(R.id.item) as TextView
            val direccion = vista.findViewById(R.id.item2) as TextView

            nombre.setText(boton.name)
            imagen.setImageResource(boton.image)
            direccion.setText(boton.fecha)

            return vista
        }
    }
}