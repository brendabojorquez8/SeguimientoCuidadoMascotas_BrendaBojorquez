package brenda.bojorquez.seguimientocuidadomascotas

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AgregagastActivity : AppCompatActivity() {
    private lateinit var storage: FirebaseFirestore
    private lateinit var usuario: FirebaseAuth
    private lateinit var correo: String
    lateinit var mascota: Mascota

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregagast)

        val btn_back: ImageView = findViewById(R.id.back) as ImageView
        val btn_continuar: Button = findViewById(R.id.btn_continuar) as Button
        val nombreMas: TextView = findViewById(R.id.nombreMas) as TextView

        storage = FirebaseFirestore.getInstance()
        usuario = FirebaseAuth.getInstance()
        correo = usuario.currentUser?.email.toString()

        val editText = findViewById<EditText>(R.id.et_fechaGasto)
        editText.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(10))

        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No es necesario implementar este método
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Este método se llama cada vez que se cambia el texto del EditText
                val text = s.toString()

                if (text.length == 2 && before == 0) {
                    // Si el usuario acaba de escribir el día, agregamos el primer '/'
                    editText.setText("$text/")
                    editText.setSelection(editText.text.length)
                } else if (text.length == 5 && before == 0) {
                    // Si el usuario acaba de escribir el mes, agregamos el segundo '/'
                    editText.setText("$text/")
                    editText.setSelection(editText.text.length)
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // No es necesario implementar este método
            }
        })


        val bundle = intent.extras
        if(bundle != null){
            nombreMas.setText(bundle.getString("nombre").toString())
            var imagenS: String = bundle.getString("uri").toString()
            val imagenUri = Uri.parse(imagenS)
            mascota = Mascota(bundle.getString("nombre").toString(), bundle.getInt("image"), imagenUri, bundle.getString("edad").toString() )        }

        btn_continuar.setOnClickListener {

            val et_nombre: EditText = findViewById(R.id.et_nombreGasto)
            val et_fecha: EditText = findViewById(R.id.et_fechaGasto)
            val et_tipo: EditText = findViewById(R.id.et_tipo)
            val et_costo: EditText = findViewById(R.id.et_costo)
            val et_descripcion: EditText = findViewById(R.id.et_descripcion)

            val nombre: String = et_nombre.text.toString()
            val fecha: String = et_fecha.text.toString()
            val tipo: String = et_tipo.text.toString()
            val costo: String = et_costo.text.toString()
            val descripcion: String = et_descripcion.text.toString()

            if(!nombre.isNullOrBlank() && !fecha.isNullOrBlank() && !tipo.isNullOrBlank() && !costo.isNullOrBlank() && !descripcion.isNullOrBlank()){
                val actividad = hashMapOf(
                    "nombre" to nombre,
                    "fecha" to fecha,
                    "tipo" to tipo,
                    "costo" to costo,
                    "descripcion" to descripcion,
                    "email" to usuario.currentUser?.email.toString()
                )

                storage.collection("Gastos")
                    .add(actividad)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Gasto agregado", Toast.LENGTH_SHORT).show()

                        var intento = Intent(this, GastosActivity::class.java)
                        intento.putExtra("nombre",  mascota.nombre)
                        intento.putExtra("image",  mascota.image)
                        intento.putExtra("edad", mascota.edad)
                        intento.putExtra("uri", mascota.imageUri.toString())
                        this.startActivity(intento)
                        finish()
                    }.addOnFailureListener {
                        Toast.makeText(this, "Fallo al guardar" + it.toString(), Toast.LENGTH_SHORT).show()
                    }
            }else{
                Toast.makeText(this, "Ingresar datos", Toast.LENGTH_SHORT).show()
            }
        }

        btn_back.setOnClickListener {
            var intento = Intent(this, GastosActivity::class.java)
            intento.putExtra("nombre",  mascota.nombre)
            intento.putExtra("image",  mascota.image)
            intento.putExtra("edad", mascota.edad)
            intento.putExtra("uri", mascota.imageUri.toString())
            this.startActivity(intento)
            finish()
        }
    }
}