package brenda.bojorquez.seguimientocuidadomascotas.frecres

import brenda.bojorquez.seguimientocuidadomascotas.*
import brenda.bojorquez.seguimientocuidadomascotas.adapters.ManualAdaptador
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.*
import kotlin.collections.ArrayList

class MedicionMRespActivity : AppCompatActivity() {
    private lateinit var storage: FirebaseFirestore
    private lateinit var usuario: FirebaseAuth
    private lateinit var correo: String
    var datoManual=ArrayList<MedicionDatos>()
    var adapter: ManualAdaptador? =null
    lateinit var mascota: Mascota

    companion object{
        var first = true
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medicion_mresp)

        storage = FirebaseFirestore.getInstance()
        usuario = FirebaseAuth.getInstance()
        correo = usuario.currentUser?.email.toString()

        val btn_comoMedir: Button = findViewById(R.id.btn_comoMedir) as Button
        val btn_añadir: Button = findViewById(R.id.btn_añadir) as Button
        val btn_back: ImageView = findViewById(R.id.back) as ImageView
        val nombreM: TextView = findViewById(R.id.nombreMas)

        val bundle = intent.extras
        if(bundle != null){
            nombreM.setText(bundle.getString("nombre").toString())
            var imagenS: String = bundle.getString("uri").toString()
            val imagenUri = Uri.parse(imagenS)
            mascota = Mascota(bundle.getString("nombre").toString(), bundle.getInt("image"), imagenUri, bundle.getString("edad").toString() )
        }


        if(first){
            cargarDatos()
            first = false
        }

        btn_back.setOnClickListener {
            var intento = Intent(this, RespiradActivity::class.java)
            intento.putExtra("nombre",  mascota.nombre)
            intento.putExtra("image",  mascota.image)
            intento.putExtra("uri", mascota.imageUri.toString())
            intento.putExtra("edad", mascota.edad)
            this.startActivity(intento)
            finish()
        }

        btn_comoMedir.setOnClickListener {
            var intento = Intent(this, ComoRespActivity::class.java)
            intento.putExtra("nombre",  mascota.nombre)
            intento.putExtra("image",  mascota.image)
            intento.putExtra("edad", mascota.edad)
            intento.putExtra("uri", mascota.imageUri.toString())

            this.startActivity(intento)
        }

        btn_añadir.setOnClickListener {
            val builder = AlertDialog.Builder(this@MedicionMRespActivity)
            val view = layoutInflater.inflate(R.layout.dato_resp_alert, null)


            builder.setView(view)
            val dialog = builder.create()
            dialog.show()

            val btn_cancelar: Button = view.findViewById(R.id.btn_cancelar) as Button
            val btn_agregar: Button = view.findViewById(R.id.btn_agregar) as Button

            val numberPickerU: NumberPicker = view.findViewById(R.id.numberPickerU) as NumberPicker
            val numberPickerD: NumberPicker = view.findViewById(R.id.numberPickerD) as NumberPicker

            val item: TextView = view.findViewById(R.id.item) as TextView

            numberPickerU.maxValue = 9
            numberPickerU.minValue = 0

            // Establecer los valores mínimos y máximos
            numberPickerD.minValue = 10
            numberPickerD.maxValue = 30

            item.setText(String.format("%s xmin", numberPickerU.value))

            numberPickerU.setOnValueChangedListener { _, _, newVal ->
                val sum = numberPickerD.value + numberPickerU.value
                item.text = String.format("%s xmin", sum)
            }
            numberPickerD.setOnValueChangedListener { _, _, newVal ->
                val sum = numberPickerD.value + numberPickerU.value
                item.text = String.format("%s xmin", sum)
            }

            btn_cancelar.setOnClickListener {
                dialog.dismiss()
            }
            btn_agregar.setOnClickListener {
                if(!item.text.equals("0 xmin")){
                    val horaActual = LocalTime.now()
                    val hora = horaActual.hour
                    var minutos:String = horaActual.minute.toString()
                    if(horaActual.minute<10){
                        minutos = "0"+horaActual.minute
                    }

                    val currentDate = SimpleDateFormat("d 'de' MMMM 'de' yyyy", Locale.getDefault()).format(Date())

                    val actividad = hashMapOf(
                            "fecha" to currentDate,
                            "hora" to convertirHora24("$hora:$minutos"),
                            "mascota" to nombreM.text,
                            "xmin" to item.text.toString(),
                            "email" to usuario.currentUser?.email.toString()
                        )

                    storage.collection("FrecRespiratorias")
                            .add(actividad)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Frecuencia Respiratoria agregada", Toast.LENGTH_SHORT).show()
                                cargarDatos()
                                dialog.dismiss()
                            }.addOnFailureListener {
                                Toast.makeText(this, "Fallo al guardar" + it.toString(), Toast.LENGTH_SHORT).show()
                            }
                    }else{
                    Toast.makeText(this, "Ingresar datos", Toast.LENGTH_SHORT).show()
                }

            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun cargarDatos(){
        var gridBotones: GridView = findViewById(R.id.gridCardiaca)
        datoManual = ArrayList()
        storage.collection("FrecRespiratorias")
            .whereEqualTo("email", correo)
            .whereEqualTo("mascota", mascota.nombre)
            .get()
            .addOnSuccessListener {
                documents ->
                if(documents.size()>0) {
                    var fechas: ArrayList<String> = ArrayList()
                    var dato: ArrayList<String> = ArrayList()
                    for (document in documents) {
                        var fechaActual: String = document.getString("fecha").toString()
                        if (fechas.size > 0) {
                            if (fechaExiste(fechas, fechaActual)) {

                            } else {
                                fechas?.add(fechaActual)
                            }
                        } else {
                            fechas?.add(fechaActual)
                        }
                    }

                    if (fechas != null) {
                        // Ordenar la lista de fechas utilizando el comparador personalizado
                        var fechasOrdenadas = fechas.sortedWith(Comparator { fecha1, fecha2 ->
                            compararFechas(fecha1, fecha2)
                        })
                        fechasOrdenadas = ordenarFechas(fechasOrdenadas)
                        for (fecha in fechasOrdenadas) {
                            dato = ArrayList()
                            for (document in documents) {

                                if (fecha.equals(document.getString("fecha"))) {
                                    dato.add(
                                        document.getString("xmin") .toString() + " - " + document.getString("hora") .toString())
                                }
                            }

                            datoManual.add(MedicionDatos(fecha, dato))

                        }
                    }
                    adapter = ManualAdaptador(this, datoManual)
                    gridBotones.adapter = adapter
                }
            }.addOnFailureListener{
                Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
            }
    }

    fun fechaExiste(lista:ArrayList<String>, fecha:String): Boolean{
        for(fechaL in lista){
            if(fechaL.equals(fecha)){
                return true
            }
        }
        return false
    }
    fun convertirHora24(hora: String): String {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        val dateObj = sdf.parse(hora)
        return SimpleDateFormat("hh:mm a", Locale.getDefault()).format(dateObj)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun compararFechas(fecha1: String, fecha2: String): Int {
        val format = SimpleDateFormat("dd 'de' MMMM 'de' yyyy", Locale.getDefault())
        val date1 = format.parse(fecha1)
        val date2 = format.parse(fecha2)

        println("FECHA1: $date1")
        println("FECHA2: $date2")

        return date1.compareTo(date2)

    }

    fun ordenarFechas(fechas: List<String>): List<String> {
        val format = SimpleDateFormat("dd 'de' MMMM 'de' yyyy", Locale.getDefault())
        return fechas.sortedByDescending { format.parse(it) }
    }
}