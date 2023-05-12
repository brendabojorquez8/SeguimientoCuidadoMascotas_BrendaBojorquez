package brenda.bojorquez.seguimientocuidadomascotas

import android.net.Uri

data class Mascota (var nombre:String,
                    var image:Int,
                    var imageUri: Uri,
                    var edad:String)