package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        setContent {
            MaterialTheme {
                JomamboApp(auth)
            }
        }
    }
}

@Composable
fun JomamboApp(auth: FirebaseAuth) {
    val navController = rememberNavController()
    val startDestination = if (auth.currentUser != null) "home" else "login"
    
    NavHost(navController = navController, startDestination = startDestination) {
        composable("login") { LoginScreen(auth, navController) }
        composable("signup") { SignupScreen(auth, navController) }
        composable("home") { HomeScreen(auth, navController) }
    }
}

@Composable
fun LoginScreen(auth: FirebaseAuth, navController: androidx.navigation.NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Text("JOMAMBO HUSTLE", style = MaterialTheme.typography.headlineLarge, color = Color(0xFF008000))
        Spacer(modifier = Modifier.height(8.dp))
        Text("Welcome Back", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(32.dp))
        
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email))
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Password") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())
        
        if (error.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(error, color = Color.Red)
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = {
            if(email.isBlank() || password.isBlank()) { error = "Fill all fields"; return@Button }
            loading = true
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    loading = false
                    if (task.isSuccessful) {
                        navController.navigate("home") { popUpTo("login") { inclusive = true } }
                    } else {
                        error = task.exception?.message ?: "Login failed"
                    }
                }
        }, modifier = Modifier.fillMaxWidth().height(50.dp), enabled = !loading) {
            Text(if(loading) "Logging in..." else "LOGIN")
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        TextButton(onClick = { navController.navigate("signup") }) {
            Text("Don't have account? Create Account")
        }
    }
}

@Composable
fun SignupScreen(auth: FirebaseAuth, navController: androidx.navigation.NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Create JOMAMBO Account", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(32.dp))
        
        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Full Name") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email))
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Password (min 6 chars)") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())
        
        if (error.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(error, color = Color.Red)
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = {
            if(email.isBlank() || password.isBlank() || name.isBlank()) { error = "Fill all fields"; return@Button }
            loading = true
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val userId = auth.currentUser?.uid ?: ""
                        val userMap = hashMapOf("name" to name, "email" to email, "balance" to 0, "createdAt" to System.currentTimeMillis())
                        Firebase.firestore.collection("users").document(userId).set(userMap)
                            .addOnSuccessListener {
                                loading = false
                                navController.navigate("home") { popUpTo("signup") { inclusive = true } }
                            }
                    } else {
                        loading = false
                        error = task.exception?.message ?: "Signup failed"
                    }
                }
        }, modifier = Modifier.fillMaxWidth().height(50.dp), enabled = !loading) {
            Text(if(loading) "Creating..." else "CREATE ACCOUNT")
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        TextButton(onClick = { navController.popBackStack() }) {
            Text("Already have account? Login")
        }
    }
}

@Composable
fun HomeScreen(auth: FirebaseAuth, navController: androidx.navigation.NavController) {
    val user = auth.currentUser
    var balance by remember { mutableStateOf("0.00") }
    
    LaunchedEffect(Unit) {
        user?.uid?.let { uid ->
            Firebase.firestore.collection("users").document(uid).get()
                .addOnSuccessListener { doc ->
                    val bal = doc.getLong("balance") ?: 0L
                    balance = bal.toString()
                }
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Text("JOMAMBO", style = MaterialTheme.typography.headlineLarge, color = Color(0xFF008000))
        Spacer(modifier = Modifier.height(16.dp))
        Text("Welcome!", style = MaterialTheme.typography.titleLarge)
        Text(user?.email ?: "", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(32.dp))
        
        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color(0xFF008000))) {
            Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Your Balance", color = Color.White)
                Text("₦ $balance", style = MaterialTheme.typography.headlineLarge, color = Color.White)
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = {
            auth.signOut()
            navController.navigate("login") { popUpTo("home") { inclusive = true } }
        }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) {
            Text("LOGOUT")
        }
    }
}
