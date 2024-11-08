package com.example.proyectocrm.scenes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.proyectocrm.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen() {
    val email = remember { mutableStateOf(TextFieldValue("")) }
    val password = remember { mutableStateOf(TextFieldValue("")) }
    val rememberMe = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEDF1F3)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Icono superior
        Icon(
            painter = painterResource(id = R.drawable.ic_logo), // Reemplaza con el recurso del ícono
            contentDescription = "Logo",
            modifier = Modifier.size(64.dp),
            tint = Color(0xFF007AFF)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Inicia Sesión",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1F1F1F)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Ingresa tu correo electrónico y contraseña para iniciar sesión",
            fontSize = 14.sp,
            color = Color(0xFF5A5A5A),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Campo de correo
        OutlinedTextField(
            value = email.value,
            onValueChange = { email.value = it },
            label = { Text("Email") },
            placeholder = { Text("yourname@gmail.com", color = Color.Gray) }, // Configura el color del placeholder aquí
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_email), // Ícono de email
                    contentDescription = "Email Icon"
                )
            },
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .height(56.dp)
                .background(Color(0xFFE1E1E1), RoundedCornerShape(8.dp)),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Color(0xE1E1E1),
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            ),
            shape = RoundedCornerShape(8.dp),
            singleLine = true
        )


        Spacer(modifier = Modifier.height(8.dp))

        // Campo de contraseña
        OutlinedTextField(
            value = password.value,
            onValueChange = { password.value = it },
            label = { Text("Contraseña") },
            placeholder = { Text("******", color = Color.Gray) }, // Configura el color del placeholder aquí
            visualTransformation = PasswordVisualTransformation(),
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_lock), // Ícono de candado
                    contentDescription = "Lock Icon"
                )
            },
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_visibility), // Ícono de visibilidad
                    contentDescription = "Visibility Icon"
                )
            },
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .height(56.dp)
                .background(Color(0xFFE1E1E1), RoundedCornerShape(8.dp)),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Color(0xF5F5F5),
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            ),
            shape = RoundedCornerShape(8.dp),
            singleLine = true
        )


        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(0.85f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = rememberMe.value,
                onCheckedChange = { rememberMe.value = it },
                colors = CheckboxDefaults.colors(checkmarkColor = Color(0xFF007AFF))
            )
            Text(text = "Recuérdame", color = Color.Gray)
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "¿Olvidaste tu contraseña?",
                color = Color(0xFF007AFF),
                modifier = Modifier.clickable { /* Acción de recuperación */ }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { /* Acción de iniciar sesión */ },
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007AFF)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "Iniciar sesión", color = Color.White, fontWeight = FontWeight.SemiBold)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Separador
        Row(
            modifier = Modifier.fillMaxWidth(0.85f),
            horizontalArrangement = Arrangement.Center
        ) {
            Divider(
                color = Color.Gray,
                thickness = 1.dp,
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            )
            Text(
                text = " o ",
                color = Color.Gray,
                fontSize = 14.sp,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Divider(
                color = Color.Gray,
                thickness = 1.dp,
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Botón de Google
        Button(
            onClick = { /* Acción de iniciar sesión con Google */ },
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .height(50.dp)
                .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp)),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            shape = RoundedCornerShape(8.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_google), // Ícono de Google
                contentDescription = "Google Icon",
                tint = Color.Unspecified
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Continuar con Google", color = Color.Black)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Botón de Facebook
        Button(
            onClick = { /* Acción de iniciar sesión con Facebook */ },
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .height(50.dp)
                .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp)),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            shape = RoundedCornerShape(8.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_facebook), // Ícono de Facebook
                contentDescription = "Facebook Icon",
                tint = Color.Unspecified
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Continuar con Facebook", color = Color.Black)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row {
            Text(
                text = "¿No tienes una cuenta? ",
                color = Color.Gray,
                fontSize = 14.sp
            )
            Text(
                text = "Registrarse",
                color = Color(0xFF007AFF),
                fontSize = 14.sp,
                modifier = Modifier.clickable { /* Acción de registro */ }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    LoginScreen()
}
