package brenda.bojorquez.seguimientocuidadomascotas

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.time.LocalDate
import java.time.Period
import java.util.*

class AgregarmascotaActivity : AppCompatActivity() {
    private lateinit var storage: FirebaseFirestore
    private lateinit var usuario: FirebaseAuth
    private lateinit var nombre_mas: String
    private val REQUEST_CODE = 1
    private lateinit var especie: String
    private var selectedImageUri: Uri = Uri.parse("")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregarmascota)

        val btn_continuar:Button = findViewById(R.id.btn_continuar)  as Button
        val btn_back: ImageView = findViewById(R.id.back) as ImageView
        val imagen: ImageView = findViewById(R.id.imageM) as ImageView

        val editText = findViewById<EditText>(R.id.et_fechana_mas)
        val editText2 = findViewById<EditText>(R.id.et_contacto_mas)
        editText.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(10))
        editText2.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(10))

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
            especie = bundle.getString("especie").toString()
            imagen.setImageResource(bundle.getInt("image"))
        }

        btn_back.setOnClickListener {
            val intent = Intent(this, NuevapetActivity::class.java)
            startActivity(intent)
            finish()
        }

        storage = FirebaseFirestore.getInstance()
        usuario = FirebaseAuth.getInstance()

        imagen.setOnClickListener {
            if(!usuario.currentUser?.email.isNullOrBlank()){
                fileUpload()
            }
        }

        btn_continuar.setOnClickListener {
            val et_nombre_mas: EditText = findViewById(R.id.nombre_mas)
            val et_fechana: EditText = findViewById(R.id.et_fechana_mas)
            val et_raza_mas: EditText = findViewById(R.id.et_raza_mas)
            val et_tamaño_mas: EditText = findViewById(R.id.et_tamaño_mas)
            val et_alergia_mas: EditText = findViewById(R.id.et_alergia_mas)
            val et_senas_mas: EditText = findViewById(R.id.et_señas_mas)

            val et_historial_mas: EditText = findViewById(R.id.et_historial_mas)
            val et_veterinario_mas: EditText = findViewById(R.id.et_veterinario_mas)
            val et_contacto_mas: EditText = findViewById(R.id.et_contacto_mas)

            nombre_mas = et_nombre_mas.text.toString()
            val fechana: String = et_fechana.text.toString()
            val raza_mas: String = et_raza_mas.text.toString()
            val tamaño_mas: String = et_tamaño_mas.text.toString()
            val alergia_mas: String = et_alergia_mas.text.toString()
            val senas_mas: String = et_senas_mas.text.toString()

            var historial_mas: String = et_historial_mas.text.toString()
            var veterinario_mas: String = et_veterinario_mas.text.toString()
            var contacto_mas: String = et_contacto_mas.text.toString()

            if(!nombre_mas.isNullOrBlank() && !fechana.isNullOrBlank() && !raza_mas.isNullOrBlank() && !tamaño_mas.isNullOrBlank() && !alergia_mas.isNullOrBlank() &&
                !senas_mas.isNullOrBlank() && !historial_mas.isNullOrBlank() && !veterinario_mas.isNullOrBlank() && !contacto_mas.isNullOrBlank()){
                    if (selectedImageUri != Uri.parse("")) {
                        uploadImage(selectedImageUri!!, raza_mas, tamaño_mas, alergia_mas,
                            senas_mas, historial_mas, fechana, veterinario_mas,
                            contacto_mas)
                    }else{
                        Toast.makeText(this, "Agrega una imagen clickeando el icono de la especie", Toast.LENGTH_SHORT).show()
                    }
            }else{
                Toast.makeText(this, "Ingresar datos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun fileUpload(){
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"

        startActivityForResult(intent, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            val fileUri = data?.data
            selectedImageUri = fileUri!!
            val imagen: ImageView = findViewById(R.id.imageM) as ImageView
            imagen.setImageURI(selectedImageUri)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun uploadImage(imageUri: Uri, raza_mas: String, tamaño_mas: String, alergia_mas: String,
                            senas_mas: String, historial_mas: String, fechana: String, veterinario_mas: String,
                            contacto_mas: String) {
        val folder: StorageReference = FirebaseStorage.getInstance().reference.child(usuario.currentUser?.email.toString())
        val fileName: StorageReference = folder.child(nombre_mas + " - " + imageUri.lastPathSegment)
        fileName.putFile(imageUri)
            .addOnSuccessListener { taskSnapshot ->
                fileName.downloadUrl.addOnSuccessListener { uri ->
                    // Guardar la URL de la imagen en Firestore junto con los demás datos de la mascota
                    val actividad = hashMapOf(
                        "nombreMascota" to nombre_mas,
                        "raza" to raza_mas,
                        "tamaño" to tamaño_mas,
                        "alergia" to alergia_mas,
                        "señas" to senas_mas,
                        "historial" to historial_mas,
                        "fecha nacimiento" to fechana,
                        "edad" to calcularEdad(fechana),
                        "veterinario" to veterinario_mas,
                        "contatcto" to contacto_mas,
                        "imagen" to uri.toString(),
                        "email" to usuario.currentUser?.email.toString(),
                        "especie" to especie
                    )
                    storage.collection("Mascotas")
                        .add(actividad)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Mascota agregada", Toast.LENGTH_SHORT).show()
                            val bundle = intent.extras
                            if (bundle != null) {
                                DuenoperfilActivity.mascotasPerfilD.remove(Mascota(" ", R.drawable.nueva,Uri.EMPTY, "New Pet"))
                                var act = Mascota(nombre_mas, 0, uri, calcularEdad(fechana))
                                DuenoperfilActivity.mascotasPerfilD.add(act)
                                DuenoperfilActivity.mascotasPerfilD.add(Mascota(" ", R.drawable.nueva,Uri.EMPTY, "New Pet"))
                                val intent = Intent(this, DuenoperfilActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                        }.addOnFailureListener {
                            Toast.makeText(
                                this,
                                "Fallo al guardar" + it.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
            }
            .addOnFailureListener { /*...*/ }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun calcularEdad(fechaNacimiento: String): String {
        val partesFecha = fechaNacimiento.split("/")
        val dia = partesFecha[0].toInt()
        val mes = partesFecha[1].toInt()
        val anio = partesFecha[2].toInt()
        val fechaNac = LocalDate.of(anio, mes, dia)
        val edad = Period.between(fechaNac, LocalDate.now()).years
        return "$edad años"
    }
}