
package com.example.proyectocrm.scenes
import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import com.example.proyectocrm.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaLogin(navHostController: NavHostController) {
    val auth = FirebaseAuth.getInstance()
    val context = LocalContext.current
    val message = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val rememberMe = remember { mutableStateOf(false) }

    // Configuración de Google Sign-In
    val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken("314609855316-momoqbcv11cil4bu7ru88ktuqten9al2.apps.googleusercontent.com")
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
                    auth.signInWithCredential(credential).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            message.value = "Inicio de sesión con Google exitoso"
                            navHostController.navigate("PantallaHome")
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
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        // Icono del logo
        Icon(
            painter = painterResource(id = R.drawable.ic_logo),
            contentDescription = "Logo",
            modifier = Modifier.size(64.dp),
            tint = Color(0xFF007AFF)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Título
        Text(
            text = "Inicia Sesión",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1F1F1F),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Subtítulo
        Text(
            text = "Ingresa tu correo electrónico y contraseña para iniciar sesión",
            fontSize = 14.sp,
            color = Color(0xFF5A5A5A),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Campo de correo electrónico
        OutlinedTextField(
            value = email.value,
            onValueChange = { email.value = it },
            label = { Text("Email", color = Color.Gray) },
            placeholder = { Text("yourname@gmail.com", color = Color.Gray) },
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .height(56.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Color(0xFFF5F5F5),
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color.Transparent,
                cursorColor = Color.Black
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Campo de contraseña
        OutlinedTextField(
            value = password.value,
            onValueChange = { password.value = it },
            label = { Text("Contraseña", color = Color.Gray) },
            placeholder = { Text("******", color = Color.Gray) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .height(56.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Color(0xFFF5F5F5),
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color.Transparent,
                cursorColor = Color.Black
            ),
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
                colors = CheckboxDefaults.colors(Color.Gray)
            )

            Text(text = "Recuérdame", color = Color.Gray)
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "¿Olvidaste tu contraseña?",
                color = Color(0xFF007AFF),
                modifier = Modifier.clickable { navHostController.navigate("pantallaRecuperarContrasena") }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón de iniciar sesión
        Button(
            onClick = {
                loginUser(auth, email.value, password.value, navHostController, message)
            },
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007AFF)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "Iniciar sesión", color = Color.White, fontWeight = FontWeight.SemiBold)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Separador -- o --
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(0.85f)
        ) {
            Divider(color = Color.Gray, thickness = 1.dp, modifier = Modifier.weight(1f))
            Text(
                text = " o ",
                color = Color.Gray,
                fontSize = 14.sp,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Divider(color = Color.Gray, thickness = 1.dp, modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Botón de Google
        Button(
            onClick = { googleSignInLauncher.launch(googleSignInClient.signInIntent) },
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            shape = RoundedCornerShape(8.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_google),
                contentDescription = "Google Icon",
                tint = Color.Unspecified,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Continuar con Google", color = Color.Black)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "¿No tienes una cuenta? ",
                color = Color.Gray,
                fontSize = 14.sp
            )
            Text(
                text = "Registrarse",
                color = Color(0xFF007AFF),
                fontSize = 14.sp,
                modifier = Modifier.clickable { navHostController.navigate("PantallaRegistro") }
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
                navHostController.navigate("PantallaHome")
            } else {
                message.value = "Error: ${task.exception?.message}"
            }
        }
    } else {
        message.value = "Por favor completa todos los campos"
    }
}
