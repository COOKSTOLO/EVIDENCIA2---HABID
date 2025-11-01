package com.example.actividad6_menuopcionesapp

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.actividad6_menuopcionesapp.ui.theme.Actividad6MenuOpcionesAppTheme
import kotlinx.coroutines.launch
import java.util.Calendar

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Actividad6MenuOpcionesAppTheme {
                MenuOpcionesApp()
            }
        }
    }
}

private enum class DrawerScreen(val title: String) {
    Login("Login"),
    Edad("Edad"),
    Switch("Control (Switch)"),
    Contador("Contador")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MenuOpcionesApp() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var current by rememberSaveable { mutableStateOf(DrawerScreen.Login) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(Modifier.height(12.dp))
                DrawerItem(
                    text = DrawerScreen.Login.title,
                    selected = current == DrawerScreen.Login
                ) {
                    current = DrawerScreen.Login
                    scope.launch { drawerState.close() }
                }
                DrawerItem(
                    text = DrawerScreen.Edad.title,
                    selected = current == DrawerScreen.Edad
                ) {
                    current = DrawerScreen.Edad
                    scope.launch { drawerState.close() }
                }
                DrawerItem(
                    text = DrawerScreen.Switch.title,
                    selected = current == DrawerScreen.Switch
                ) {
                    current = DrawerScreen.Switch
                    scope.launch { drawerState.close() }
                }
                DrawerItem(
                    text = DrawerScreen.Contador.title,
                    selected = current == DrawerScreen.Contador
                ) {
                    current = DrawerScreen.Contador
                    scope.launch { drawerState.close() }
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(current.title) },
                    navigationIcon = {
                        // Usamos un botón de texto simple para evitar dependencias de íconos
                        Text(
                            text = "☰",
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .clickable { scope.launch { drawerState.open() } }
                        )
                    }
                )
            }
        ) { innerPadding ->
            Box(Modifier.padding(innerPadding).fillMaxSize()) {
                when (current) {
                    DrawerScreen.Login -> LoginScreen()
                    DrawerScreen.Edad -> AgeScreen()
                    DrawerScreen.Switch -> SwitchScreen()
                    DrawerScreen.Contador -> CounterScreen()
                }
            }
        }
    }
}

@Composable
private fun DrawerItem(text: String, selected: Boolean, onClick: () -> Unit) {
    val color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = text, color = color, style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
private fun LoginScreen() {
    val ctx = LocalContext.current
    var user by rememberSaveable { mutableStateOf("") }
    var pass by rememberSaveable { mutableStateOf("") }
    var error by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        OutlinedTextField(
            value = user,
            onValueChange = { user = it },
            label = { Text("Usuario") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = pass,
            onValueChange = { pass = it },
            label = { Text("Contraseña") },
            visualTransformation = if (pass.isEmpty()) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions.Default,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))
        Button(onClick = {
            if (user == "admin" && pass == "1234") {
                error = ""
                Toast.makeText(ctx, "Sesión iniciada. ¡Bienvenido $user!", Toast.LENGTH_LONG).show()
            } else {
                error = "Credenciales no válidas. Intente nuevamente."
            }
        }) {
            Text("Ingresar")
        }
        if (error.isNotEmpty()) {
            Spacer(Modifier.height(8.dp))
            Text(text = error, color = MaterialTheme.colorScheme.error)
        }
    }
}

@Composable
private fun AgeScreen() {
    val ctx = LocalContext.current
    var ageText by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Button(onClick = {
            val today = Calendar.getInstance()
            val y = today.get(Calendar.YEAR)
            val m = today.get(Calendar.MONTH)
            val d = today.get(Calendar.DAY_OF_MONTH)
            val dialog = DatePickerDialog(ctx, { _, year, month, dayOfMonth ->
                val age = calculateAge(year, month, dayOfMonth)
                ageText = "Edad: $age años"
            }, y, m, d)
            dialog.datePicker.maxDate = today.timeInMillis
            dialog.show()
        }) {
            Text("Seleccionar fecha de nacimiento")
        }
        if (ageText.isNotEmpty()) {
            Spacer(Modifier.height(12.dp))
            Text(text = ageText, style = MaterialTheme.typography.titleMedium)
        }
    }
}

private fun calculateAge(year: Int, month: Int, day: Int): Int {
    val dob = Calendar.getInstance().apply { set(year, month, day) }
    val today = Calendar.getInstance()
    var age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR)
    val hasNotHadBirthdayThisYear =
        today.get(Calendar.MONTH) < dob.get(Calendar.MONTH) ||
                (today.get(Calendar.MONTH) == dob.get(Calendar.MONTH) &&
                        today.get(Calendar.DAY_OF_MONTH) < dob.get(Calendar.DAY_OF_MONTH))
    if (hasNotHadBirthdayThisYear) age--
    return age.coerceAtLeast(0)
}

@Composable
private fun SwitchScreen() {
    var enabled by rememberSaveable { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Switch(checked = enabled, onCheckedChange = { enabled = it })
            Spacer(Modifier.width(8.dp))
            Text(if (enabled) "Encendido" else "Apagado")
        }
    }
}

@Composable
private fun CounterScreen() {
    var count by rememberSaveable { mutableStateOf(0) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(Modifier.height(24.dp))
        Text(text = "$count", style = MaterialTheme.typography.displayLarge)
        Spacer(Modifier.height(16.dp))
        Button(onClick = { count++ }) { Text("Incrementar") }
    }
}