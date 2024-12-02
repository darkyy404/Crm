package com.example.proyectocrm.components

import androidx.lifecycle.ViewModel
import com.example.proyectocrm.models.Contacto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ContactosViewModel : ViewModel() {
    private val _contactos = MutableStateFlow(
        listOf(
            Contacto("Vincent Moody", "Último mensaje"),
            Contacto("Bradley Malone", "Visto hace 10 min"),
            Contacto("Janie Todd", "¿Te llegó la info?")
        )
    )
    val contactos: StateFlow<List<Contacto>> = _contactos

    fun buscarContactos(query: String) {
        _contactos.value = if (query.isNotEmpty()) {
            _contactos.value.filter {
                it.nombre.contains(query, ignoreCase = true)
            }
        } else {
            _contactos.value
        }
    }

    fun agregarContacto(contacto: Contacto) {
        _contactos.value = _contactos.value + contacto
    }
}
