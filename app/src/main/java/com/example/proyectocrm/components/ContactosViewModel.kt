import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.proyectocrm.models.Contacto

class ContactosViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val _contactos = MutableStateFlow<List<Contacto>>(emptyList())
    val contactos: StateFlow<List<Contacto>> = _contactos

    init {
        // Escuchar cambios en Firestore
        db.collection("contactos").addSnapshotListener { snapshot, e ->
            if (e != null) {
                println("Error al cargar contactos: ${e.message}")
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val nuevosContactos = snapshot.documents.mapNotNull { it.toObject(Contacto::class.java) }
                _contactos.value = nuevosContactos.distinctBy { it.email } // Aseguramos que no haya duplicados
            }
        }
    }

    // Método para agregar un nuevo contacto
    fun agregarContacto(contacto: Contacto) {
        db.collection("contactos")
            .whereEqualTo("email", contacto.email)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.isEmpty) {
                    db.collection("contactos").add(contacto)
                        .addOnSuccessListener {
                            println("Contacto agregado: ${contacto.nombre}")
                        }
                        .addOnFailureListener { e ->
                            println("Error al agregar contacto: ${e.message}")
                        }
                } else {
                    println("El contacto ya existe: ${contacto.email}")
                }
            }
            .addOnFailureListener { e ->
                println("Error al verificar existencia de contacto: ${e.message}")
            }
    }

    // Método para eliminar un contacto
    fun eliminarContacto(contacto: Contacto) {
        db.collection("contactos")
            .whereEqualTo("email", contacto.email) // Usamos email como identificador único
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot.documents) {
                    db.collection("contactos").document(document.id).delete()
                        .addOnSuccessListener {
                            println("Contacto eliminado: ${contacto.nombre}")
                        }
                        .addOnFailureListener { e ->
                            println("Error al eliminar contacto: ${e.message}")
                        }
                }
            }
            .addOnFailureListener { e ->
                println("Error al buscar contacto para eliminar: ${e.message}")
            }
    }

    // Método para actualizar un contacto existente
    fun actualizarContacto(contactoActualizado: Contacto) {
        db.collection("contactos")
            .whereEqualTo("email", contactoActualizado.email)
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot.documents) {
                    db.collection("contactos").document(document.id).set(contactoActualizado)
                        .addOnSuccessListener {
                            println("Contacto actualizado: ${contactoActualizado.nombre}")
                        }
                        .addOnFailureListener { e ->
                            println("Error al actualizar contacto: ${e.message}")
                        }
                }
            }
            .addOnFailureListener { e ->
                println("Error al buscar contacto para actualizar: ${e.message}")
            }
    }
}
