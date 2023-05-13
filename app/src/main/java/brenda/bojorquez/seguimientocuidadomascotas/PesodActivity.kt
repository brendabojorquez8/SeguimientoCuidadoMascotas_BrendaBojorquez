package brenda.bojorquez.seguimientocuidadomascotas

import brenda.bojorquez.seguimientocuidadomascotas.adapters.ManualAdaptador
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

class PesodActivity : AppCompatActivity() {
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
        setContentView(R.layout.activity_pesod)

        storage = FirebaseFirestore.getInstance()
        usuario = FirebaseAuth.getInstance()
        correo = usuario.currentUser?.email.toString()


        val btn_back: ImageView = findViewById(R.id.back) as ImageView
        val bundle = intent.extras


        // Crea un GradientDrawable para el fondo de la SeekBar
        val color1 = Color.parseColor("#D0E9E9") // rojo
        val color2 = Color.parseColor("#A0CD68") // amarillo
        val color3 = Color.parseColor("#F7E2BB") // verde
        val color4 = Color.parseColor("#F2AAAE") // azul

        val colors = intArrayOf(color1, color2, color3, color4)
        val positions = floatArrayOf(0.0f, 0.33f, 0.66f, 1.0f)

        val gradient = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors)

        gradient.gradientType = GradientDrawable.LINEAR_GRADIENT
        gradient.orientation = GradientDrawable.Orientation.LEFT_RIGHT
        gradient.cornerRadii = floatArrayOf(16f, 16f, 16f, 16f, 16f, 16f, 16f, 16f)
        gradient.gradientRadius = 360f

        val gradientDrawable = LayerDrawable(arrayOf(gradient))
        val seekBar = findViewById<SeekBar>(R.id.seekBar)
        seekBar.progressDrawable = gradientDrawable
        seekBar.isEnabled = false

        if(bundle != null){

            val nombreM: TextView = findViewById(R.id.nombreMas)
            nombreM.setText(bundle.getString("nombre").toString())

            var imagenS: String = bundle.getString("uri").toString()
            val imagenUri = Uri.parse(imagenS)
            mascota = Mascota(bundle.getString("nombre").toString(), bundle.getInt("image"), imagenUri, bundle.getString("edad").toString() )        }

        btn_back.setOnClickListener {
            var intento = Intent(this, SignosvActivity::class.java)
            intento.putExtra("nombre",  mascota.nombre)
            intento.putExtra("image",  mascota.image)
            intento.putExtra("edad", mascota.edad)
            intento.putExtra("uri", mascota.imageUri.toString())
            this.startActivity(intento)
        }
        val btn_añadir: Button = findViewById(R.id.btn_añadir) as Button
        btn_añadir.setOnClickListener{
            datosAgregadoHoy()
        }


    }

    fun cargarDatos(){
        val currentDate = SimpleDateFormat("d 'de' MMMM 'de' yyyy", Locale.getDefault()).format(Date())
        datoManual = ArrayList()
        storage.collection("Pesos")
            .whereEqualTo("email", correo)
            .whereEqualTo("mascota", mascota.nombre)
            .whereEqualTo("fecha", currentDate)
            .get()
            .addOnSuccessListener {
                    documents ->
                if(documents.size()>0) {
                    val pesoActual:TextView = findViewById(R.id.pesoActual)
                    for (document in documents){
                        pesoActual.text = document.getString("kg")
                    }
                }
            }.addOnFailureListener{
                Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun datosAgregadoHoy(){
        // Obtener la fecha actual
        val currentDate = SimpleDateFormat("d 'de' MMMM 'de' yyyy", Locale.getDefault()).format(Date())
        var fechaIgual:Boolean = false
        storage.collection("Pesos")
            .whereEqualTo("email", correo)
            .whereEqualTo("mascota", mascota.nombre)
            .whereEqualTo("fecha", currentDate)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if(documentSnapshot.size()>0){
                    Toast.makeText(this, "Ya ha registrado el peso hoy", Toast.LENGTH_SHORT).show()
                }else{
                    dialog()
                }

            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun dialog(){

            val builder = AlertDialog.Builder(this@PesodActivity)
            val view = layoutInflater.inflate(R.layout.dato_peso_alert, null)
            builder.setView(view)
            val dialog = builder.create()
            dialog.show()

            val btn_cancelar: Button = view.findViewById(R.id.btn_cancelar) as Button
            val btn_agregar: Button = view.findViewById(R.id.btn_agregar) as Button
            val numberPickerU: NumberPicker = view.findViewById(R.id.numberPickerU) as NumberPicker
            val numberPickerD: NumberPicker = view.findViewById(R.id.numberPickerD) as NumberPicker
            val item: TextView = view.findViewById(R.id.item) as TextView
            numberPickerD.maxValue = 100
            numberPickerD.minValue = 0

            // Establecer los valores mínimos y máximos
            numberPickerU.minValue = 0
            numberPickerU.maxValue = 100

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

            item.setText(String.format("%s Kg", numberPickerU.value))
            numberPickerU.setOnValueChangedListener { _, _, newVal ->
                val df = DecimalFormat("#0.0")
                val sum = numberPickerD.value + numberPickerU.value* 0.1
                item.text = String.format("%s Kg", df.format(sum))
            }
            numberPickerD.setOnValueChangedListener { _, _, newVal ->
                val df = DecimalFormat("#0.0")
                val sum = numberPickerD.value + numberPickerU.value* 0.1
                item.text = String.format("%s Kg", df.format(sum))
            }

            btn_cancelar.setOnClickListener {
                dialog.dismiss()
            }
            btn_agregar.setOnClickListener {

                if(!item.text.equals("0 Kg")){

                    val horaActual = LocalTime.now()
                    val hora = horaActual.hour
                    var minutos: String = horaActual.minute.toString()
                    if (horaActual.minute < 10) {
                        minutos = "0" + horaActual.minute
                    }

                    val currentDate =
                        SimpleDateFormat("d 'de' MMMM 'de' yyyy", Locale.getDefault()).format(
                            Date()
                        )

                    val actividad = hashMapOf(
                        "fecha" to currentDate,
                        "hora" to convertirHora24("$hora:$minutos"),
                        "mascota" to mascota.nombre,
                        "kg" to item.text.toString(),
                        "email" to usuario.currentUser?.email.toString()
                    )

                    storage.collection("Pesos")
                        .add(actividad)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Peso agregada", Toast.LENGTH_SHORT).show()
                            //cargarDatos()
                            dialog.dismiss()
                        }.addOnFailureListener {
                            Toast.makeText(
                                this,
                                "Fallo al guardar" + it.toString(),
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                }else{
                    Toast.makeText(this, "Ingresar datos", Toast.LENGTH_SHORT).show()
                }


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
}