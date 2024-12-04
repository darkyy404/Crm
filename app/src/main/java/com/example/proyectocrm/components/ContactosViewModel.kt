import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.proyectocrm.models.Contacto

class ContactosViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val _contactos = MutableStateFlow<List<Contacto>>(emptyList())
    val contactos: StateFlow<List<Contacto>> = _contactos

    private val allContactos = mutableListOf<Contacto>() // Lista completa de contactos

    init {
        // Escuchar cambios en Firestore
        db.collection("contactos").addSnapshotListener { snapshot, e ->
            if (e != null) {
                println("Error al cargar contactos: ${e.message}")
                return@addSnapshotListener
            }
            if (snapshot != null && !snapshot.isEmpty) {
                val nuevosContactos = snapshot.documents.mapNotNull { it.toObject(Contacto::class.java) }
                allContactos.clear()
                allContactos.addAll(nuevosContactos)
                _contactos.value = allContactos // Actualizamos la lista observada
            }
        }
    }

    // Método para agregar un nuevo contacto
    fun agregarContacto(contacto: Contacto) {
        db.collection("contactos").add(contacto)
            .addOnSuccessListener {
                println("Contacto agregado: $contacto")
                allContactos.add(contacto)
                _contactos.value = allContactos // Actualizamos la lista observada
            }
            .addOnFailureListener { e ->
                println("Error al agregar contacto: ${e.message}")
            }
    }
}
