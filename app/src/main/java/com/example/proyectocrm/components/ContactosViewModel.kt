package com.example.proyectocrm.components

import androidx.lifecycle.ViewModel
import com.example.proyectocrm.models.Contacto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ContactosViewModel : ViewModel() {
    private val _contactos = MutableStateFlow<List<Contacto>>(emptyList())
    val contactos: StateFlow<List<Contacto>> = _contactos

    fun agregarContacto(contacto: Contacto) {
        _contactos.value = _contactos.value + contacto
    }

    fun buscarContactos(query: String) {
        _contactos.value = _contactos.value.filter {
            it.nombre.contains(query, ignoreCase = true)
        }
    }
}
