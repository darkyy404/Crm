package com.example.proyectocrm.models

data class Contacto(
    var nombre: String = "",
    var ultimoMensaje: String = "",
    var rol: String = "",
    var email: String = "",
    var telefono: String = "",
    var direccion: String = "",
    var categoria: String = ""
) {
    // Constructor vacío requerido por Firebase
    constructor() : this("", "", "", "", "", "", "")
}
