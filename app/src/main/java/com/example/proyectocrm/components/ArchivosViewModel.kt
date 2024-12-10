package com.example.proyectocrm.components


import androidx.lifecycle.ViewModel
import com.example.proyectocrm.scenes.Archivo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// Modelo para manejar la lista de archivos
class ArchivosViewModel : ViewModel() {
    private val _archivos = MutableStateFlow<List<Archivo>>(emptyList()) // Estado mutable para la lista de archivos
    val archivos: StateFlow<List<Archivo>> = _archivos // Flujo para observar los cambios en la lista

    // Función para agregar un archivo a la lista
    fun agregarArchivo(archivo: Archivo) {
        _archivos.value = _archivos.value + archivo // Agrega el archivo a la lista existente
    }
}
