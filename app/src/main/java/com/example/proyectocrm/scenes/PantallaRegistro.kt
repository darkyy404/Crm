package com.example.proyectocrm.scenes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.proyectocrm.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.text.input.VisualTransformation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen() {
    val name = remember { mutableStateOf(TextFieldValue("")) }
    val lastName = remember { mutableStateOf(TextFieldValue("")) }
    val email = remember { mutableStateOf(TextFieldValue("")) }
    val phone = remember { mutableStateOf(TextFieldValue("")) }
    val password = remember { mutableStateOf(TextFieldValue("")) }
    val confirmPassword = remember { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEDF1F3)), // Fondo gris claro
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier
                    .padding(start = 16.dp)
                    .size(24.dp)
                    .clickable { /* Acción de volver atrás */ },
                tint = Color.Black
            )
        }

        Text(
            text = "Registro",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1F1F1F),
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(start = 16.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "¡Crea una cuenta para continuar!",
            fontSize = 14.sp,
            color = Color(0xFF5A5A5A),
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(start = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo de nombre
        CustomOutlinedTextField(value = name.value, label = "Nombre", onValueChange = { name.value = it })

        // Campo de apellido
        CustomOutlinedTextField(value = lastName.value, label = "Apellidos", onValueChange = { lastName.value = it })

        // Campo de email
        CustomOutlinedTextField(value = email.value, label = "Email", onValueChange = { email.value = it })

        // Campo de número telefónico
        CustomOutlinedTextField(value = phone.value, label = "Número Telefónico", onValueChange = { phone.value = it })

        // Campo de contraseña
        CustomOutlinedTextField(
            value = password.value,
            label = "Establecer contraseña",
            onValueChange = { password.value = it },
            isPassword = true
        )

        // Campo de confirmar contraseña
        CustomOutlinedTextField(
            value = confirmPassword.value,
            label = "Repetir contraseña",
            onValueChange = { confirmPassword.value = it },
            isPassword = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { /* Acción de registro */ },
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007AFF)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "Registro", color = Color.White, fontWeight = FontWeight.SemiBold)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row {
            Text(
                text = "¿Ya tienes una cuenta? ",
                color = Color.Gray,
                fontSize = 14.sp
            )
            Text(
                text = "Iniciar sesión",
                color = Color(0xFF007AFF),
                fontSize = 14.sp,
                modifier = Modifier.clickable { /* Acción de iniciar sesión */ }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomOutlinedTextField(
    value: TextFieldValue,
    label: String,
    onValueChange: (TextFieldValue) -> Unit,
    isPassword: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = Color.Gray) },
        modifier = Modifier
            .fillMaxWidth(0.85f)
            .height(56.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            containerColor = Color(0xFFF5F5F5),
            unfocusedBorderColor = Color.Transparent,
            focusedBorderColor = Color.Transparent,
            cursorColor = Color.Black // Color del cursor
        ),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        trailingIcon = if (isPassword) {
            {
                Icon(
                    painter = painterResource(id = R.drawable.ic_visibility), // Ícono de visibilidad
                    contentDescription = "Visibility Icon",
                    modifier = Modifier.size(20.dp)
                )
            }
        } else null,
        shape = RoundedCornerShape(8.dp),
        singleLine = true
    )

}

@Preview(showBackground = true)
@Composable
fun PreviewRegisterScreen() {
    RegisterScreen()
}
