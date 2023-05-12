package brenda.bojorquez.seguimientocuidadomascotas

import brenda.bojorquez.seguimientocuidadomascotas.account.LoginActivity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DuenoperfilActivity : AppCompatActivity() {
    private lateinit var storage: FirebaseFirestore
    private lateinit var usuario: FirebaseAuth
    private lateinit var correo: String
    var adapter: AdaptadorMascotas? =null

    companion object{
        var mascotasPerfilD = ArrayList<Mascota>()
        var first = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_duenoperfil)

        storage = FirebaseFirestore.getInstance()
        usuario = FirebaseAuth.getInstance()
        correo = usuario.currentUser?.email.toString()

        val nombreD:TextView = findViewById(R.id.nombreD)
        val telefonoD:TextView = findViewById(R.id.telefonoD)

        if(usuario.currentUser?.displayName?.length==null){
            storage.collection("usuarios")
                .whereEqualTo("email", correo)
                .get()
                .addOnSuccessListener {
                    it.forEach{
                        nombreD.setText(it.getString("nombre"))
                        telefonoD.setText(it.getString("telefono"))


                    }
                }.addOnFailureListener{
                    Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
                }
        }else{
            nombreD.setText(usuario.currentUser?.displayName)
            telefonoD.setText(usuario.currentUser?.phoneNumber)
        }

        if(first){
            cargarBotones()
            first = false
        }


        val btn_logout: ImageView = findViewById(R.id.logout) as ImageView

        btn_logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()

            Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        adapter = AdaptadorMascotas(this, mascotasPerfilD)

        var gridPelis: GridView = findViewById(R.id.mascotas)

        gridPelis.adapter = adapter
    }
    private fun logout() {
        LoginActivity.mGoogleSignInClient.signOut()
    }
    fun cargarBotones(){
        storage.collection("Mascotas")
            .whereEqualTo("email", correo)
            .get()
            .addOnSuccessListener {
                mascotasPerfilD.remove(Mascota(" ", R.drawable.nueva, Uri.EMPTY, "New Pet"))
                it.forEach{
                        var  image:Int = 0
                        if(it.getString("especie").equals("Felino")){
                            image = R.drawable.cat
                        }
                        if(it.getString("especie").equals("Canino")){
                            image = R.drawable.dog
                        }
                        if(it.getString("especie").equals("Ave")){
                            image = R.drawable.bird
                        }
                        if(it.getString("especie").equals("Pez")){
                            image = R.drawable.pez
                        }
                        if(it.getString("especie").equals("Reptil")){
                            image = R.drawable.lizard
                        }
                        if(it.getString("especie").equals("Insecto")){
                            image = R.drawable.insecto
                        }
                    var imagenS: String = it.getString("imagen").toString()
                    val imagenUri = Uri.parse(imagenS)
                    var nombre:String = it.getString("nombreMascota").toString()
                    var edadString = it.getString("edad").toString()


                    var act = Mascota(nombre, image, imagenUri, edadString)

                    mascotasPerfilD.add(act)

                }
                mascotasPerfilD.add(Mascota(" ", R.drawable.nueva, Uri.EMPTY,"New Pet"))
                adapter = AdaptadorMascotas(this, mascotasPerfilD)
                var gridPelis: GridView = findViewById(R.id.mascotas)

                gridPelis.adapter = adapter
            }.addOnFailureListener{
                Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
            }

        mascotasPerfilD.add(Mascota("Timon", R.drawable.timon, Uri.EMPTY,"9 años"))
        mascotasPerfilD.add(Mascota("Odin", R.drawable.odin, Uri.EMPTY,"6 años"))
        mascotasPerfilD.add(Mascota("Silver", R.drawable.silver, Uri.EMPTY,"2 años"))
        mascotasPerfilD.add(Mascota(" ", R.drawable.nueva, Uri.EMPTY,"New Pet"))

    }

    class AdaptadorMascotas: BaseAdapter {
        var botones = ArrayList<Mascota>()
        var contexto: Context?=null

        constructor(contexto: Context, productos:ArrayList<Mascota>){
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
            var mascota=botones[p0]
            var inflador= LayoutInflater.from(contexto)
            var vista = inflador.inflate(R.layout.cell_mascota, null)

            var imagen = vista.findViewById(R.id.my_image_view) as de.hdodenhof.circleimageview.CircleImageView
            var nombre = vista.findViewById(R.id.mascota_nombre_cell) as TextView
            var edad = vista.findViewById(R.id.mascota_edad_cell) as TextView

            if (mascota.imageUri.toString() != "") {
                Glide.with(contexto!!)
                    .load(mascota.imageUri)
                    .into(imagen)
            } else {
                imagen.setImageResource(mascota.image)
            }
            nombre.setText(mascota.nombre)
            edad.setText(mascota.edad)

            imagen.setOnClickListener {

                if(mascota.edad.equals("New Pet")){
                    var intento2 = Intent(contexto, NuevapetActivity::class.java)
                    contexto!!.startActivity(intento2)

                }else{
                    var intento = Intent(contexto, MascotasperfilActivity::class.java)
                    intento.putExtra("nombre", mascota.nombre)
                    intento.putExtra("image", mascota.image)
                    intento.putExtra("edad", mascota.edad)
                    intento.putExtra("uri", mascota.imageUri.toString())
                    contexto!!.startActivity(intento)
                }
            }

            return vista
        }
    }
}