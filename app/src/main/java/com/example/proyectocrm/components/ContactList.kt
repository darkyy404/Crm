package com.example.proyectocrm.components

import Contacto
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable

@Composable
fun ContactList(contactos: List<Contacto>, onContactClick: (Contacto) -> Unit) {
    LazyColumn {
        items(contactos) { contacto ->
            ContactoCard(contacto = contacto, onClick = { onContactClick(contacto) })
        }
    }
}

fun onContactClick(contacto: Int) {
    TODO("Not yet implemented")
}

private fun LazyListScope.items(
    count: List<Contacto>,
    itemContent: @Composable() (LazyItemScope.(index: Int) -> Unit)
) {
    TODO("Not yet implemented")
}

@Composable
fun ContactoCard(contacto: Int, onClick: () -> Unit) {
    TODO("Not yet implemented")
}
