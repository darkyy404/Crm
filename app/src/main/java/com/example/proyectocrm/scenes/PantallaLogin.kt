package com.example.proyectocrm.scenes

import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.proyectocrm.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

@Composable
fun PantallaLogin(navHostController: NavHostController) {
    val auth = FirebaseAuth.getInstance()
    val context = LocalContext.current
    val message = remember { mutableStateOf("") }
    val email = remember { mutableStateOf(TextFieldValue("")) }
    val password = remember { mutableStateOf(TextFieldValue("")) }

    // Configuración de Google Sign-In
    val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken("7577710609-mg8frfipf87pj48bi3ndne4t62tqjp86.apps.googleusercontent.com")
        .requestEmail()
        .build()
    val googleSignInClient = GoogleSignIn.getClient(context, googleSignInOptions)

    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                account?.let {
                    val credential = GoogleAuthProvider.getCredential(it.idToken, null)
                    auth.signInWithCredential(credential)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                message.value = "Inicio de sesión con Google exitoso"
                                navHostController.navigate("PantallaMenu")
                            } else {
                                message.value = "Error: ${task.exception?.message}"
                            }
                        }
                }
            } catch (e: ApiException) {
                message.value = "Error en Google Sign-In: ${e.message}"
                Log.e("GoogleSignIn", "Error", e)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEDF1F3)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Icono superior
        Icon(
            painter = painterResource(id = R.drawable.ic_logo),
            contentDescription = "Logo",
            modifier = Modifier.size(64.dp),
            tint = Color(0xFF007AFF)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text("Inicia Sesión", fontSize = 28.sp, color = Color(0xFF1F1F1F))
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Ingresa tu correo electrónico y contraseña para iniciar sesión",
            fontSize = 14.sp,
            color = Color(0xFF5A5A5A)
        )
        Spacer(modifier = Modifier.height(24.dp))

        // Campo de correo
        OutlinedTextField(
            value = email.value,
            onValueChange = { email.value = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(0.85f)
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Campo de contraseña
        OutlinedTextField(
            value = password.value,
            onValueChange = { password.value = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(0.85f)
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Botón de inicio de sesión
        Button(
            onClick = {
                loginUser(auth, email.value.text, password.value.text, navHostController, message)
            },
            modifier = Modifier.fillMaxWidth(0.85f),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007AFF))
        ) {
            Text("Iniciar Sesión", color = Color.White)
        }
        Spacer(modifier = Modifier.height(8.dp))

        // Botón de inicio de sesión con Google
        Button(
            onClick = { googleSignInLauncher.launch(googleSignInClient.signInIntent) },
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp)),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_google),
                    contentDescription = "Google Icon",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Iniciar sesión con Google", color = Color.Black)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Mensaje de estado
        if (message.value.isNotEmpty()) {
            Text(
                text = message.value,
                color = if (message.value.startsWith("Error")) Color.Red else Color.Green
            )
        }
    }
}

fun loginUser(
    auth: FirebaseAuth,
    email: String,
    password: String,
    navHostController: NavHostController,
    message: MutableState<String>
) {
    if (email.isNotBlank() && password.isNotBlank()) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                message.value = "Inicio de sesión exitoso"
                navHostController.navigate("PantallaMenu")
            } else {
                message.value = "Error: ${task.exception?.message}"
            }
        }
    } else {
        message.value = "Por favor completa todos los campos"
    }
}
