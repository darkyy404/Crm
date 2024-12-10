package com.example.proyectocrm.components

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectocrm.scenes.Archivo
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.*

class ArchivosViewModel : ViewModel() {
    private val _archivos = MutableStateFlow<List<Archivo>>(emptyList()) // Estado mutable para los archivos
    val archivos: StateFlow<List<Archivo>> = _archivos // Flujo observable

    private val storageRef = FirebaseStorage.getInstance().reference // Referencia a Firebase Storage

    /**
     * Agrega un archivo localmente.
     * Este método es útil si estás manipulando archivos localmente.
     */
    fun agregarArchivo(archivo: Archivo) {
        _archivos.value = _archivos.value + archivo // Agregar archivo a la lista actual
    }

    /**
     * Carga archivos desde Firebase.
     */
    fun cargarArchivosDesdeFirebase() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Simulación de carga: Ajusta para cargar archivos reales de tu base de datos (Firestore, Storage, etc.)
                val files = storageRef.child("uploads").listAll().await().items.map { storageItem ->
                    val url = storageItem.downloadUrl.await().toString()
                    Archivo(nombre = storageItem.name, tipo = "Desconocido") // Ajusta según tu lógica
                }
                _archivos.value = files
            } catch (e: Exception) {
                e.printStackTrace() // Manejar errores (e.g., logs)
            }
        }
    }

    /**
     * Sube un archivo a Firebase Storage.
     */
    fun subirArchivoAFirebase(uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val fileName = UUID.randomUUID().toString() // Generar un nombre único
                val uploadTask = storageRef.child("uploads/$fileName").putFile(uri).await()
                val archivo = Archivo(
                    nombre = fileName,
                    tipo = "Desconocido" // Ajusta según el tipo real
                )
                _archivos.value = _archivos.value + archivo // Actualizar la lista local
            } catch (e: Exception) {
                e.printStackTrace() // Manejar errores
            }
        }
    }
}
