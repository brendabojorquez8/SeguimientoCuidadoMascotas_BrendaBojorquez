package brenda.bojorquez.seguimientocuidadomascotas.temp

import brenda.bojorquez.seguimientocuidadomascotas.*
import brenda.bojorquez.seguimientocuidadomascotas.adapters.ManualAdaptador
import brenda.bojorquez.seguimientocuidadomascotas.freccard.ComoCardActivity
import brenda.bojorquez.seguimientocuidadomascotas.frecres.MedicionMRespActivity
import brenda.bojorquez.seguimientocuidadomascotas.frecres.RespiradActivity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.*

class MedicionMTempActivity : AppCompatActivity() {
    private lateinit var storage: FirebaseFirestore
    private lateinit var usuario: FirebaseAuth
    private lateinit var correo: String
    var datoManual= ArrayList<MedicionDatos>()
    var adapter: ManualAdaptador? =null
    lateinit var mascota: Mascota

    companion object{
        var first = true
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medicion_mtemp)

        storage = FirebaseFirestore.getInstance()
        usuario = FirebaseAuth.getInstance()
        correo = usuario.currentUser?.email.toString()

        val btn_comoMedir: Button = findViewById(R.id.btn_comoMedir) as Button
        val btn_añadir: Button = findViewById(R.id.btn_añadir) as Button
        val btn_back: ImageView = findViewById(R.id.back) as ImageView
        val bundle = intent.extras
        if(bundle != null){

            val nombreM: TextView = findViewById(R.id.nombreMas)
            nombreM.setText(bundle.getString("nombre").toString())

            var imagenS: String = bundle.getString("uri").toString()
            val imagenUri = Uri.parse(imagenS)
            mascota = Mascota(bundle.getString("nombre").toString(), bundle.getInt("image"), imagenUri, bundle.getString("edad").toString() )        }

        if(first){
            cargarDatos()
            first = false
        }

        btn_back.setOnClickListener {
            var intento = Intent(this, TemperaturadActivity::class.java)
            intento.putExtra("nombre",  mascota.nombre)
            intento.putExtra("image",  mascota.image)
            intento.putExtra("edad", mascota.edad)
            intento.putExtra("uri", mascota.imageUri.toString())
            this.startActivity(intento)
            finish()
        }

        btn_comoMedir.setOnClickListener {
            var intento = Intent(this, ComoTempActivity::class.java)
            intento.putExtra("nombre",  mascota.nombre)
            intento.putExtra("image",  mascota.image)
            intento.putExtra("edad", mascota.edad)
            intento.putExtra("uri", mascota.imageUri.toString())

            this.startActivity(intento)
            finish()
        }

        btn_añadir.setOnClickListener {
            val builder = AlertDialog.Builder(this@MedicionMTempActivity)
            val view = layoutInflater.inflate(R.layout.dato_temp_alert, null)

            builder.setView(view)

            val dialog = builder.create()
            dialog.show()

            val btn_cancelar: Button = view.findViewById(R.id.btn_cancelar) as Button
            val btn_agregar: Button = view.findViewById(R.id.btn_agregar) as Button

            val numberPickerU: NumberPicker = view.findViewById(R.id.numberPickerU) as NumberPicker
            val numberPickerD: NumberPicker = view.findViewById(R.id.numberPickerD) as NumberPicker

            val item: TextView = view.findViewById(R.id.item) as TextView

            numberPickerD.maxValue = 40
            numberPickerD.minValue = 0

            // Establecer los valores mínimos y máximos
            numberPickerU.minValue = 0
            numberPickerU.maxValue = 10

            // Crear un arreglo de String con los valores de 10 en 10
            val displayedValues = arrayOfNulls<String>(11)
            val step = BigDecimal("0.1")
            var value = BigDecimal("0.0")

            for (i in 0..10) {
                displayedValues[i] = value.toString()
                value += step
            }

            // Establecer los valores a mostrar en el NumberPicker
            numberPickerU.displayedValues = displayedValues

            item.setText(String.format("%s C", numberPickerU.value))

            numberPickerU.setOnValueChangedListener { _, _, newVal ->
                val df = DecimalFormat("#0.0")
                val sum = numberPickerD.value + numberPickerU.value* 0.1
                item.text = String.format("%s C", df.format(sum))
            }
            numberPickerD.setOnValueChangedListener { _, _, newVal ->
                val df = DecimalFormat("#0.0")
                val sum = numberPickerD.value + numberPickerU.value* 0.1
                item.text = String.format("%s C", df.format(sum))
            }

            btn_cancelar.setOnClickListener {
                dialog.dismiss()
            }
            btn_agregar.setOnClickListener {
                if(!item.text.equals("0 C")){
                    val horaActual = LocalTime.now()
                    val hora = horaActual.hour
                    var minutosT:String = horaActual.minute.toString()
                    if(horaActual.minute<10){
                        minutosT = "0"+horaActual.minute
                    }

                    val currentDate = SimpleDateFormat("d 'de' MMMM 'de' yyyy", Locale.getDefault()).format(Date())

                    val actividad = hashMapOf(
                        "fecha" to currentDate,
                        "hora" to convertirHora24("$hora:$minutosT"),
                        "mascota" to mascota.nombre,
                        "centigrados" to item.text.toString(),
                        "email" to usuario.currentUser?.email.toString()
                    )

                    storage.collection("Temperaturas")
                        .add(actividad)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Temperatura agregada", Toast.LENGTH_SHORT).show()
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
        storage.collection("Temperaturas")
            .whereEqualTo("email", correo)
            .whereEqualTo("mascota", mascota.nombre)
            .get()
            .addOnSuccessListener {
                    documents ->
                if(documents.size()>0) {

                    var fechas: ArrayList<String> = ArrayList()
                    var dato: ArrayList<String> = java.util.ArrayList()
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
                    fechas.sortDescending()

                    if (fechas != null) {
                        // Ordenar la lista de fechas utilizando el comparador personalizado
                        var fechasOrdenadas = fechas.sortedWith(Comparator { fecha1, fecha2 ->
                            compararFechas(fecha1, fecha2)
                        })
                        fechasOrdenadas = ordenarFechas(fechasOrdenadas)
                        for (fecha in fechasOrdenadas) {
                            dato = java.util.ArrayList()
                            for (document in documents) {

                                if (fecha.equals(document.getString("fecha"))) {
                                    dato.add(
                                        document.getString("centigrados") .toString() + " - " + document.getString("hora") .toString())
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

    fun fechaExiste(lista: java.util.ArrayList<String>, fecha:String): Boolean{
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